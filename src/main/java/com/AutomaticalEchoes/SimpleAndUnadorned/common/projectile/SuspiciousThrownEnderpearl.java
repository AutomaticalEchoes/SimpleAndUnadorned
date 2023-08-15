package com.AutomaticalEchoes.SimpleAndUnadorned.common.projectile;

import com.AutomaticalEchoes.SimpleAndUnadorned.register.EntityRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.ItemsRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class SuspiciousThrownEnderpearl extends ThrowableItemProjectile{
    private boolean back = false;

    public static @NotNull SuspiciousThrownEnderpearl Create(EntityType<? extends SuspiciousThrownEnderpearl> p_37491_, Level p_37492_) {
        return new SuspiciousThrownEnderpearl(EntityRegister.SUSPICIOUS_THROWN_ENDERPEARL_PROJECTILE.get(), p_37492_);
    }

    public SuspiciousThrownEnderpearl(EntityType<? extends SuspiciousThrownEnderpearl> p_37491_, Level p_37492_) {
        super(p_37491_, p_37492_);
    }

    public SuspiciousThrownEnderpearl(Level p_37499_, LivingEntity p_37500_) {
        super(EntityRegister.SUSPICIOUS_THROWN_ENDERPEARL_PROJECTILE.get(), p_37500_, p_37499_);
    }

    protected Item getDefaultItem() {
        return ItemsRegister.SUSPICIOUS_THROWN_ENDERPEARL_ITEM.get();
    }

    protected void onHitEntity(EntityHitResult p_37502_) {
        super.onHitEntity(p_37502_);
        p_37502_.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), 0.0F);
    }

    protected void onHit(HitResult p_37504_) {
        if (!this.level.isClientSide && !this.isRemoved()) {
            if(back) return;
            super.onHit(p_37504_);
            this.setNoGravity(true);
            Entity owner = this.getOwner();
            if(owner == null ){
                this.discard();
                return;
            }


            if(p_37504_ instanceof EntityHitResult entityHitResult){
                Entity target =entityHitResult.getEntity();
                Vec3 vec3 = owner.position();
                target.teleportTo(vec3.x,vec3.y,vec3.z);
            }

            if( p_37504_ instanceof BlockHitResult blockHitResult){
                this.setDeltaMovement(Vec3.ZERO);
                BlockPos blockPos = blockHitResult.getBlockPos();
                Direction direction = blockHitResult.getDirection();
                Vec3 pos = new Vec3(blockPos.getX(),blockPos.getY(),blockPos.getZ()).add(Vec3.atCenterOf( direction.getNormal()));
                this.setPos(pos);
            }
        }
    }

    @Override
    public void playerTouch(Player p_20081_) {
        if(!p_20081_.level.isClientSide() && back){
            p_20081_.getInventory().add(getPickResult());
        }
    }

    public void tick() {
        super.tick();
        Entity entity = this.getOwner();
        if(entity!=null && !back &&(this.getDeltaMovement().length() ==0 ||this.distanceTo(entity)>32)){
            back=true;
            this.setNoGravity(true);
        }
        if (entity instanceof Player player && back) {
            Vec3 vec3 = player.position().subtract(this.position()).scale(0.01);
            this.setDeltaMovement(this.getDeltaMovement().add(vec3));
        }
    }

    @Override
    public ItemStack getPickResult() {
        this.discard();
        return new ItemStack(ItemsRegister.SUSPICIOUS_THROWN_ENDERPEARL_ITEM.get());
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public float getPickRadius() {
        return 0.5F;
    }


    @Nullable
    public Entity changeDimension(ServerLevel p_37506_, net.minecraftforge.common.util.ITeleporter teleporter) {
        Entity entity = this.getOwner();
        if (entity != null && entity.level.dimension() != p_37506_.dimension()) {
            this.setOwner((Entity)null);
        }

        return super.changeDimension(p_37506_, teleporter);
    }

}
