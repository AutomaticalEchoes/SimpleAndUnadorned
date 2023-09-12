package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SusPillager.Goal;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SusPillager.SusPillager;
import net.minecraft.world.entity.ai.goal.Goal;

public class SusPillagerChargeBullets extends Goal {
    private final SusPillager susPillager;

    public SusPillagerChargeBullets(SusPillager susPillager) {
        this.susPillager = susPillager;
    }

    @Override
    public boolean canUse() {
        return susPillager.getTarget() == null && susPillager.getBullet() < 9 && susPillager.canCharge() && susPillager.getCrossbowState() != SusPillager.CrossbowState.CHARGING;
    }

    @Override
    public void start() {
        susPillager.StartChargeBullet();
    }
}
