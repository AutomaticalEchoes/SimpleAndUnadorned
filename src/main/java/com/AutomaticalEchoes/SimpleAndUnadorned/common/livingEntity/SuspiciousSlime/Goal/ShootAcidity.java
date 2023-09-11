package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.Goal;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.SuspiciousSlime;
import com.AutomaticalEchoes.SimpleAndUnadorned.config.ModCommonConfig;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.material.Fluids;

public class ShootAcidity extends Goal {
    private final SuspiciousSlime suspiciousSlime;

    public ShootAcidity(SuspiciousSlime suspiciousSlime) {
        this.suspiciousSlime = suspiciousSlime;
    }

    @Override
    public boolean canUse() {
        return  !suspiciousSlime.isInFluidType(Fluids.WATER.getFluidType())
                && suspiciousSlime.getTarget()!=null
                && suspiciousSlime.isBrave()
                && suspiciousSlime.hasLineOfSight(suspiciousSlime.getTarget())
                && suspiciousSlime.distanceTo(suspiciousSlime.getTarget()) < ModCommonConfig.SUSPICIOUS_SLIME_ACIDITY_DISTANCE.get()
                && suspiciousSlime.canShootAcidity();
    }

    @Override
    public void start() {
      suspiciousSlime.shootAcidity();
    }
}
