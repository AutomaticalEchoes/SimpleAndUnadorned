package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SusPillager;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SusPillager.Goal.SusPillagerChargeBullets;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SusPillager.Goal.SusPillagerCrossbowAttackGoal;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;
import java.util.Map;

public class SusPillager extends AbstractIllager implements CrossbowAttackMob{
    private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(Pillager.class, EntityDataSerializers.BOOLEAN);
    private static final int INVENTORY_SIZE = 5;
    private static final int SLOT_OFFSET = 300;
    private static final float CROSSBOW_POWER = 1.6F;
    private CrossbowState crossbowState = CrossbowState.UNCHARGED;
    private int attackDelay;
    private int bullets = 6;
    private int lastReamingTick = 0;
    private int chargeCoolDown = 0;
    private final SimpleContainer inventory = new SimpleContainer(5);
    public SusPillager(EntityType<? extends AbstractIllager> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new Raider.HoldGroundAttackGoal(this, 10.0F));
        this.goalSelector.addGoal(3, new SusPillagerChargeBullets(this));
        this.goalSelector.addGoal(3, new SusPillagerCrossbowAttackGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 15.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 15.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true,LivingEntity::attackable));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, (double)0.35F).add(Attributes.MAX_HEALTH, 24.0D).add(Attributes.ATTACK_DAMAGE, 5.0D).add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_CHARGING_CROSSBOW, false);
    }

    public void addAdditionalSaveData(CompoundTag p_33300_) {
        super.addAdditionalSaveData(p_33300_);
        ListTag listtag = new ListTag();

        for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
                listtag.add(itemstack.save(new CompoundTag()));
            }
        }

        p_33300_.putInt("bullet", this.bullets);
        p_33300_.put("Inventory", listtag);
    }

    public void readAdditionalSaveData(CompoundTag p_33291_) {
        super.readAdditionalSaveData(p_33291_);
        this.bullets = p_33291_.getInt("bullet");
        ListTag listtag = p_33291_.getList("Inventory", 10);

        for(int i = 0; i < listtag.size(); ++i) {
            ItemStack itemstack = ItemStack.of(listtag.getCompound(i));
            if (!itemstack.isEmpty()) {
                this.inventory.addItem(itemstack);
            }
        }

        this.setCanPickUpLoot(true);
    }


    public AbstractIllager.IllagerArmPose getArmPose() {
        if (this.isChargingCrossbow()) {
            return AbstractIllager.IllagerArmPose.CROSSBOW_CHARGE;
        } else if (this.isHolding(is -> is.getItem() instanceof net.minecraft.world.item.CrossbowItem)) {
            return AbstractIllager.IllagerArmPose.CROSSBOW_HOLD;
        } else {
            return this.isAggressive() ? AbstractIllager.IllagerArmPose.ATTACKING : AbstractIllager.IllagerArmPose.NEUTRAL;
        }
    }



    public float getWalkTargetValue(BlockPos p_33288_, LevelReader p_33289_) {
        return 0.0F;
    }

    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_33282_, DifficultyInstance p_33283_, MobSpawnType p_33284_, @Nullable SpawnGroupData p_33285_, @Nullable CompoundTag p_33286_) {
        RandomSource randomsource = p_33282_.getRandom();
        this.populateDefaultEquipmentSlots(randomsource, p_33283_);
        this.populateDefaultEquipmentEnchantments(randomsource, p_33283_);
        return super.finalizeSpawn(p_33282_, p_33283_, p_33284_, p_33285_, p_33286_);
    }

    protected void populateDefaultEquipmentSlots(RandomSource p_219059_, DifficultyInstance p_219060_) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
    }

    public boolean isAlliedTo(Entity p_33314_) {
        if (super.isAlliedTo(p_33314_)) {
            return true;
        } else if (p_33314_ instanceof LivingEntity && ((LivingEntity)p_33314_).getMobType() == MobType.ILLAGER) {
            return this.getTeam() == null && p_33314_.getTeam() == null;
        } else {
            return false;
        }
    }

    public boolean canFireProjectileWeapon(ProjectileWeaponItem p_33280_) {
        return p_33280_ == Items.CROSSBOW;
    }

    public boolean isChargingCrossbow() {
        return this.entityData.get(IS_CHARGING_CROSSBOW);
    }

    public void setChargingCrossbow(boolean p_33302_) {
        this.entityData.set(IS_CHARGING_CROSSBOW, p_33302_);
    }

    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.PILLAGER_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PILLAGER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource p_33306_) {
        return SoundEvents.PILLAGER_HURT;
    }

    public void shootCrossbowProjectile(LivingEntity p_33275_, ItemStack p_33276_, Projectile p_33277_, float p_33278_) {
        this.shootCrossbowProjectile(this, p_33275_, p_33277_, p_33278_, 1.6F);
        this.level.addFreshEntity(p_33277_);
    }

    public SimpleContainer getInventory() {
        return this.inventory;
    }

    protected void pickUpItem(ItemEntity p_33296_) {
        ItemStack itemstack = p_33296_.getItem();
        if (itemstack.getItem() instanceof BannerItem) {
            super.pickUpItem(p_33296_);
        } else if (this.wantsItem(itemstack)) {
            this.onItemPickup(p_33296_);
            ItemStack itemstack1 = this.inventory.addItem(itemstack);
            if (itemstack1.isEmpty()) {
                p_33296_.discard();
            } else {
                itemstack.setCount(itemstack1.getCount());
            }
        }

    }

    private boolean wantsItem(ItemStack p_149745_) {
        return this.hasActiveRaid() && p_149745_.is(Items.WHITE_BANNER);
    }

    public SlotAccess getSlot(int p_149743_) {
        int i = p_149743_ - 300;
        return i >= 0 && i < this.inventory.getContainerSize() ? SlotAccess.forContainer(this.inventory, i) : super.getSlot(p_149743_);
    }

    public void applyRaidBuffs(int p_33267_, boolean p_33268_) {
        Raid raid = this.getCurrentRaid();
        boolean flag = this.random.nextFloat() <= raid.getEnchantOdds();
        if (flag) {
            ItemStack itemstack = new ItemStack(Items.CROSSBOW);
            Map<Enchantment, Integer> map = Maps.newHashMap();
            if (p_33267_ > raid.getNumGroups(Difficulty.NORMAL)) {
                map.put(Enchantments.QUICK_CHARGE, 2);
            } else if (p_33267_ > raid.getNumGroups(Difficulty.EASY)) {
                map.put(Enchantments.QUICK_CHARGE, 1);
            }

            map.put(Enchantments.MULTISHOT, 1);
            EnchantmentHelper.setEnchantments(map, itemstack);
            this.setItemSlot(EquipmentSlot.MAINHAND, itemstack);
        }

    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.PILLAGER_CELEBRATE;
    }
//    ______________________________________________________________________________________________
    public CrossbowState getCrossbowState() {
        return crossbowState;
    }

    public boolean canCharge(){
        return chargeCoolDown <= 0;
    }

    @Override
    public void tick() {
        super.tick();
        if(chargeCoolDown > 0) chargeCoolDown --;
        if(this.getCrossbowState() == CrossbowState.UNCHARGED && getBullet() > 0) this.crossbowState = CrossbowState.CHARGED;
        ChargingBullet();
        ReadyToAttack();
    }

    public void StartChargeBullet(){
//        if(this.crossbowState != CrossbowState.UNCHARGED) return;
        this.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof CrossbowItem));
        this.crossbowState = CrossbowState.CHARGING;
        this.setChargingCrossbow(true);
    }

    public void ChargingBullet(){
        if(this.crossbowState != CrossbowState.CHARGING) return;
        if (!this.isUsingItem()) {
            this.crossbowState = CrossbowState.UNCHARGED;
        }
        int i = this.getTicksUsingItem();
        if (i >= CrossbowItem.getChargeDuration(this.getUseItem())) {
            this.releaseUsingItem();
            this.setChargingCrossbow(false);
            this.crossbowState = CrossbowState.READY_TO_ATTACK;
            this.attackDelay = 10;
            this.addBullet(3);
            this.chargeCoolDown = 30;
        }
    }

    public void ReadyToAttack(){
        if(this.crossbowState != CrossbowState.READY_TO_ATTACK) return;
        --this.attackDelay;
        if(attackDelay <=10 && attackDelay > 0){
            ItemStack crossBow = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof CrossbowItem));
            CrossbowItem.setCharged(crossBow, true);
        }else if (attackDelay <= 0){
            this.crossbowState = CrossbowState.CHARGED;
        }
    }

    public void Attack(LivingEntity livingEntity,boolean blitz){
        if(this.crossbowState != CrossbowState.CHARGED) return;
        ItemStack crossBow = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof CrossbowItem));
        this.shootCrossbowProjectile(livingEntity ,crossBow ,Magazine.RandomAttackTube(this.level,this) ,1.6F);
        CrossbowItem.setCharged(crossBow, false);
        this.bulletUsed(1);

        if(getBullet() > 0) {
            this.crossbowState = CrossbowState.READY_TO_ATTACK;
            this.attackDelay = blitz ? 10 : 20 + this.getRandom().nextInt(20);
        }else{
            this.crossbowState = CrossbowState.UNCHARGED;
        }
    }

    @Override
    public void startUsingItem(InteractionHand p_21159_) {
        super.startUsingItem(p_21159_);
        this.lastReamingTick = tickCount;
    }

    @Override
    protected void updateUsingItem(ItemStack p_147201_) {
        p_147201_.onUseTick(this.level, this, this.getUseItemRemainingTicks());
        if (this.shouldTriggerItemUseEffects()) {
            this.triggerItemUseEffects(p_147201_, 5);
        }

        if((tickCount - lastReamingTick) % 2 == 0){
            --this.useItemRemaining;
        }

        if (this.useItemRemaining == 0 && !this.level.isClientSide && !p_147201_.useOnRelease()) {
            this.completeUsingItem();
        }
    }
    private boolean shouldTriggerItemUseEffects() {
        int i = this.getUseItemRemainingTicks();
        FoodProperties foodproperties = this.useItem.getFoodProperties(this);
        boolean flag = foodproperties != null && foodproperties.isFastFood();
        flag |= i <= this.useItem.getUseDuration() - 7;
        return flag && i % 4 == 0;
    }

    public int getBullet() {
        return bullets;
    }

    public void addBullet(int i){
        this.bullets = Math.min(16 ,bullets + i);
    }

    public void bulletUsed(int i){
        this.bullets -= i;
    }

    @Override
    public void performRangedAttack(LivingEntity p_33317_, float p_33318_) {

    }

    public enum CrossbowState {
        UNCHARGED,
        CHARGING,
        CHARGED,
        READY_TO_ATTACK;
    }

}
