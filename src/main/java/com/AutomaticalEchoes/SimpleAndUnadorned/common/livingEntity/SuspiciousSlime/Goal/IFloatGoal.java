package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.Goal;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.SuspiciousSlime;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class IFloatGoal extends Goal {
    private final SuspiciousSlime slime;
    public IFloatGoal(SuspiciousSlime p_33655_) {
        this.slime = p_33655_;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        p_33655_.getNavigation().setCanFloat(true);
    }

    public boolean canUse() {
        return (this.slime.isInWater() || this.slime.isInLava() || this.slime.isInMucus()) && this.slime.getMoveControl() instanceof IMoveControl;
    }

    public boolean requiresUpdateEveryTick() {
            return true;
    }
    public void tick() {
        if (this.slime.getRandom().nextFloat() < 0.1F * slime.getSize()) {
            this.slime.getJumpControl().jump();
        }
        ((IMoveControl)this.slime.getMoveControl()).setWantedMovement(1.2D);
    }
}
