package com.AutomaticalEchoes.SimpleAndUnadorned.register;

import com.AutomaticalEchoes.SimpleAndUnadorned.SimpleAndUnadorned;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.item.DipolarTube;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.item.SuspiciousEnderPearlItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemsRegister {
    private static Item.Properties CreateProperties(){
        return new Item.Properties().tab(SimpleAndUnadorned.SIMPLE_AND_UNADORNED_TAB) ;
    }
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, SimpleAndUnadorned.MODID);

    public static final RegistryObject<Item> SUSPICIOUS_THROWN_ENDERPEARL_ITEM = REGISTRY.register("suspicious_thrown_enderpearl", () -> new SuspiciousEnderPearlItem(CreateProperties()));
    public static final RegistryObject<Item> SUS_ENDERPEARL_POWDER = REGISTRY.register("sus_enderpearl_powder",() -> new Item(CreateProperties()));

    public static final RegistryObject<Item> SUSPICIOUS_CREEPER_SAC =REGISTRY.register("suspicious_creeper_sac",() -> new Item(CreateProperties()));

    public static final RegistryObject<Item> SUSPICIOUS_SLIME_BALL = REGISTRY.register("suspicious_slime_ball",() -> new Item(CreateProperties()));
    public static final RegistryObject<Item> MUCUS_BUCKET = REGISTRY.register("mucus_bucket",() -> new BucketItem(FluidRegister.MUCUS, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(SimpleAndUnadorned.SIMPLE_AND_UNADORNED_TAB)));
    public static final RegistryObject<Item> ACIDITY_BUCKET = REGISTRY.register("acidity_bucket",() -> new BucketItem(FluidRegister.ACIDITY, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(SimpleAndUnadorned.SIMPLE_AND_UNADORNED_TAB)));
    public static final RegistryObject<Item> SUSPICIOUS_WATER_BUCKET = REGISTRY.register("suspicious_water_bucket",()-> new BucketItem(FluidRegister.SUSPICIOUS_WATER,(new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(SimpleAndUnadorned.SIMPLE_AND_UNADORNED_TAB)));
    public static final RegistryObject<Item> TRANSPARENT_CRYSTAL = REGISTRY.register("transparent_crystal",() -> new Item(CreateProperties()));
    public static final RegistryObject<Item> TRANSPARENT_CRYSTAL_POWDER = REGISTRY.register("transparent_crystal_powder",() -> new Item(CreateProperties()));
    public static final RegistryObject<Item> TRANSPARENT_CRYSTAL_BLOCK_ITEM = REGISTRY.register("transparent_crystal_block_item",() -> new BlockItem(BlockRegister.TRANSPARENT_CRYSTAL_BLOCK.get(),CreateProperties()));

    public static final RegistryObject<Item> DIPOLAR_TUBE_POTION_ITEM = REGISTRY.register("dipolar_tube_potion",() -> new DipolarTube(CreateProperties()));
    public static final RegistryObject<Item> DIPOLAR_TUBE_ITEM = REGISTRY.register("dipolar_tube",() -> new Item(CreateProperties()));
}
