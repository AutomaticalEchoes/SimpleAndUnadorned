package com.AutomaticalEchoes.SimpleAndUnadorned.common.effect;

import com.AutomaticalEchoes.SimpleAndUnadorned.api.Function.FluidFunction;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;


public class AcidErosion extends MobEffect {
    public AcidErosion(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        if(p_19467_ instanceof Player player){
            FluidFunction.HurtArmor(player);
        }
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return p_19455_ % 20 ==0;
    }
}
