package com.AutomaticalEchoes.SimpleAndUnadorned.api.Function;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class IPredicate {
    public static final Predicate<ItemStack> ARROW_ITEM_LIKE = (p_43017_) -> p_43017_.getItem() instanceof ArrowItem;
}
