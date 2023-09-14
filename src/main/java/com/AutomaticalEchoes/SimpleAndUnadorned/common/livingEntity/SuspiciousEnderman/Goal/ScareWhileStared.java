package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.Goal;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.SuspiciousEnderman;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class ScareWhileStared extends Goal {
    private final SuspiciousEnderman enderman;
    @Nullable
    private Player pendingTarget;
    private final TargetingConditions startAggroTargetConditions;

    public ScareWhileStared(SuspiciousEnderman p_32573_) {
        this.enderman = p_32573_;
        this.startAggroTargetConditions = TargetingConditions.forCombat().range(this.enderman.getAttributeValue(Attributes.FOLLOW_RANGE)).selector((p_32578_) -> p_32573_.isLookingAtMe((Player)p_32578_));
    }

    public boolean canUse() {
        this.pendingTarget = this.enderman.level.getNearestPlayer(this.startAggroTargetConditions, this.enderman);
        return this.pendingTarget != null;
    }

    @Override
    public void start() {
        if(!this.enderman.isSusBreak())this.enderman.susBreak(true);
        super.start();
    }

    public void stop() {
        this.enderman.beingClown();
        this.pendingTarget = null;
        this.enderman.goalSelector.enableControlFlag(Flag.MOVE);
    }

    public boolean canContinueToUse() {
        return this.pendingTarget != null && this.enderman.isLookingAtMe(this.pendingTarget);
    }

    public void tick() {
        this.enderman.goalSelector.disableControlFlag(Flag.MOVE);
        this.enderman.scareBeingStaredAt();
        this.enderman.getLookControl().setLookAt(pendingTarget);
    }
}
