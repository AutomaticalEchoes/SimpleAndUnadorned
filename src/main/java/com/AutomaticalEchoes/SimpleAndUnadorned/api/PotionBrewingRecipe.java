package com.AutomaticalEchoes.SimpleAndUnadorned.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class PotionBrewingRecipe implements IBrewingRecipe {
    private final Potion input;
    private final Potion output;
    private final Item ingredient;

    public PotionBrewingRecipe(Potion input, Item ingredient, Potion output) {
        this.input = input;
        this.output = output;
        this.ingredient = ingredient;
    }

    @Override
    public boolean isInput(ItemStack input) {
        return PotionUtils.getPotion(input)==this.input;
    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        return ingredient.getItem()==this.ingredient;
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        if (!this.isInput(input) || !this.isIngredient(ingredient)) {
            return ItemStack.EMPTY;
        }
        ItemStack itemStack=new ItemStack(input.getItem());
        itemStack.setTag(new CompoundTag());
        PotionUtils.setPotion(itemStack,this.output);
        return itemStack;
    }

}
