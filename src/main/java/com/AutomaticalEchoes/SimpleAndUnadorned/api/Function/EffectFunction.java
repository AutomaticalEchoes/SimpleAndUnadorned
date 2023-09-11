package com.AutomaticalEchoes.SimpleAndUnadorned.api.Function;

import com.AutomaticalEchoes.SimpleAndUnadorned.config.ModCommonConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public interface EffectFunction {
     Predicate<LivingEntity> LIVING_ENTITY_SELECTOR = (p_31504_) -> p_31504_.getMobType() != MobType.UNDEFINED&& p_31504_.attackable();

     TargetingConditions targetingConditions=TargetingConditions
            .forCombat()
            .range(128.0D)
            .ignoreLineOfSight()
            .selector(LIVING_ENTITY_SELECTOR);
     Random random=new Random();


     static void Rage(LivingEntity p_19467_) {
        if(p_19467_.isAlive()){
            List<Monster> entities=p_19467_.level.getNearbyEntities(Monster.class,targetingConditions,p_19467_,p_19467_.getBoundingBox().inflate(128.0D));
            for (Monster monster:entities) {
                if(monster.getTarget() == null) monster.setTarget(p_19467_);
                monster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,2400,random.nextInt(2)+ ModCommonConfig.RAGE_TARGET_EFFECT_SPEEDUP_LEVEL.get() ));
            }
        }
    }

    static void applyEffect(LivingEntity livingEntity, Collection<MobEffectInstance> mobEffects){
         applyEffect(livingEntity,mobEffects,1.0D);
    }

    static void applyEffect(LivingEntity livingEntity, Collection<MobEffectInstance> mobEffects, double scale){
         applyEffect(livingEntity,mobEffects,scale,null);
    }

    static void applyEffect(LivingEntity livingEntity, Collection<MobEffectInstance> mobEffects, double scale, @Nullable Entity entity){
        for(MobEffectInstance mobeffectinstance : mobEffects) {
            if (mobeffectinstance.getEffect().isInstantenous()) {
                mobeffectinstance.getEffect().applyInstantenousEffect(null, null, livingEntity, mobeffectinstance.getAmplifier(), 1.0D);
            } else {
                livingEntity.addEffect(new MobEffectInstance(mobeffectinstance.getEffect(), (int) Math.max(mobeffectinstance.getDuration() * scale, 1), mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(), mobeffectinstance.isVisible()), entity);
            }
        }
    }

    static void CreateAreaEffectCloudOrInstantenous(ServerLevel serverLevel, Vec3 location, Collection<MobEffectInstance> mobEffectInstances, Entity entity){
        CreateAreaEffectCloudOrInstantenous(serverLevel, location, mobEffectInstances, entity, 2.0F);
    }

    static void CreateAreaEffectCloudOrInstantenous(ServerLevel serverLevel, Vec3 location, Collection<MobEffectInstance> mobEffectInstances, Entity entity, float radius){
        CreateAreaEffectCloudOrInstantenous(serverLevel, location, mobEffectInstances, entity, radius, 10);
    }

    static void CreateAreaEffectCloudOrInstantenous(ServerLevel serverLevel, Vec3 location, Collection<MobEffectInstance> mobEffectInstances, Entity entity, float radius , int waitTime ){
        CreateAreaEffectCloudOrInstantenous(serverLevel, location, mobEffectInstances, entity, radius, waitTime, 1.0F);
    }

    static void CreateAreaEffectCloudOrInstantenous(ServerLevel serverLevel, Vec3 location, Collection<MobEffectInstance> mobEffectInstances, Entity entity, float radius , int waitTime, double scale ) {
         CreateAreaEffectCloudOrInstantenous(serverLevel, location, mobEffectInstances, entity, radius, waitTime, scale,null);
    }

    static void CreateAreaEffectCloudOrInstantenous(ServerLevel serverLevel, Vec3 location, Collection<MobEffectInstance> mobEffectInstances, Entity entity, float radius , int waitTime, double scale ,@Nullable LivingEntity source){
        int i = 0;
        AreaEffectCloud areaEffectCloud = new AreaEffectCloud(serverLevel , location.x, location.y, location.z);
        areaEffectCloud.setRadius(radius);
        areaEffectCloud.setRadiusOnUse(-0.5F);
        areaEffectCloud.setWaitTime(waitTime);
        areaEffectCloud.setOwner(source);
        areaEffectCloud.setRadiusPerTick(-areaEffectCloud.getRadius() / (float)areaEffectCloud.getDuration());
        for(MobEffectInstance mobeffectinstance : mobEffectInstances) {
            if (mobeffectinstance.getEffect().isInstantenous()) {
                if(entity instanceof LivingEntity livingEntity){
                    mobeffectinstance.getEffect().applyInstantenousEffect(null, null, livingEntity, mobeffectinstance.getAmplifier(), 1.0D);
                }else {
                    entity.hurt(DamageSource.MAGIC,1);
                }
            } else {
                areaEffectCloud.addEffect(new MobEffectInstance(mobeffectinstance.getEffect(), (int) Math.max(mobeffectinstance.getDuration() * scale, 1), mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(), mobeffectinstance.isVisible()));
                i++;
            }
        }
        if(i > 0) serverLevel.addFreshEntity(areaEffectCloud);
    }
}
