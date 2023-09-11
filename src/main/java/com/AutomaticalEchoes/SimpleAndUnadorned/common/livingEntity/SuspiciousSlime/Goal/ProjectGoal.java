package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.Goal;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.SuspiciousSlime;
import net.minecraft.world.entity.ai.goal.Goal;

public abstract class ProjectGoal extends Goal {
    protected final SuspiciousSlime slime;

    protected ProjectGoal(SuspiciousSlime suspiciousSlime) {
       this.slime = suspiciousSlime;
    }

    @Override
    public void start() {
        slime.setHasProject(true);
    }

    @Override
    public void stop() {
        slime.setHasProject(false);
    }
}
