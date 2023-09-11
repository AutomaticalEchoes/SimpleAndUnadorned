package com.AutomaticalEchoes.SimpleAndUnadorned.common.event;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.Event;

import java.util.HashMap;
import java.util.Map;

public class ExpTransformerRegister extends Event {
    private final HashMap<Item,Integer> TRANSFORM_MAP;
    public ExpTransformerRegister(HashMap<Item,Integer> map) {
        TRANSFORM_MAP = map;
    }

    public void register(Item item , Integer integer){
        TRANSFORM_MAP.put(item,integer);
    }

    public void registerAll(Map<Item,Integer> map){
        TRANSFORM_MAP.putAll(map);
    }
}
