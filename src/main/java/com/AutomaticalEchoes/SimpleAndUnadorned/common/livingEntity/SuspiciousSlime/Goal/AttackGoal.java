package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.Goal;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.SuspiciousSlime;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class AttackGoal extends Goal {
    private final SuspiciousSlime slime;
    private int growTiredTimer;

    public AttackGoal(SuspiciousSlime p_33648_) {
        this.slime = p_33648_;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    public boolean canUse() {
        LivingEntity livingentity = this.slime.getTarget();
        if (livingentity == null) {
            return false;
        } else {
            return this.slime.isBrave() && this.slime.canAttack(livingentity) && this.slime.getMoveControl() instanceof IMoveControl;
        }
    }

    public void start() {
        this.growTiredTimer = reducedTickDelay(300);
        super.start();
    }

    public boolean canContinueToUse() {
        LivingEntity livingentity = this.slime.getTarget();
        if (livingentity == null) {
            return false;
        } else if (!this.slime.canAttack(livingentity)) {
            return false;
        } else {
            return --this.growTiredTimer > 0;
        }
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }
    public void tick() {
        LivingEntity livingentity = this.slime.getTarget();
        if (livingentity != null) {
            this.slime.lookAt(livingentity, 10.0F, 10.0F);
        }
        ((IMoveControl)this.slime.getMoveControl()).setDirection(this.slime.getYRot(), this.slime.isDealsDamage(),false);

    }
}
