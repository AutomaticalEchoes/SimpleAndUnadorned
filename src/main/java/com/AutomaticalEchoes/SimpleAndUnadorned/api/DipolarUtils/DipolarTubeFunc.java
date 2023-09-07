package com.AutomaticalEchoes.SimpleAndUnadorned.api.DipolarUtils;

import com.AutomaticalEchoes.SimpleAndUnadorned.api.IExplosion;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.projectile.DipolarTubeProjectile;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.EffectsRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.ItemsRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public interface DipolarTubeFunc {
    boolean shouldHitEntity(DipolarTubeProjectile projectile,Entity entity);
    void onHit(DipolarTubeProjectile dipolarTubeProjectile ,HitResult hitResult );
    void onHitEntity(DipolarTubeProjectile dipolarTubeProjectile, EntityHitResult entityHitResult );
    void onHitBlock(DipolarTubeProjectile dipolarTubeProjectile, BlockHitResult blockHitResult );
    void tick(DipolarTubeProjectile dipolarTubeProjectile ,Integer tickCount);

    static void Total(DipolarTubeProjectile dipolarTubeProjectile, EntityHitResult entityHitResult){
        if (dipolarTubeProjectile.level instanceof ServerLevel && entityHitResult.getEntity() instanceof LivingEntity livingEntity && !livingEntity.isBlocking()) {
            DipolarUtils.ApplyEffectToLivingEntity(dipolarTubeProjectile,livingEntity,0.25,dipolarTubeProjectile.getOwner());
            if(dipolarTubeProjectile.getPierceLevel() <= 0){
                dipolarTubeProjectile.discard();
            }
        }
    }

    static void Burst(DipolarTubeProjectile dipolarTubeProjectile, EntityHitResult entityHitResult){
        if (dipolarTubeProjectile.level instanceof ServerLevel serverLevel && entityHitResult.getEntity() instanceof LivingEntity livingEntity && !livingEntity.isBlocking()) {
            Entity owner = dipolarTubeProjectile.getOwner();
            boolean isLiving = owner instanceof LivingEntity ;
            DipolarUtils.CreateAreaEffectCloudOrInstantenous(serverLevel,livingEntity.position(), dipolarTubeProjectile.getItem(),livingEntity,1.0F * (1 + dipolarTubeProjectile.getPierceLevel()),isLiving ? (LivingEntity) owner : null);
            dipolarTubeProjectile.discard();
        }
    }

    static boolean Hide_ShouldHitEntity(DipolarTubeProjectile projectile,Entity target){
        return projectile.isPassenger() || projectile.isOnGround();
    }

    static void Hide(DipolarTubeProjectile dipolarTubeProjectile ,EntityHitResult entityHitResult){
        if (dipolarTubeProjectile.level instanceof ServerLevel serverLevel &&( dipolarTubeProjectile.isOnGround() || dipolarTubeProjectile.isPassenger() )) {
            Entity owner = dipolarTubeProjectile.getOwner();
            boolean isLiving = owner instanceof LivingEntity ;
            DipolarUtils.CreateAreaEffectCloudOrInstantenous(serverLevel,dipolarTubeProjectile.position(), dipolarTubeProjectile.getItem(),entityHitResult.getEntity(),2.0F,isLiving ? (LivingEntity) owner : null);
            if(dipolarTubeProjectile.isOnGround()){
                List<Entity> entities = serverLevel.getEntities(dipolarTubeProjectile, dipolarTubeProjectile.getBoundingBox().inflate(0.2F), dipolarTubeProjectile::canHitEntity);
                entities.forEach(entity -> entity.setDeltaMovement(entity.getEyePosition().subtract(dipolarTubeProjectile.position()).normalize().scale(0.2)));
            }
            if(dipolarTubeProjectile.getPierceLevel() <= 0) dipolarTubeProjectile.discard();
        }
    }

    static void PASTE(DipolarTubeProjectile dipolarTubeProjectile, EntityHitResult entityHitResult ){
        if (dipolarTubeProjectile.level instanceof ServerLevel serverLevel) {
            if(entityHitResult.getEntity() instanceof LivingEntity livingEntity && livingEntity.isBlocking())return;
             dipolarTubeProjectile.startRiding(entityHitResult.getEntity());
        }
    }

    static void SWASH(DipolarTubeProjectile dipolarTubeProjectile,Integer tickCount){
        if(dipolarTubeProjectile.level.isClientSide) return;
        if(!dipolarTubeProjectile.hit) {
            dipolarTubeProjectile.tickCount = 0;
        }else if(tickCount > 20 * 5 / (1 + dipolarTubeProjectile.getPierceLevel())){
            DipolarUtils.Explode(dipolarTubeProjectile);
            dipolarTubeProjectile.discard();
        }
    }
}
