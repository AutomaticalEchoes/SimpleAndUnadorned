package com.AutomaticalEchoes.SimpleAndUnadorned.common.effect;

import com.AutomaticalEchoes.SimpleAndUnadorned.api.Function.EffectFunction;
import com.AutomaticalEchoes.SimpleAndUnadorned.config.ModCommonConfig;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;


public class RageTargetEffect extends MobEffect {
    public RageTargetEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        EffectFunction.applyRageEffectTick(p_19467_);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
       return p_19455_ % ( ModCommonConfig.RAGE_TARGET_EFFECT_DURATION_TICK.get()*20) ==0;
    }
}
