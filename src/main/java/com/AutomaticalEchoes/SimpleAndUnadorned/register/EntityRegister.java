package com.AutomaticalEchoes.SimpleAndUnadorned.register;

import com.AutomaticalEchoes.SimpleAndUnadorned.SimpleAndUnadorned;
import com.AutomaticalEchoes.SimpleAndUnadorned.api.IExperienceOrb;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.MiniSusCreeper.MiniSusCreeper;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousCreeper.SuspiciousCreeper;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.SuspiciousSlime;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.SuspiciousEnderman;
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
    public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SimpleAndUnadorned.MODID);
    public static final RegistryObject<EntityType<AcidityBall>> ACIDITY = REGISTER.register("acidity",
            () -> EntityType.Builder.of(AcidityBall::Create,MobCategory.MISC).sized(0.5F,0.5F)
                    .build("acidity"));

    public static final RegistryObject<EntityType<SuspiciousThrownEnderpearl>> SUSPICIOUS_THROWN_ENDERPEARL_PROJECTILE = REGISTER.register("suspicious_thrown_enderpearl",
            () -> EntityType.Builder.of(SuspiciousThrownEnderpearl::Create,MobCategory.MISC).sized(0.5f,0.5f)
                    .build("suspicious_thrown_enderpearl"));

    public static final RegistryObject<EntityType<DipolarTubeProjectile>> DIPOLAR_TUBE_PROJECTILE = REGISTER.register("dipolar_tube",
            () -> EntityType.Builder.of(DipolarTubeProjectile::Create,MobCategory.MISC).sized(0.5f,0.5F)
                    .build("dipolar_tube"));
    public static final RegistryObject<EntityType<SuspiciousCreeper>> SUSPICIOUS_CREEPER = REGISTER.register("suspicious_creeper",
            ()-> EntityType.Builder.of(SuspiciousCreeper::new, MobCategory.MONSTER).sized(1.0F,2.0F)
                    .clientTrackingRange(20)
                    .build("suspicious_creeper"));
    public static final RegistryObject<EntityType<SuspiciousSlime>> SUSPICIOUS_SLIME = REGISTER.register("suspicious_slime",
            () -> EntityType.Builder.of(SuspiciousSlime::new, MobCategory.MONSTER).sized(2.04F,2.04F)
                    .clientTrackingRange(20)
                    .build("suspicious_slime"));
    public static final RegistryObject<EntityType<MiniSusCreeper>> MINI_SUS_CREEPER = REGISTER.register("mini_suspicious_creeper",
            ()-> EntityType.Builder.of(MiniSusCreeper::new, MobCategory.MONSTER).sized(1.0F,1.0F)
                    .clientTrackingRange(20)
                    .build("mini_suspicious_creeper"));
    public static final RegistryObject<EntityType<SuspiciousEnderman>> SUSPICIOUS_ENDERMAN = REGISTER.register("suspicious_enderman",
            ()-> EntityType.Builder.of(SuspiciousEnderman::new, MobCategory.MONSTER).sized(1.0F,3.0F)
                    .clientTrackingRange(20)
                    .build("suspicious_endermann"));
    public static final RegistryObject<EntityType<IExperienceOrb>> I_EXPERIENCE_ORB= REGISTER.register("i_experience_orb",
            ()-> EntityType.Builder.of(IExperienceOrb::Create, MobCategory.MISC).sized(1.0F,3.0F)
                    .clientTrackingRange(20)
                    .build("i_experience_orb"));
}
