package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.Goal;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.blockEntity.SusSlimeBase.SusSlimeBase;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.SuspiciousSlime;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

public class WantSaveItem extends ProjectGoal {
    private BlockPos blockPos;
    public WantSaveItem(SuspiciousSlime suspiciousSlime){
        super(suspiciousSlime);
    }

    @Override
    public boolean canUse() {
        if(SuspiciousSlime.isContainerFull(slime.getInventory())){
            if (slime.getBase() == null) {
                slime.locateSusSlimeBlock.start();
                return false;
            }
            this.blockPos = slime.getBase();
            return !slime.level.isClientSide && slime.level.getBlockEntity(blockPos) instanceof SusSlimeBase base && base.hasAnyMatching(ItemStack::isEmpty);
        }
        return false;
    }

    @Override
    public void tick() {
        if(slime.blockPosition().distToCenterSqr(blockPos.getX(),blockPos.getY(),blockPos.getZ()) < 1) {
            slime.saveItemToBase();
            stop();
        }
        this.slime.lookAt(blockPos,10.0F,10.0F);
        ((IMoveControl)this.slime.getMoveControl()).setDirection(this.slime.getYRot(), true,false);
    }
}