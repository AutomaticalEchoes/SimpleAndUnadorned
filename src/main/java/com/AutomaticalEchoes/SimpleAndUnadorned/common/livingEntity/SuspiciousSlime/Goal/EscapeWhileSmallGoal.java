package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.Goal;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.SuspiciousSlime;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;


public class EscapeWhileSmallGoal extends Goal{
    private final SuspiciousSlime slime;

    public EscapeWhileSmallGoal(SuspiciousSlime p_33660_) {
        this.slime = p_33660_;
    }

    public boolean canUse() {
        return this.slime.getSize() < 4
                && !slime.isBrave()
                && !slime.isPassenger()
                && slime.getTarget() instanceof Player
                && slime.getTarget().hasLineOfSight(slime);
    }

    @Override
    public void start() {
        this.slime.getNavigation().stop();
    }

    public void tick() {
        float rot = this.slime.getTarget().getYRot();
        if(((IMoveControl)slime.getMoveControl()).getJumpDelay() <= 0)
        ((IMoveControl)slime.getMoveControl()).setDirection(rot + slime.getRandom().nextInt(-60,60),false,true);
    }
}
