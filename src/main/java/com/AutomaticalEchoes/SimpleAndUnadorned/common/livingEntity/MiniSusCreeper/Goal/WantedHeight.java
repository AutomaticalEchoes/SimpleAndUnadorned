package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.MiniSusCreeper.Goal;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.MiniSusCreeper.MiniSusCreeper;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class WantedHeight extends RandomStrollGoal {
    private final MiniSusCreeper miniSusCreeper;
    public WantedHeight(MiniSusCreeper p_25734_, double p_25735_) {
        super(p_25734_, p_25735_);
        miniSusCreeper = p_25734_;
    }

    @Override
    public boolean canUse() {
        return miniSusCreeper.isOnGround() && !miniSusCreeper.isLocked() && miniSusCreeper.getAttachFace() != Direction.UP && super.canUse();
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        return miniSusCreeper.getWantedPosition();
    }
}
