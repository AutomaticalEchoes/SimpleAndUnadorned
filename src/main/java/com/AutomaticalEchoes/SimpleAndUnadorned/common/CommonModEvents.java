package com.AutomaticalEchoes.SimpleAndUnadorned.common;

import com.AutomaticalEchoes.SimpleAndUnadorned.SimpleAndUnadorned;
import com.AutomaticalEchoes.SimpleAndUnadorned.api.DipolarUtils.DipolarBrewingRecipe;
import com.AutomaticalEchoes.SimpleAndUnadorned.api.PotionBrewingRecipe;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.MiniSusCreeper.MiniSusCreeper;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SusPillager.Magazine;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SusPillager.SusPillager;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousCreeper.SuspiciousCreeper;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.SuspiciousSlime;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.SuspiciousEnderman;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.netWork.PacketHandler;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.EntityRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.ItemsRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.PotionRegister;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
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
        event.enqueueWork(new Runnable() {
            @Override
            public void run() {
                CHANNEL = PacketHandler.RegisterPacket();
                SpawnPlacements.register(EntityRegister.SUSPICIOUS_ENDERMAN.get(),SpawnPlacements.Type.ON_GROUND,Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                        Mob::checkMobSpawnRules);
                SpawnPlacements.register(EntityRegister.SUSPICIOUS_CREEPER.get(),SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                        Monster::checkMonsterSpawnRules);
                SpawnPlacements.register(EntityRegister.SUSPICIOUS_SLIME.get(),SpawnPlacements.Type.ON_GROUND,Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                        SuspiciousSlime::checkSusSlimeSpawnRules);
                SpawnPlacements.register(EntityRegister.MINI_SUS_CREEPER.get(),SpawnPlacements.Type.ON_GROUND,Heightmap.Types.MOTION_BLOCKING,
                        MiniSusCreeper::checkMiniCreeperSpawnRules);
            }
        });
        BrewingRecipeRegistry.addRecipe(new PotionBrewingRecipe(Potions.AWKWARD, ItemsRegister.SUSPICIOUS_CREEPER_SAC.get(), PotionRegister.RAGE_TARGET.get()));
        BrewingRecipeRegistry.addRecipe(new PotionBrewingRecipe(PotionRegister.ACIDITY.get(), Items.FERMENTED_SPIDER_EYE,PotionRegister.ACID_EROSION.get()));
        BrewingRecipeRegistry.addRecipe(new PotionBrewingRecipe(PotionRegister.ACIDITY.get(), ItemsRegister.SUSPICIOUS_SLIME_BALL.get(),PotionRegister.INVALID_ARMOR.get()));
        BrewingRecipeRegistry.addRecipe(new DipolarBrewingRecipe());
    }

    @SubscribeEvent
    public static void registerEntityAttribute(EntityAttributeCreationEvent event){
        event.put(EntityRegister.SUSPICIOUS_CREEPER.get(), SuspiciousCreeper.createAttributes().build());
        event.put(EntityRegister.SUSPICIOUS_SLIME.get(), SuspiciousSlime.createAttributes().build());
        event.put(EntityRegister.MINI_SUS_CREEPER.get(), MiniSusCreeper.createAttributes().build());
        event.put(EntityRegister.SUSPICIOUS_ENDERMAN.get(), SuspiciousEnderman.createAttributes().build());
        event.put(EntityRegister.SUSPICIOUS_PILLAGER.get(), SusPillager.createAttributes().build());
    }
}
