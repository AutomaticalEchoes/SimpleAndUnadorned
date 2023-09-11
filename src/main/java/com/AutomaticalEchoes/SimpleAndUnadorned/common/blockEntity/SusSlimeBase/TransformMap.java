package com.AutomaticalEchoes.SimpleAndUnadorned.common.blockEntity.SusSlimeBase;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.event.ExpTransformerRegister;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;

public class TransformMap {
    protected static final HashMap<Item,Integer> TRANSFORM_MAP = new HashMap<>();
    public static void Init(){
        MinecraftForge.EVENT_BUS.post(new ExpTransformerRegister(TRANSFORM_MAP));
    }
}
