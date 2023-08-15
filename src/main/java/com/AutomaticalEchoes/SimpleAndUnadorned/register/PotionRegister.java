package com.AutomaticalEchoes.SimpleAndUnadorned.register;


import com.AutomaticalEchoes.SimpleAndUnadorned.SimpleAndUnadorned;
import com.AutomaticalEchoes.SimpleAndUnadorned.api.ColorPotion;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class PotionRegister {
    public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(ForgeRegistries.POTIONS, SimpleAndUnadorned.MODID);

    public static final RegistryObject<Potion> RAGE_TARGET = REGISTRY.register("rage_target_potion",()->new Potion("rage_target",new MobEffectInstance(EffectsRegister.RAGE_TARGET.get(),4800),new MobEffectInstance(MobEffects.GLOWING,4800)));

    public static final RegistryObject<Potion> SUS_WATER = REGISTRY.register("sus_water", Potion::new);
    public static final RegistryObject<Potion> MUCUS = REGISTRY.register("mucus", () -> new ColorPotion().Color(0xA1639C58));
    public static final RegistryObject<Potion> ACIDITY = REGISTRY.register("acidity", () -> new ColorPotion().Color(0xA1953472));
    public static final RegistryObject<Potion> INVALID_ARMOR = REGISTRY.register("invalid_armor_potion",() -> new Potion("invalid_armor",new MobEffectInstance(EffectsRegister.INVALID_ARMOR.get(),4800)));
    public static final RegistryObject<Potion> ACID_EROSION = REGISTRY.register("acid_erosion_potion",()-> new Potion("acid_erosion",new MobEffectInstance(EffectsRegister.ACID_EROSION.get(),4800)));

}
