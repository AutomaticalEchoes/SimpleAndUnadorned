package com.AutomaticalEchoes.SimpleAndUnadorned.register;

import com.AutomaticalEchoes.SimpleAndUnadorned.SimpleAndUnadorned;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.projectile.AcidityBall;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.projectile.DipolarTubeProjectile;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.projectile.SuspiciousThrownEnderpearl;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegister {
    public static final DeferredRegister<EntityType<?>> REGISTER= DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SimpleAndUnadorned.MODID);
    public static final RegistryObject<EntityType<AcidityBall>> ACIDITY = REGISTER.register("acidity",
            () -> EntityType.Builder.of(AcidityBall::Create,MobCategory.MISC).sized(0.5F,0.5F)
                    .build("acidity"));

    public static final RegistryObject<EntityType<SuspiciousThrownEnderpearl>> SUSPICIOUS_THROWN_ENDERPEARL_PROJECTILE = REGISTER.register("suspicious_thrown_enderpearl",
            () -> EntityType.Builder.of(SuspiciousThrownEnderpearl::Create,MobCategory.MISC).sized(0.5f,0.5f)
                    .build("suspicious_thrown_enderpearl"));

    public static final RegistryObject<EntityType<DipolarTubeProjectile>> DIPOLAR_TUBE_PROJECTILE = REGISTER.register("dipolar_tube",
            () -> EntityType.Builder.of(DipolarTubeProjectile::Create,MobCategory.MISC).sized(0.5f,0.5F)
                    .build("dipolar_tube"));
}
