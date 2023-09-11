package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.Goal;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.SuspiciousSlime;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.BlockRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;

public class LocateSusSlimeBlock extends ProjectGoal {
    public LocateSusSlimeBlock(SuspiciousSlime suspiciousSlime){
        super(suspiciousSlime);
    }

    @Override
    public boolean canUse() {
        return !slime.level.isClientSide && (slime.getBase() == null || slime.level.getBlockState(slime.getBase()).getBlock() != BlockRegister.SUSPICIOUS_SLIME_BLOCK.get());
    }

    @Override
    public void start() {
        List<BlockPos> blockPos = slime.locateSusSlimeBlock();
        slime.setBase(blockPos.isEmpty()? null :blockPos.get(0));
    }
}
