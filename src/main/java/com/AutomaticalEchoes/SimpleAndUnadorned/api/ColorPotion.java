package com.AutomaticalEchoes.SimpleAndUnadorned.api;

import net.minecraft.world.item.alchemy.Potion;

public class ColorPotion extends Potion {
    private int color;
    public ColorPotion Color(int i){
        this.color = i;
        return this;
    }

    public int getColor() {
        return color;
    }
}
