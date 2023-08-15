package com.AutomaticalEchoes.SimpleAndUnadorned.api.Function;

import com.AutomaticalEchoes.SimpleAndUnadorned.config.ModCommonConfig;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;

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


     static void applyRageEffectTick(LivingEntity p_19467_) {
        if(p_19467_.isAlive()){
            List<Monster> entities=p_19467_.level.getNearbyEntities(Monster.class,targetingConditions,p_19467_,p_19467_.getBoundingBox().inflate(128.0D));
            for (Monster monster:entities) {
                if(monster.getTarget() == null) monster.setTarget(p_19467_);
                monster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,2400,random.nextInt(2)+ ModCommonConfig.RAGE_TARGET_EFFECT_SPEEDUP_LEVEL.get() ));
            }
        }
    }
}
