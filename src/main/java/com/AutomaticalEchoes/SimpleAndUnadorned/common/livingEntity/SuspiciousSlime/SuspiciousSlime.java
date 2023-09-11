package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime;

import com.AutomaticalEchoes.SimpleAndUnadorned.api.HasExperience;
import com.AutomaticalEchoes.SimpleAndUnadorned.api.IExperienceOrb;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.blockEntity.SusSlimeBase.SusSlimeBase;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.event.SusSlimeWantExpEvent;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.Goal.*;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.projectile.AcidityBall;
import com.AutomaticalEchoes.SimpleAndUnadorned.config.ModCommonConfig;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.EntityRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.FluidRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.ItemsRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.PoiTypeRegister;
import com.google.common.annotations.VisibleForTesting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SuspiciousSlime extends Mob implements Enemy, InventoryCarrier , HasExperience {
    private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(SuspiciousSlime.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> POWER = SynchedEntityData.defineId(SuspiciousSlime.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> EXP = SynchedEntityData.defineId(SuspiciousSlime.class, EntityDataSerializers.INT);
    private final int TRANSLATE_TICK = 20 * ModCommonConfig.SUSPICIOUS_SLIME_TRANSLATE_TICK.get();
    private final int COLLECT_TICK = 20 * ModCommonConfig.SUSPICIOUS_SLIME_WANT_COLLECT_TICK.get();
    private final int ACIDITY_PREPARE_TICK = 20 * ModCommonConfig.SUSPICIOUS_SLIME_ACIDITY_PREPARE.get() * 20;
    private @Nullable BlockPos base;
    private boolean wasOnGround;
    private SimpleContainer container = new SimpleContainer(4);
    private int translateTick = TRANSLATE_TICK;
    private int wantCollectItem = COLLECT_TICK;
    private int acidityPrepareTime = ACIDITY_PREPARE_TICK;
    private boolean brave = false;
    private boolean hasProject = false;
    public LocateSusSlimeBlock locateSusSlimeBlock;
    public float targetSquish;
    public float squish;
    public float oSquish;

    public SuspiciousSlime(EntityType<? extends SuspiciousSlime> p_33588_, Level p_33589_) {
        super(p_33588_, p_33589_);
        this.moveControl=new IMoveControl(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_SIZE, 1);
        this.entityData.define(POWER, 1);
        this.entityData.define(EXP,0);
    }

    @VisibleForTesting
    public void setSize(int p_33594_, boolean p_33595_) {
        int i = Mth.clamp(p_33594_, 1, 127);
        this.entityData.set(ID_SIZE, i);
        this.reapplyPosition();
        this.refreshDimensions();
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(i * i);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2F + 0.1F * (float)i);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(i);
        if (p_33595_) {
            this.setHealth(this.getMaxHealth());
            this.removeAllEffects();
        }
        this.xpReward = i;
    }

    public void addAdditionalSaveData(CompoundTag p_33619_) {
        super.addAdditionalSaveData(p_33619_);
        p_33619_.putInt("size", this.getSize());
        p_33619_.putInt("power", this.getPower());
        p_33619_.putInt("exp",getExperience());
        p_33619_.putInt("acidity",this.acidityPrepareTime);
        p_33619_.putBoolean("wasOnGround", this.wasOnGround);
        p_33619_.put("slimeContainer", this.container.createTag());
    }

    public void readAdditionalSaveData(CompoundTag p_33607_) {
        super.readAdditionalSaveData(p_33607_);
        this.setSize(p_33607_.getInt("size"),true);
        this.setPower(p_33607_.getInt("power"));
        this.addExperience(p_33607_.getInt("exp"));
        this.acidityPrepareTime = p_33607_.getInt("acidity");
        this.wasOnGround = p_33607_.getBoolean("wasOnGround");
        this.container.fromTag(p_33607_.getList("slimeContainer",10));
    }

    public boolean isTiny() {
        return this.getSize() <= 1;
    }

    protected ParticleOptions getParticleType() {
        return ParticleTypes.ITEM_SLIME;
    }

    protected boolean shouldDespawnInPeaceful() {
        return this.getSize() > 0;
    }
    @Override
    public void tick() {
        this.squish += (this.targetSquish - this.squish) * 0.5F;
        this.oSquish = this.squish;
        super.tick();
        if (this.onGround && !this.wasOnGround) {
            int i = this.getSize();

            if (spawnCustomParticles()) i = 0; // don't spawn particles if it's handled by the implementation itself
            for(int j = 0; j < i * 8; ++j) {
                float f = this.random.nextFloat() * ((float)Math.PI * 2F);
                float f1 = this.random.nextFloat() * 0.5F + 0.5F;
                float f2 = Mth.sin(f) * (float)i * 0.5F * f1;
                float f3 = Mth.cos(f) * (float)i * 0.5F * f1;
                this.level.addParticle(this.getParticleType(), this.getX() + (double)f2, this.getY(), this.getZ() + (double)f3, 0.0D, 0.0D, 0.0D);
            }

            this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            this.targetSquish = -0.5F;
        } else if (!this.onGround && this.wasOnGround) {
            this.targetSquish = 1.0F;
        }

        if(this.wantCollectItem >=0) wantCollectItem--;
        if(this.acidityPrepareTime >=0) acidityPrepareTime--;
        this.wasOnGround = this.onGround;
        this.decreaseSquish();

        if(this.level.isClientSide) return;
        translateTick();
        int rawBrightness = this.level.getRawBrightness(this.blockPosition(), 0);
        this.setBrave(rawBrightness < 11 && rawBrightness > 7);
    }

    protected void decreaseSquish() {
        this.targetSquish *= 0.6F;
    }

    public int getJumpDelay() {
        return this.random.nextInt(20) + 10;
    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_33609_) {
        if (ID_SIZE.equals(p_33609_)) {
            this.refreshDimensions();
            this.setYRot(this.yHeadRot);
            this.yBodyRot = this.yHeadRot;
            if (this.isInWater() && this.random.nextInt(20) == 0) {
                this.doWaterSplashEffect();
            }
        }
        super.onSyncedDataUpdated(p_33609_);
    }


    @Override
    protected void registerGoals() {
        locateSusSlimeBlock = new LocateSusSlimeBlock(this);
        this.goalSelector.addGoal(1, new IFloatGoal(this));
        this.goalSelector.addGoal(1, locateSusSlimeBlock);
        this.goalSelector.addGoal(2, new AttackGoal(this));
        this.goalSelector.addGoal(4, new RandomDirectionGoal(this));
        this.goalSelector.addGoal(5, new ShootAcidity(this));
        this.goalSelector.addGoal(6, new LocateItem(this));
        this.goalSelector.addGoal(6, new WantSaveItem(this));
        this.goalSelector.addGoal(6, new LocateMerge(this));
        this.goalSelector.addGoal(7, new EscapeWhileSmallGoal(this));
        this.goalSelector.addGoal(8, new KeepJumpingGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (p_33641_) -> Math.abs(p_33641_.getY() - this.getY()) <= 4.0D));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        return (p_21016_.isFire() || p_21016_.isMagic()) && super.hurt(p_21016_, p_21017_);
    }

    @Override
    public void die(DamageSource p_21014_) {
        int i = this.getSize();
        if (!this.level.isClientSide && this.isDeadOrDying()) {
            if(i > 1){
                float f = (float)i / 4.0F;
                int j = i / 2;
                int k = this.random.nextInt(4);
                for(int l = 0; l < 4; ++l) {
                    float f1 = ((float)(l % 2) - 0.5F) * f;
                    float f2 = ((float)(l / 2) - 0.5F) * f;
                    if(l == k){
                        tranSmall(j,f1,f2);
                    }else {
                        tranTotalSmall(j,f1,f2);
                    }
                }
            }else {
                dropCarried();
                Containers.dropItemStack(this.level,getX(),getY(),getZ(),new ItemStack(ItemsRegister.SUSPICIOUS_SLIME_BALL.get()));
            }
        }
        super.die(p_21014_);
    }


    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    public void push(Entity p_33636_) {
        super.push(p_33636_);
        if (p_33636_ instanceof IronGolem && this.isDealsDamage()) {
            this.dealDamage((LivingEntity)p_33636_);
        }
    }

    public void playerTouch(Player p_33611_) {
        if (this.isDealsDamage()) {
            this.dealDamage(p_33611_);
        }
    }

    protected void dealDamage(LivingEntity p_33638_) {
        if (this.isAlive()) {
            int i = this.getSize();
            if (this.distanceToSqr(p_33638_) < 0.6D * (double)i * 0.6D * (double)i && this.hasLineOfSight(p_33638_) && p_33638_.hurt(DamageSource.mobAttack(this), this.getAttackDamage())) {
                this.playSound(SoundEvents.SLIME_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                this.doEnchantDamageEffects(this, p_33638_);
            }
        }
    }

    protected float getStandingEyeHeight(Pose p_33614_, EntityDimensions p_33615_) {
        return 0.625F * p_33615_.height;
    }

    public boolean isDealsDamage() {
        return this.isEffectiveAi();
    }

    protected float getAttackDamage() {
        return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    protected SoundEvent getHurtSound(DamageSource p_33631_) {
        return this.isTiny() ? SoundEvents.SLIME_HURT_SMALL : SoundEvents.SLIME_HURT;
    }

    protected SoundEvent getDeathSound() {
        return this.isTiny() ? SoundEvents.SLIME_DEATH_SMALL : SoundEvents.SLIME_DEATH;
    }

    protected SoundEvent getSquishSound() {
        return this.isTiny() ? SoundEvents.SLIME_SQUISH_SMALL : SoundEvents.SLIME_SQUISH;
    }

    public float getSoundVolume() {
        return 0.4F * (float)this.getSize();
    }

    public int getMaxHeadXRot() {
        return 0;
    }

    public boolean doPlayJumpSound() {
        return this.getSize() > 0;
    }

    protected void jumpFromGround() {
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.x, (double)this.getJumpPower(), vec3.z);
        this.hasImpulse = true;
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_33601_, DifficultyInstance p_33602_, MobSpawnType p_33603_, @Nullable SpawnGroupData p_33604_, @Nullable CompoundTag p_33605_) {
        this.setSize(1, true);
        return super.finalizeSpawn(p_33601_, p_33602_, p_33603_, p_33604_, p_33605_);
    }

    public float getSoundPitch() {
        float f = this.isTiny() ? 1.4F : 0.8F;
        return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * f;
    }

    public SoundEvent getJumpSound() {
        return this.isTiny() ? SoundEvents.SLIME_JUMP_SMALL : SoundEvents.SLIME_JUMP;
    }

    public EntityDimensions getDimensions(Pose p_33597_) {
        return super.getDimensions(p_33597_).scale(0.255F * (float)this.getSize());
    }

    /**
     * Called when the slime spawns particles on landing, see onUpdate.
     * Return true to prevent the spawning of the default particles.
     */
    protected boolean spawnCustomParticles() { return false; }

    @Override
    protected void pickUpItem(ItemEntity p_21471_) {
        super.pickUpItem(p_21471_);
    }


    //    _______________________________________________________________________________________________________
    public boolean isBrave() {
        return brave;
    }

    public void setBrave(boolean brave) {
        this.brave = brave;
    }

    public SimpleContainer getInventory() {
        return container;
    }

    public void setContainer(SimpleContainer simpleContainer) {
        this.container = simpleContainer;
    }

    public boolean isHasProject() {
        return hasProject;
    }

    public void setHasProject(boolean hasProject) {
        this.hasProject = hasProject;
    }

    public void setPower(int power) {
        this.entityData.set(POWER,power);
    }

    public int getSize() {
        return this.entityData.get(ID_SIZE) ;
    }

    public int getPower(){
        return this.entityData.get(POWER);
    }

    public @Nullable BlockPos getBase() {
        return base;
    }

    public void setBase(@Nullable BlockPos base) {
        this.base = base;
    }

    @Override
    public int getExperience() {
        return this.entityData.get(EXP);
    }

    @Override
    public void addExperience(int value) {
        this.entityData.set(EXP,getExperience() + value);
    }

    public void grow(int n){
        int power = getPower() + n;
        setPower(power);
    }

    public boolean isInMucus(){
        return !firstTick && this.forgeFluidTypeHeight.getDouble(FluidRegister.Type.MUCUS.get()) > 0.0D;
    }

    public boolean wantCollectItem() {
        return wantCollectItem <= 0;
    }

    public void setWantCollectItem(int wantCollectItem) {
        this.wantCollectItem = wantCollectItem;
    }

    public void tryPickUp(ItemEntity itemEntity){
        ItemStack itemStack = container.addItem(itemEntity.getItem());
        if(itemStack.getCount() > 0){
            itemEntity.setItem(itemStack);
            this.playSound(SoundEvents.ALLAY_ITEM_GIVEN,1.0F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            return;
        }
        itemEntity.discard();
    }

    public boolean isMergable() {
        return this.getSize() < 4;
    }

    public boolean canShootAcidity(){
        return acidityPrepareTime <= 0;
    }

    public void tryToMerge(Slime p_32016_) {
        if(p_32016_.getSize() <= this.getSize()){
            this.grow(p_32016_.getSize());
            this.playSound(SoundEvents.BUCKET_EMPTY_FISH,1.0F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            p_32016_.discard();
        }
    }

    @Override
    public void dropExperience() {
        if(this.level instanceof ServerLevel serverLevel)
        IExperienceOrb.Award(serverLevel,this.position(),this.getExperience(),List.of(EntityRegister.SUSPICIOUS_SLIME.get()));
    }

    public void translateTick(){
        if(translateTick >= 0){
            translateTick --;
            return;
        }

        if(this.getSize() > 4) return;

        if(this.getExperience() > 100){
            int experience = this.getExperience();
            int growLevel = experience / 100;
            int exit = experience % 100;
            this.grow(growLevel);
            this.entityData.set(EXP,exit);
        }

        if(this.getPower() >= this.getSize() << this.getSize()){
            this.tranGrow();
        }
    }

    public void shootAcidity(){
        AcidityBall acidity = new AcidityBall(this.level,this);
        LivingEntity target = this.getTarget();
        this.getJumpControl().jump();
        Vec3 vec3=this.position();
        acidity.setPos(vec3.x,vec3.y+1.0D, vec3.z);
        this.level.addFreshEntity(acidity);
        acidity.setMoveLand(target.position().subtract(this.position()).scale(0.3));
        this.acidityPrepareTime = ACIDITY_PREPARE_TICK;
    }

    public SuspiciousSlime translate(int size1){
        Component component = this.getCustomName();
        SuspiciousSlime slime = EntityRegister.SUSPICIOUS_SLIME.get().create(this.level);
        if (this.isPersistenceRequired()) {
            slime.setPersistenceRequired();
        }
        slime.setContainer(this.container);
        slime.setCustomName(component);
        slime.setNoAi(this.isNoAi());
        slime.setInvulnerable(this.isInvulnerable());
        slime.setSize(size1, true);
        slime.addExperience(this.getExperience());
        return slime;
    }

    public void tranGrow(){
        if(this.getSize() > 2) return;
        int i = this.getSize() << 1;
        this.setSize(i,true);
    }

    public void lookAt(BlockPos p_21392_, float p_21393_, float p_21394_) {
        double d0 = p_21392_.getX() - this.getX();
        double d2 = p_21392_.getZ() - this.getZ();
        double d1 = p_21392_.getY() - this.getEyeY();

        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        float f = (float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
        float f1 = (float)(-(Mth.atan2(d1, d3) * (double)(180F / (float)Math.PI)));
        this.setXRot(this.rotlerp(this.getXRot(), f1, p_21394_));
        this.setYRot(this.rotlerp(this.getYRot(), f, p_21393_));
    }

    public float rotlerp(float p_21377_, float p_21378_, float p_21379_) {
        float f = Mth.wrapDegrees(p_21378_ - p_21377_);
        if (f > p_21379_) {
            f = p_21379_;
        }

        if (f < -p_21379_) {
            f = -p_21379_;
        }

        return p_21377_ + f;
    }

    public void tranTotalSmall(int size ,float f1,float f2){
        Slime slime = EntityType.SLIME.create(this.level);
        if (this.isPersistenceRequired()) {
            slime.setPersistenceRequired();
        }
        Component component = this.getCustomName();
        slime.setCustomName(component);
        slime.setNoAi(this.isNoAi());
        slime.setInvulnerable(this.isInvulnerable());
        slime.setSize(size, true);
        slime.moveTo(this.getX() + (double)f1, this.getY() + 0.5D, this.getZ() + (double)f2, this.random.nextFloat() * 360.0F, 0.0F);
        this.level.addFreshEntity(slime);
    }

    public void tranSmall(int size ,float f1,float f2){
        this.setSize(size,true);
        this.setPower(size);
        this.moveTo(this.getX() + (double)f1, this.getY() + 0.5D, this.getZ() + (double)f2, this.random.nextFloat() * 360.0F, 0.0F);
    }

    public void dropCarried(){
        dropExperience();
        if(this.container.isEmpty())return;
        Containers.dropContents(this.level,this,container);
    }

    public void saveItemToBase(){
        if(this.base == null || !(this.level.getBlockEntity(base) instanceof SusSlimeBase susSlimeBase)) return;
        List<ItemStack> list = this.container.removeAllItems();
        List<ItemStack> list1 = new ArrayList<>();

        list.forEach(itemStack -> {
            ItemStack itemStack1 = susSlimeBase.addItemStack(itemStack);
            if (!itemStack1.isEmpty()) list1.add(itemStack1);
        });

        susSlimeBase.setChanged();
        MinecraftForge.EVENT_BUS.post(new SusSlimeWantExpEvent(this,susSlimeBase));
        setWantCollectItem(COLLECT_TICK);
        if(list1.isEmpty()) return;
        list1.forEach(itemStack -> container.addItem(itemStack));
    }

    public List<BlockPos> locateSusSlimeBlock(){
        BlockPos blockpos = this.blockPosition();
        PoiManager poimanager = ((ServerLevel)this.level).getPoiManager();
        Stream<PoiRecord> stream = poimanager.getInRange((p_218130_) -> p_218130_.get() == PoiTypeRegister.SUS_SLIME_BLOCK.get(), blockpos, 20, PoiManager.Occupancy.ANY);
        return stream.map(PoiRecord::getPos).sorted(Comparator.comparingDouble((p_148811_) -> p_148811_.distSqr(blockpos))).collect(Collectors.toList());
    }

    public InteractionResult mobInteract(Player p_28298_, InteractionHand p_28299_) {
        ItemStack itemstack = p_28298_.getItemInHand(p_28299_);
        if(itemstack.is(Items.BUCKET) &&  this.getSize() == 4) {
            p_28298_.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            ItemStack itemstack1 = ItemUtils.createFilledResult(itemstack, p_28298_, ItemsRegister.ACIDITY_BUCKET.get().getDefaultInstance());
            p_28298_.setItemInHand(p_28299_, itemstack1);
            this.tranSmall(1,0,0);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }else if(itemstack.is(Items.WATER_BUCKET) && this.getSize() !=4){
            p_28298_.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            ItemStack itemstack1 = ItemUtils.createFilledResult(itemstack, p_28298_, this.getSize() !=1 ? ItemsRegister.MUCUS_BUCKET.get().getDefaultInstance() : ItemsRegister.SUSPICIOUS_WATER_BUCKET.get().getDefaultInstance());
            p_28298_.setItemInHand(p_28299_, itemstack1);
            this.dropCarried();
            this.discard();
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }else {
            return super.mobInteract(p_28298_, p_28299_);
        }
    }


    @Nullable
    public static  <T extends Entity> T getNearestEntity(List<? extends T> p_45983_, Entity entity){
        return getNearestEntity(p_45983_,entity.getX(),entity.getY(),entity.getZ());
    }

    @Nullable
    public static  <T extends Entity> T getNearestEntity(List<? extends T> p_45983_, double p_45986_, double p_45987_, double p_45988_) {
        double d0 = -1.0D;
        T t = null;

        for(T t1 : p_45983_) {
            double d1 = t1.distanceToSqr(p_45986_, p_45987_, p_45988_);
            if (d0 == -1.0D || d1 < d0) {
                d0 = d1;
                t = t1;
            }
        }
        return t;
    }

    public static boolean checkSusSlimeSpawnRules(EntityType<? extends LivingEntity> p_217018_, ServerLevelAccessor p_217019_, MobSpawnType p_217020_, BlockPos p_217021_, RandomSource p_217022_) {
        return p_217019_.getBlockState(p_217021_.below()).is(Blocks.CLAY);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader p_21433_) {
        return true;
    }

    public static boolean isContainerFull(Container container){
        return !container.hasAnyMatching(ItemStack::isEmpty);
    }

}
