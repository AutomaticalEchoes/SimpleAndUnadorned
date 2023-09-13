package com.AutomaticalEchoes.SimpleAndUnadorned.common.event;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SusPillager.Magazine;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

import java.util.Map;
import java.util.Set;

public class PillagerMagazineRegister extends Event {
    private final Magazine magazine;

    public PillagerMagazineRegister(Magazine magazine) {
        this.magazine = magazine;
    }

    public void Register(ItemStack itemStack , Integer weight){
        if(!Magazine.ValidItemStack(itemStack)) return;
        magazine.register(itemStack,weight);
    }

    public void RegisterAll(Map<ItemStack,Integer> map){
        for (Map.Entry<ItemStack,Integer> entry : map.entrySet()) {
            Register(entry.getKey(),entry.getValue());
        }

    }

    public static class Harmful extends PillagerMagazineRegister{

        public Harmful(Magazine magazine) {
            super(magazine);
        }
    }

    public static class Beneficial extends PillagerMagazineRegister{

        public Beneficial(Magazine magazine) {
            super(magazine);
        }
    }
}
