package com.AutomaticalEchoes.SimpleAndUnadorned.api.DipolarUtils;

import com.AutomaticalEchoes.SimpleAndUnadorned.register.ItemsRegister;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import javax.annotation.Nullable;
import java.util.HashMap;

public class Polarity {
    private static final HashMap<Item, Polarity> POLARITIES = new HashMap<>();
    private static final HashMap<String,Polarity> NAME_POLARITY = new HashMap<>();
    public static final Polarity SWASH = new Polarity("swash" , (byte) 1,ItemsRegister.TRANSPARENT_CRYSTAL_POWDER.get(),0x7eb1c7,Polarities.SWASH);
    public static final Polarity PASTE = new Polarity("paste", (byte) 2,Items.SLIME_BALL,0x38a132,Polarities.PASTE);
    public static final Polarity HIDE = new Polarity("hide", (byte) 1,ItemsRegister.SUS_ENDERPEARL_POWDER.get(),0x258474,Polarities.HIDE);
    public static final Polarity BURST = new Polarity("burst", (byte) 3, Items.BLAZE_POWDER,0xf3ff94,Polarities.BURST);


    @Nullable
    public static Polarity getPolarity(Item item){
        return POLARITIES.getOrDefault(item, null);
    }

    @Nullable
    public static Polarity getFromName(String name){
        return NAME_POLARITY.getOrDefault(name,null);
    }

    private final CompoundTag tag;
    private final String Name;
    private final Integer Color;
    private final Item MapItem;
    private final DipolarTubeFunc Func;
    private final Byte LogicalNum;

    Polarity(String name, byte logicalNum, Item item, int color, DipolarTubeFunc func) {
        this.Name = name;
        this.Color = color;
        this.MapItem = item;
        this.tag = new CompoundTag();
        this.tag.putString("name",name);
        this.Func = func;
        this.LogicalNum = logicalNum;
        NAME_POLARITY.put(name,this);
        POLARITIES.put(item,this);
    }

    public CompoundTag getTag() {
        return tag;
    }

    public String getName() {
        return Name;
    }

    public Integer getColor() {
        return Color;
    }

    public Item getMapItem() {
        return MapItem;
    }

    public DipolarTubeFunc getFunc() {
        return Func;
    }

    public Byte getLogicalNum() {
        return LogicalNum;
    }
}
