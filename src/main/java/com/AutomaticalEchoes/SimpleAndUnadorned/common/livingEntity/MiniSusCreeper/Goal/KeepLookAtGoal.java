package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.MiniSusCreeper.Goal;

import com.AutomaticalEchoes.SimpleAndUnadorned.api.Function.EffectFunction;
import com.AutomaticalEchoes.SimpleAndUnadorned.api.Function.IFunction;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.MiniSusCreeper.MiniSusCreeper;
import com.AutomaticalEchoes.SimpleAndUnadorned.config.ModCommonConfig;
import net.minecraft.world.entity.ai.goal.Goal;

public class KeepLookAtGoal extends Goal {
    public static Integer WARNING_TICK = 20 * ModCommonConfig.WARNING_TICK.get();
    private final MiniSusCreeper miniSusCreeper;
    private int tick = 0;
    public KeepLookAtGoal(MiniSusCreeper miniSusCreeper){
        this.miniSusCreeper = miniSusCreeper;
    }

    @Override
    public boolean canUse() {
        return miniSusCreeper.getTarget() != null && miniSusCreeper.hasLineOfSight(miniSusCreeper.getTarget());
    }

    @Override
    public void start() {
        miniSusCreeper.setLocked(true);
    }

    @Override
    public void tick() {
        tick ++;
        miniSusCreeper.getLookControl().setLookAt(miniSusCreeper.getTarget(),30.0F,30.0F);
        if(tick > WARNING_TICK){
            EffectFunction.Rage(miniSusCreeper.getTarget());
        }
    }

    @Override
    public void stop() {
        tick =0;
        miniSusCreeper.setLocked(false);
        miniSusCreeper.setTarget(null);
    }
}
