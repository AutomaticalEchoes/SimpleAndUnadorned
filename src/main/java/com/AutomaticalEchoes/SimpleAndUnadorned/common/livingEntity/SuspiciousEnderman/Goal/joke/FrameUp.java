package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.Goal.joke;

import com.AutomaticalEchoes.SimpleAndUnadorned.api.joke.Joke;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.SuspiciousEnderman;
import com.AutomaticalEchoes.SimpleAndUnadorned.config.ModCommonConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class FrameUp extends Joke<Player> {
    public FrameUp(SuspiciousEnderman suspiciousEnderman) {
        super(suspiciousEnderman);
    }

    @Override
    public Case Case() {
        return Case.CARRIED;
    }

    @Override
    public boolean canJoke() {
        return super.canJoke() && ModCommonConfig.ENABLE_JOKE_FRAME_UP.get();
    }

    @Override
    public void doJoke() {
        if(suspiciousEnderman.getJokingTarget() instanceof Player target){
            Inventory playerInventory = target.getInventory();
            ItemStack itemStack = suspiciousEnderman.getCarriedItem();
            int freeSlot = playerInventory.getFreeSlot();
            int slotWithRemainingSpace = playerInventory.getSlotWithRemainingSpace(itemStack);
            int slotNum = freeSlot == -1? slotWithRemainingSpace : freeSlot ;
            if(slotNum != -1){
                playerInventory.setItem(slotNum,itemStack);
                suspiciousEnderman.setCarriedItem(ItemStack.EMPTY);
            }
        }

    }

    @Override
    public @Nullable Predicate<LivingEntity> TargetSelector() {
        return PLAYER;
    }
}
