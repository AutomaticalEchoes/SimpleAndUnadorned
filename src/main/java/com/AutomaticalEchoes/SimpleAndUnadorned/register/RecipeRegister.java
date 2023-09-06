package com.AutomaticalEchoes.SimpleAndUnadorned.register;

import com.AutomaticalEchoes.SimpleAndUnadorned.SimpleAndUnadorned;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.recipe.DipolarTubeRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeRegister {
    public static final DeferredRegister<RecipeSerializer<?>> REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SimpleAndUnadorned.MODID);
    public static final RegistryObject<SimpleRecipeSerializer<DipolarTubeRecipe>> DIPOLAR_TUBE_POTION = REGISTER.register("crafting_dipolar_tube_potion",
            () -> new SimpleRecipeSerializer<>(DipolarTubeRecipe::new));

}
