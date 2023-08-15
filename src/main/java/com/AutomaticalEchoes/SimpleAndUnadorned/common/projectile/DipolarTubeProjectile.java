package com.AutomaticalEchoes.SimpleAndUnadorned.common.projectile;

import com.AutomaticalEchoes.SimpleAndUnadorned.api.DipolarUtils.DipolarTubeFunc;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.EntityRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.ItemsRegister;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;


public class DipolarTubeProjectile extends AbstractArrow implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(DipolarTubeProjectile.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Boolean> TURN = SynchedEntityData.defineId(DipolarTubeProjectile.class,EntityDataSerializers.BOOLEAN);
    private int tickCount = 0;
    private final ArrayList<DipolarTubeFunc> dipolarTubeFuncArrayList = new ArrayList<>();
    private boolean isHit,turn;

    public static DipolarTubeProjectile Create(EntityType<? extends DipolarTubeProjectile> p_37442_, Level p_37443_){
        return new DipolarTubeProjectile(p_37443_);
    }

    public DipolarTubeProjectile(Level p_37443_) {
        super(EntityRegister.DIPOLAR_TUBE_PROJECTILE.get(), p_37443_);
    }

    public DipolarTubeProjectile(double p_37433_, double p_37434_, double p_37435_, Level p_37436_) {
        super(EntityRegister.DIPOLAR_TUBE_PROJECTILE.get(), p_37433_, p_37434_, p_37435_, p_37436_);
    }

    public DipolarTubeProjectile(LivingEntity p_37439_, Level p_37440_) {
        super(EntityRegister.DIPOLAR_TUBE_PROJECTILE.get(), p_37439_, p_37440_);
    }

    public DipolarTubeProjectile(LivingEntity p_37439_, Level p_37440_,ItemStack itemStack) {
        super(EntityRegister.DIPOLAR_TUBE_PROJECTILE.get(), p_37439_, p_37440_);
        this.setItem(itemStack);
    }

    public DipolarTubeProjectile AddFunc(DipolarTubeFunc func){
       if(this.dipolarTubeFuncArrayList.size() <= 2) this.dipolarTubeFuncArrayList.add(func);
       return this;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_ITEM_STACK, ItemStack.EMPTY);
        this.getEntityData().define(TURN,turn);
    }

    public void addAdditionalSaveData(CompoundTag p_37449_) {
        super.addAdditionalSaveData(p_37449_);
        ItemStack itemstack = this.getItemRaw();
        if (!itemstack.isEmpty()) {
            p_37449_.put("Item", itemstack.save(new CompoundTag()));
        }
        p_37449_.putBoolean("Turn",turn);
    }

    public void readAdditionalSaveData(CompoundTag p_37445_) {
        super.readAdditionalSaveData(p_37445_);
        ItemStack itemstack = ItemStack.of(p_37445_.getCompound("Item"));
        this.turn = p_37445_.getBoolean("Turn");
        this.setItem(itemstack);
    }

    @Override
    protected void onHit(HitResult p_37260_) {
        super.onHit(p_37260_);
        for(DipolarTubeFunc dipolarTubeFunc : dipolarTubeFuncArrayList){
            dipolarTubeFunc.onHit(this,p_37260_);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);
        setOnGround(true);
        setTurn(false);
        for(DipolarTubeFunc dipolarTubeFunc : dipolarTubeFuncArrayList){
            dipolarTubeFunc.onHitBlock(this,p_37258_);
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return getItem();
    }

    @Override
    protected void onHitEntity(EntityHitResult p_37259_) {
        Entity entity = p_37259_.getEntity();
        if (entity.getType() == EntityType.ENDERMAN) {
            return;
        }
        if (this.getPierceLevel() > 0) {
            this.setPierceLevel((byte) (this.getPierceLevel() - 1));
        }else {
            this.isHit = true;
            SoundEvent soundevent = SoundEvents.GLASS_BREAK;
            float f1 = 1.0F;
            this.playSound(soundevent, f1, 1.0F);
        }

        preHitEntity(p_37259_);
    }

    @Override
    public void tick() {
        super.tick();
        if(isTurn() && this.level.isClientSide && Minecraft.getInstance().player.getId() == this.getOwner().getId()){
            for (int i = 0; i < 3; i++) {
                this.level.addParticle(ParticleTypes.GLOW,this.getX(),this.getY() + 0.3 * random.nextInt(-1,1),this.getZ(),0,0,0);
            }

        }
        if(this.isAlive()) tickCount ++ ;
        for(DipolarTubeFunc dipolarTubeFunc : dipolarTubeFuncArrayList){
            dipolarTubeFunc.tick(this, tickCount);
        }
    }


//_____________________________________________________________________________________________________________________________
    public static void CreateAreaEffectCloud(ServerLevel serverLevel,Vec3 location, ItemStack itemStack){
        AreaEffectCloud areaEffectCloud = new AreaEffectCloud(serverLevel , location.x, location.y, location.z);
        areaEffectCloud.setRadius(2.0F);
        areaEffectCloud.setRadiusOnUse(-0.5F);
        areaEffectCloud.setWaitTime(10);
        areaEffectCloud.setRadiusPerTick(-areaEffectCloud.getRadius() / (float)areaEffectCloud.getDuration());
        for(MobEffectInstance mobeffectinstance : PotionUtils.getMobEffects(itemStack)) {
            areaEffectCloud.addEffect(new MobEffectInstance(mobeffectinstance));
        }
        serverLevel.addFreshEntity(areaEffectCloud);
    }

    public static void ApplyEffectToLivingEntity( DipolarTubeProjectile dipolarTubeProjectile,LivingEntity livingEntity ){
        for(MobEffectInstance mobeffectinstance : PotionUtils.getMobEffects(dipolarTubeProjectile.getItem())) {
            if (mobeffectinstance.getEffect().isInstantenous()) {
                mobeffectinstance.getEffect().applyInstantenousEffect(null, null, livingEntity, mobeffectinstance.getAmplifier(), 1.0D);
            } else {
                livingEntity.addEffect(new MobEffectInstance(mobeffectinstance));
            }
        }
    }

    protected void preHitEntity(EntityHitResult p_36757_){
        for(DipolarTubeFunc dipolarTubeFunc : dipolarTubeFuncArrayList){
            dipolarTubeFunc.onHitEntity(this,p_36757_);
        }
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
        this.entityData.set(TURN,turn);
    }

    public boolean isTurn() {
        return entityData.get(TURN);
    }

    public int TickCount() {
        return tickCount;
    }

    public void setItem(ItemStack p_37447_) {
        if (!p_37447_.is(this.getDefaultItem()) || p_37447_.hasTag()) {
            this.getEntityData().set(DATA_ITEM_STACK, Util.make(p_37447_.copy(), (p_37451_) -> {
                p_37451_.setCount(1);
            }));
        }
    }

    protected Item getDefaultItem(){
       return ItemsRegister.DIPOLAR_TUBE_ITEM.get();
    }

    protected ItemStack getItemRaw() {
        return this.getEntityData().get(DATA_ITEM_STACK);
    }

    public ItemStack getItem() {
        ItemStack itemstack = this.getItemRaw();
        return itemstack.isEmpty() ? new ItemStack(this.getDefaultItem()) : itemstack;
    }

    public boolean canHitEntity(Entity entity){
        return super.canHitEntity(entity);
    }

}
