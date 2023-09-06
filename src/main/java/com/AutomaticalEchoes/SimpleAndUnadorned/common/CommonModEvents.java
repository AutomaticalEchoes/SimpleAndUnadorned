package com.AutomaticalEchoes.SimpleAndUnadorned.common;

import com.AutomaticalEchoes.SimpleAndUnadorned.SimpleAndUnadorned;
import com.AutomaticalEchoes.SimpleAndUnadorned.api.DipolarUtils.DipolarBrewingRecipe;
import com.AutomaticalEchoes.SimpleAndUnadorned.api.PotionBrewingRecipe;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.ItemsRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.PotionRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.RecipeRegister;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(modid = SimpleAndUnadorned.MODID,bus=Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvents {
    public static SimpleChannel CHANNEL;
    @SubscribeEvent
    public static  void commonSetup(final FMLCommonSetupEvent event)
    {
        BrewingRecipeRegistry.addRecipe(new PotionBrewingRecipe(Potions.AWKWARD, ItemsRegister.SUSPICIOUS_CREEPER_SAC.get(), PotionRegister.RAGE_TARGET.get()));
        BrewingRecipeRegistry.addRecipe(new PotionBrewingRecipe(PotionRegister.ACIDITY.get(), Items.FERMENTED_SPIDER_EYE,PotionRegister.ACID_EROSION.get()));
        BrewingRecipeRegistry.addRecipe(new PotionBrewingRecipe(PotionRegister.ACIDITY.get(), ItemsRegister.SUSPICIOUS_SLIME_BALL.get(),PotionRegister.INVALID_ARMOR.get()));
        BrewingRecipeRegistry.addRecipe(new DipolarBrewingRecipe());
    }
}
