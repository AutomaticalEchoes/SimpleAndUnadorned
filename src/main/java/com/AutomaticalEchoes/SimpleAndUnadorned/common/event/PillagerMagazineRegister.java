package com.AutomaticalEchoes.SimpleAndUnadorned.common.event;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SusPillager.Magazine;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

import java.util.Set;

public class PillagerMagazineRegister extends Event {
    private final Set<ItemStack> attack;
    private final Set<ItemStack> help;

    public PillagerMagazineRegister(Set<ItemStack> attack, Set<ItemStack> help) {
        this.attack = attack;
        this.help = help;
    }

    public void Register(ItemStack itemStack , boolean isHarmful){
        if(!Magazine.ValidItemStack(itemStack)) return;
        if(isHarmful){
            attack.add(itemStack);
        }else {
            help.add(itemStack);
        }
    }
    public void RegisterAll(Set<ItemStack> itemStacks , boolean isHarmful){
        for (ItemStack itemStack : itemStacks) {
            if(!Magazine.ValidItemStack(itemStack)) continue;
            if(isHarmful){
                attack.add(itemStack);
            }else {
                help.add(itemStack);
            }
        }

    }
}
