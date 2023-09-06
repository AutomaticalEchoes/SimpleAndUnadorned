package com.AutomaticalEchoes.SimpleAndUnadorned.common;

import com.AutomaticalEchoes.SimpleAndUnadorned.SimpleAndUnadorned;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.event.SusSlimeSummonEvent;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.event.SusSlimeWantExpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommonEvents {

//    @SubscribeEvent
//    public static void SummonSlime(SusSlimeSummonEvent event){
//        SimpleAndUnadorned.LOGGER.info("on Summon call");
//
//    }

    @SubscribeEvent
    public static void SpawnExpOrbWentWanted(SusSlimeWantExpEvent event){
        event.getSusSlimeBase().spawnOre();
    }


}
