package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousCreeper.Goals;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousCreeper.SuspiciousCreeper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import javax.annotation.Nullable;

public class SwellGoal extends Goal {
    private final SuspiciousCreeper creeper;
    @Nullable
    private LivingEntity target;

    public SwellGoal(SuspiciousCreeper creeper) {
        this.creeper = creeper;
    }

    public boolean canUse() {
        LivingEntity livingentity = this.creeper.getTarget();
        return this.creeper.getSwellDir() > 0 || livingentity != null && this.creeper.distanceToSqr(livingentity) < this.creeper.getExplosionRadius();
    }

    public void start() {
        this.creeper.getNavigation().stop();
        this.creeper.ignite();
        this.target = this.creeper.getTarget();
    }

    public void stop() {
        this.target = null;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        if (this.target == null) {
            this.creeper.setSwellDir(-1);
        } else if (this.creeper.distanceToSqr(this.target) > 49.0D) {
            this.creeper.setSwellDir(-1);
        } else if (!this.creeper.getSensing().hasLineOfSight(this.target)) {
            this.creeper.setSwellDir(-1);
        } else {
            this.creeper.setSwellDir(1);
        }
    }
}
