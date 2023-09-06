package com.AutomaticalEchoes.SimpleAndUnadorned.api.DipolarUtils;

import com.AutomaticalEchoes.SimpleAndUnadorned.register.ItemsRegister;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.brewing.IBrewingRecipe;

import java.util.List;

public class DipolarBrewingRecipe implements IBrewingRecipe {
    @Override
    public boolean isInput(ItemStack input) {
        return DipolarUtils.ValidItem(input);
    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        return Polarity.getPolarity(ingredient.getItem()) != null;
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        if (!this.isInput(input) || !this.isIngredient(ingredient)) {
            return ItemStack.EMPTY;
        }

        Polarity polarity = Polarity.getPolarity(ingredient.getItem());

        CompoundTag resultItemTag = input.getOrCreateTag().copy();
        if(!resultItemTag.contains("polarity")){
            resultItemTag.put("polarity",new ListTag());
        }else if(DipolarUtils.isConflict(DipolarUtils.getPolarity(input),polarity)) {
            return ItemStack.EMPTY;
        }

        resultItemTag.getList("polarity", 10).add(polarity.getTag());
        ItemStack resultItem = ItemsRegister.DIPOLAR_TUBE_POTION_ITEM.get().getDefaultInstance();
        resultItem.setTag(resultItemTag);
        return resultItem;
    }
}
