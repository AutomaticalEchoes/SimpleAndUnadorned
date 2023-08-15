package com.AutomaticalEchoes.SimpleAndUnadorned.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class BaseEffect extends MobEffect {
    public static MobEffect Create(MobEffectCategory p_19451_, int p_19452_){
        return new BaseEffect(p_19451_,p_19452_);
    }
    protected BaseEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

}
