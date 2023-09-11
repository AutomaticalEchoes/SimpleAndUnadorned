package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.Goal;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.SuspiciousEnderman;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class TeleportWhileHasSight extends Goal {
    private final SuspiciousEnderman enderman;
    @Nullable
    private Player pendingTarget;
    private final TargetingConditions startAggroTargetConditions;

    public TeleportWhileHasSight(SuspiciousEnderman enderman) {
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.enderman = enderman;
        this.startAggroTargetConditions = TargetingConditions.forCombat().range(this.enderman.getAttributeValue(Attributes.FOLLOW_RANGE)).selector((p_32578_) -> {
            return p_32578_.hasLineOfSight(enderman);
        });
    }

    @Override
    public boolean canUse() {
        pendingTarget=enderman.level.getNearestPlayer(this.startAggroTargetConditions,this.enderman);
        return !enderman.isScared() && pendingTarget !=null;
    }

    @Override
    public void start() {
        this.enderman.teleport();
    }
}
