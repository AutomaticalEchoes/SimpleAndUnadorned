package com.AutomaticalEchoes.SimpleAndUnadorned.api;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class IContainerHelper {

    public static ItemStack addItem(Container container, ItemStack p_19174_) {
        ItemStack itemstack = p_19174_.copy();
        moveItemToOccupiedSlotsWithSameType(container,itemstack);
        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            moveItemToEmptySlots(container,itemstack);
            return itemstack.isEmpty() ? ItemStack.EMPTY : itemstack;
        }
    }

    private static void moveItemToEmptySlots(Container container,ItemStack p_19190_) {
        for(int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack itemstack = container.getItem(i);
            if (itemstack.isEmpty()) {
                container.setItem(i, p_19190_.copy());
                p_19190_.setCount(0);
                return;
            }
        }
    }

    private static void moveItemToOccupiedSlotsWithSameType(Container container,ItemStack p_19192_) {
        for(int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack itemstack = container.getItem(i);
            if (ItemStack.isSameItemSameTags(itemstack, p_19192_)) {
                moveItemsBetweenStacks(container,p_19192_, itemstack);
                if (p_19192_.isEmpty()) {
                    return;
                }
            }
        }
    }

    private static void moveItemsBetweenStacks(Container container ,ItemStack p_19186_, ItemStack p_19187_) {
        int i = Math.min(container.getMaxStackSize(), p_19187_.getMaxStackSize());
        int j = Math.min(p_19186_.getCount(), i - p_19187_.getCount());
        if (j > 0) {
            p_19187_.grow(j);
            p_19186_.shrink(j);
            container.setChanged();
        }
    }


}
