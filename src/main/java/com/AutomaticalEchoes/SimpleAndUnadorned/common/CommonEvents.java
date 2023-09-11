package com.AutomaticalEchoes.SimpleAndUnadorned.common;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.SuspiciousSlime;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.event.SusSlimeSummonEvent;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.event.SusSlimeWantExpEvent;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.EntityRegister;
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


}
