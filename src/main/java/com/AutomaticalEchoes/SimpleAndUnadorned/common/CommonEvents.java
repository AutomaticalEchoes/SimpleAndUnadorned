package com.AutomaticalEchoes.SimpleAndUnadorned.common;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.event.ExpTransformerRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.event.PillagerMagazineRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.item.DipolarTube;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.SuspiciousSlime;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.event.SusSlimeSummonEvent;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.event.SusSlimeWantExpEvent;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.EntityRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.ItemsRegister;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommonEvents {

    @SubscribeEvent
    public static void SummonSlime(SusSlimeSummonEvent event){
        SuspiciousSlime suspiciousSlime = EntityRegister.SUSPICIOUS_SLIME.get().create(event.getLevel());
        suspiciousSlime.setSize(1,true);
        suspiciousSlime.setPos(event.getVec3());
        event.getLevel().addFreshEntity(suspiciousSlime);

    }

    @SubscribeEvent
    public static void SpawnExpOrbWentWanted(SusSlimeWantExpEvent event){
        event.getSusSlimeBase().spawnOre();
    }

    @SubscribeEvent
    public static void RegisterExpTransformer(ExpTransformerRegister event){
        event.register(Items.DIAMOND,16);
    }

    @SubscribeEvent
    public static void RegisterPillagerMagazine(PillagerMagazineRegister event){
        event.Register(DipolarTube.ALL_POTION_TUBES.get(Potions.LONG_POISON),true);
    }
}
