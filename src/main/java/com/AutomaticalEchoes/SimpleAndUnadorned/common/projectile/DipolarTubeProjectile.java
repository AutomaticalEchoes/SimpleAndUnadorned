package com.AutomaticalEchoes.SimpleAndUnadorned.common.projectile;

import com.AutomaticalEchoes.SimpleAndUnadorned.api.DipolarUtils.DipolarTubeFunc;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.EntityRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.ItemsRegister;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
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
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class DipolarTubeProjectile extends AbstractArrow implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(DipolarTubeProjectile.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Boolean> TURN = SynchedEntityData.defineId(DipolarTubeProjectile.class,EntityDataSerializers.BOOLEAN);
    private final ArrayList<DipolarTubeFunc> dipolarTubeFuncArrayList = new ArrayList<>();
    private boolean turn;
    public  int tickCount = 0;
    public boolean hit = false;
    @Nullable
    private IntOpenHashSet piercingIgnoreEntityIds;

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
        hit = true;
        for(DipolarTubeFunc dipolarTubeFunc : dipolarTubeFuncArrayList){
            dipolarTubeFunc.onHit(this,p_37260_);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult p_37258_) {
        BlockState blockstate = this.level.getBlockState(p_37258_.getBlockPos());
        blockstate.onProjectileHit(this.level, blockstate, p_37258_, this);

        Vec3 vec3 = p_37258_.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vec3);
        Vec3 vec31 = vec3.normalize().scale((double)0.05F);
        this.setPosRaw(this.getX() - vec31.x, this.getY() - vec31.y, this.getZ() - vec31.z);
        this.inGround = true;
        this.shakeTime = 7;
        this.setSoundEvent(SoundEvents.GLASS_HIT);
        this.playSound(this.getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.setShotFromCrossbow(false);

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
        if (entity.getType() == EntityType.ENDERMAN && !isOnGround()) {
            return;
        }
        if (this.piercingIgnoreEntityIds == null) {
            this.piercingIgnoreEntityIds = new IntOpenHashSet(5);
        }
        SoundEvent soundevent = SoundEvents.GLASS_BREAK;
        float f1 = 1.0F;
        this.piercingIgnoreEntityIds.add(entity.getId());
        this.playSound(soundevent, f1, 1.0F);
        preHitEntity(p_37259_);
        if (this.getPierceLevel() > 0) {
            this.setPierceLevel((byte) (this.getPierceLevel() - 1));
        }
        setTurn(!isPassenger());
    }

    @Override
    public void tick() {
        super.tick();

        if(this.level.isClientSide && Minecraft.getInstance().player.getId() == this.getOwner().getId()){
            if(isTurn()){
                this.level.addParticle(ParticleTypes.GLOW,this.getX(),this.getY() + 0.3 * random.nextInt(-1,1),this.getZ(),0,0,0);
            }else if(tickCount % 5 == 0){
                this.level.addParticle(ParticleTypes.GLOW,this.getX() + 0.1 * random.nextInt(-1,1),this.getY() ,this.getZ() + 0.1 * random.nextInt(-1,1),0,10,0);
            }
        }

        if(!this.isAlive()) return;
        tickCount ++ ;
        for(DipolarTubeFunc dipolarTubeFunc : dipolarTubeFuncArrayList){
            dipolarTubeFunc.tick(this, tickCount);
        }
        findHitEntityOnGround();
    }

    @Override
    public void onRemovedFromWorld() {
        if(this.level.isClientSide) {
            for (int i = 0; i < 20; i++) {
                this.level.addParticle(ParticleTypes.INSTANT_EFFECT,this.getX() + 0.5 * random.nextInt(-1,1),this.getY() + 0.5 * random.nextInt(-1,1),this.getZ() + 0.5 * random.nextInt(-1,1),0,0,0);
            }
        }
        super.onRemovedFromWorld();
    }

    //_____________________________________________________________________________________________________________________________

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
        for (DipolarTubeFunc func : dipolarTubeFuncArrayList) {
            if(!func.shouldHitEntity(this,entity)) return false;
        }
        return super.canHitEntity(entity)  && (this.piercingIgnoreEntityIds == null || !this.piercingIgnoreEntityIds.contains(entity.getId()));
    }

    public void findHitEntityOnGround(){
        if((this.isOnGround() || this.isPassenger()) && level instanceof ServerLevel serverLevel) {
            float inflate = this.isPassenger() ? this.getVehicle().getBbWidth() / 2 : 0.2F;
            List<Entity> entities = serverLevel.getEntities(this, this.getBoundingBox().inflate(inflate), this::canHitEntity);
            if (entities.isEmpty()) return;
            EntityHitResult hitresult = new EntityHitResult(entities.get(0));
            Entity entity = hitresult.getEntity();
            Entity entity1 = this.getOwner();
            if (entity instanceof Player && entity1 instanceof Player && !((Player) entity1).canHarmPlayer((Player) entity)) {
                hitresult = null;
            }

            if (hitresult !=null && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
                this.hasImpulse = true;
            }
        }
    }

}
