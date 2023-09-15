package com.AutomaticalEchoes.SimpleAndUnadorned.api.DipolarUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class DipolarBrewingRecipe implements IBrewingRecipe {
    @Override
    public boolean isInput(ItemStack input) {
        return DipolarUtils.ValidItem(input);
    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        return BrewablePolarity.getPolarity(ingredient.getItem()) != null;
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        if (!this.isInput(input) || !this.isIngredient(ingredient)) {
            return ItemStack.EMPTY;
        }

        BrewablePolarity polarity = BrewablePolarity.getPolarity(ingredient.getItem());
        ItemStack resultItem = input.copy();
        CompoundTag resultItemTag = resultItem.getOrCreateTag();
        if(!resultItemTag.contains("polarity")){
            resultItemTag.put("polarity",new ListTag());
        }else if(DipolarUtils.isConflict(DipolarUtils.getPolarity(input),polarity)) {
            return ItemStack.EMPTY;
        }
        resultItemTag.getList("polarity", 10).add(polarity.getTag());
        return resultItem;
    }
}
