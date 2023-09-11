package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.Goal;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.SuspiciousSlime;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class RandomDirectionGoal extends Goal {
    private final SuspiciousSlime slime;
    private float chosenDegrees;
    private int nextRandomizeTime;

    public RandomDirectionGoal(SuspiciousSlime p_33679_) {
        this.slime = p_33679_;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    public boolean canUse() {
        return !this.slime.isBrave() && this.slime.getTarget() == null && !this.slime.isHasProject() && (this.slime.isOnGround() || this.slime.isInWater() || this.slime.isInLava() || this.slime.hasEffect(MobEffects.LEVITATION)) && this.slime.getMoveControl() instanceof IMoveControl;
    }

    public void tick() {
        if (--this.nextRandomizeTime <= 0) {
            this.nextRandomizeTime = this.adjustedTickDelay(40 + this.slime.getRandom().nextInt(60));
            this.chosenDegrees = (float)this.slime.getRandom().nextInt(360);
        }

        ((IMoveControl)this.slime.getMoveControl()).setDirection(this.chosenDegrees, false,false);
    }
}
