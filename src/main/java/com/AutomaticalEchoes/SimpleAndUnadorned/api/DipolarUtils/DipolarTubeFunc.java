package com.AutomaticalEchoes.SimpleAndUnadorned.api.DipolarUtils;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.projectile.DipolarTubeProjectile;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.ItemsRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public interface DipolarTubeFunc {
    boolean CanUse(DipolarTubeProjectile projectile);
    void onHit(DipolarTubeProjectile dipolarTubeProjectile ,HitResult hitResult );
    void onHitEntity(DipolarTubeProjectile dipolarTubeProjectile, EntityHitResult entityHitResult );
    void onHitBlock(DipolarTubeProjectile dipolarTubeProjectile, BlockHitResult blockHitResult );
    void tick(DipolarTubeProjectile dipolarTubeProjectile ,Integer tickCount);

    static void ApplyEffectOnHitLivingEntity(DipolarTubeProjectile dipolarTubeProjectile, EntityHitResult entityHitResult  ){
        if (dipolarTubeProjectile.level instanceof ServerLevel serverLevel && entityHitResult.getEntity() instanceof LivingEntity livingEntity && !livingEntity.isBlocking()) {
            for(MobEffectInstance mobeffectinstance : PotionUtils.getMobEffects(dipolarTubeProjectile.getItem())) {
                if (mobeffectinstance.getEffect().isInstantenous()) {
                    mobeffectinstance.getEffect().applyInstantenousEffect(null, null, livingEntity, mobeffectinstance.getAmplifier(), 1.0D);
                } else {
                    livingEntity.addEffect(new MobEffectInstance(mobeffectinstance));
                }
            }
            dipolarTubeProjectile.discard();
        }
    }

    static void CreateAreaEffectCloudOnHit(DipolarTubeProjectile dipolarTubeProjectile, EntityHitResult entityHitResult){
        if (dipolarTubeProjectile.level instanceof ServerLevel serverLevel && entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
            DipolarTubeProjectile.CreateAreaEffectCloud(serverLevel,dipolarTubeProjectile.position(), dipolarTubeProjectile.getItem());
            if(dipolarTubeProjectile.getPierceLevel() <= 0) dipolarTubeProjectile.discard();
        }
    }

    static void CreateAreaEffectCloudTick(DipolarTubeProjectile dipolarTubeProjectile ,Integer tickCount ){
        if (dipolarTubeProjectile.level instanceof ServerLevel serverLevel && dipolarTubeProjectile.isOnGround()) {
            List<Entity> entities = serverLevel.getEntities(dipolarTubeProjectile, dipolarTubeProjectile.getBoundingBox().inflate(0.2F), dipolarTubeProjectile::canHitEntity);
            if(entities.size() != 0){
                DipolarTubeProjectile.CreateAreaEffectCloud(serverLevel,dipolarTubeProjectile.position(), dipolarTubeProjectile.getItem());
                dipolarTubeProjectile.playSound(SoundEvents.GLASS_BREAK);
                for (Entity entity : entities) {
                    entity.setDeltaMovement(entity.getEyePosition().subtract(dipolarTubeProjectile.position()).normalize().scale(0.2));
                }
                if(dipolarTubeProjectile.getPierceLevel() <= 0) dipolarTubeProjectile.discard();
            }
        }
    }
}