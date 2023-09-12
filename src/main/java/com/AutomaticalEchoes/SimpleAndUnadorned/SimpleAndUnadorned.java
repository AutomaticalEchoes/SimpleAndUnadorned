package com.AutomaticalEchoes.SimpleAndUnadorned;

import com.AutomaticalEchoes.SimpleAndUnadorned.api.ICauldronInteraction;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.blockEntity.SusSlimeBase.TransformMap;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.item.DipolarTube;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SusPillager.Magazine;
import com.AutomaticalEchoes.SimpleAndUnadorned.config.ModCommonConfig;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.*;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.Random;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SimpleAndUnadorned.MODID)
public class SimpleAndUnadorned
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "simple_and_unadorned";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final CreativeModeTab SIMPLE_AND_UNADORNED_TAB = new CreativeModeTab("simple_and_unadorned") {
        @Override
        public ItemStack makeIcon() {
            return Items.CREEPER_BANNER_PATTERN.getDefaultInstance();
        }
    };

    public SimpleAndUnadorned()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        PoiTypeRegister.DEFERRED_REGISTER.register(modEventBus);
        FluidRegister.DEFERRED_REGISTER.register(modEventBus);
        FluidRegister.Type.TYPE_DEFERRED_REGISTER.register(modEventBus);
        EffectsRegister.REGISTRY.register(modEventBus);
        EntityRegister.REGISTER.register(modEventBus);
        BlockRegister.DEFERRED_REGISTER.register(modEventBus);
        BlockRegister.BlockEntityRegister.DEFERRED_REGISTER.register(modEventBus);
        ItemsRegister.REGISTRY.register(modEventBus);
        PotionRegister.REGISTRY.register(modEventBus);
        RecipeRegister.REGISTER.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModCommonConfig.SPEC,"myMod-common.toml");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event){
        InitTubePotionMap();
        Magazine.Init();
        ICauldronInteraction.Init();
        TransformMap.Init();
        // Do something when the server starts
    }

    public void InitTubePotionMap(){
        for(Potion potion : Registry.POTION) {
            if (potion != Potions.EMPTY) {
                ItemStack itemStack = new ItemStack(ItemsRegister.DIPOLAR_TUBE_POTION_ITEM.get());
                PotionUtils.setPotion(itemStack,potion);
                DipolarTube.ALL_POTION_TUBES.put(potion,itemStack);
            }

        }
    }

}
