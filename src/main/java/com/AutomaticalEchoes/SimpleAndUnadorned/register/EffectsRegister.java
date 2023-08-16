package com.AutomaticalEchoes.SimpleAndUnadorned.register;

import com.AutomaticalEchoes.SimpleAndUnadorned.SimpleAndUnadorned;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.effect.AcidErosion;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.effect.BaseEffect;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.effect.RageTargetEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EffectsRegister {
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, SimpleAndUnadorned.MODID);
    public static final RegistryObject<RageTargetEffect> RAGE_TARGET =REGISTRY.register("rage_target",
            () -> new RageTargetEffect(MobEffectCategory.HARMFUL, 16284963));
    public static final RegistryObject<MobEffect> INVALID_ARMOR=REGISTRY.register("invalid_armor",
            () -> BaseEffect.Create(MobEffectCategory.HARMFUL,5865782)
                    .addAttributeModifier(Attributes.ARMOR,"CB65EE4B-E64E-4BA1-824D-17F328D2E10C",(double) -0.4F, AttributeModifier.Operation.MULTIPLY_TOTAL)
                    .addAttributeModifier(Attributes.ARMOR_TOUGHNESS,"0A96DD88-9109-4533-98D2-450D4B622BD6",(double) -0.4F, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<AcidErosion> ACID_EROSION =REGISTRY.register("acid_erosion",
            () -> new AcidErosion(MobEffectCategory.HARMFUL, 16296963));

}
