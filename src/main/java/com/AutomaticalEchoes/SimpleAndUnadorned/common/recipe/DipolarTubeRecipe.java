package com.AutomaticalEchoes.SimpleAndUnadorned.common.recipe;

import com.AutomaticalEchoes.SimpleAndUnadorned.register.ItemsRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.RecipeRegister;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class DipolarTubeRecipe extends CustomRecipe {
    public DipolarTubeRecipe(ResourceLocation p_43833_) {
        super(p_43833_);
    }

    @Override
    public boolean matches(CraftingContainer p_44002_, Level p_44003_) {
        int startNum = 1;
        while (startNum <= 4){
            if(p_44002_.getItem(startNum).getItem() == Items.POTION) break;
            startNum += 3;
        }
        if(startNum > 4) return false;
        boolean isValid = true;
        for (int i = 0; i < 3; i++) {
            isValid = isValid && p_44002_.getItem(i + startNum + 2).is(ItemTags.WOODEN_BUTTONS);
        }
        return isValid;
    }

    @Override
    public ItemStack assemble(CraftingContainer p_44001_) {
        int startNum = 1;
        ItemStack itemStack = ItemStack.EMPTY;
        while (startNum <= 4){
            itemStack = p_44001_.getItem(startNum);
            if(itemStack.getItem() == Items.POTION) break;
            startNum += 3;
        }
        if(startNum > 4) return ItemStack.EMPTY;
        ItemStack potionDipolar = ItemsRegister.DIPOLAR_TUBE_POTION_ITEM.get().getDefaultInstance();
        potionDipolar.setTag(itemStack.getTag());
        potionDipolar.setCount(3);
        return potionDipolar;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return p_43999_ * p_44000_ >= 6;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegister.DIPOLAR_TUBE_POTION.get();
    }
}
