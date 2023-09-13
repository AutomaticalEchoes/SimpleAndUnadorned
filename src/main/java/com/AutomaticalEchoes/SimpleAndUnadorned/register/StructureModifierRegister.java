package com.AutomaticalEchoes.SimpleAndUnadorned.register;

import com.AutomaticalEchoes.SimpleAndUnadorned.SimpleAndUnadorned;
import com.AutomaticalEchoes.SimpleAndUnadorned.forge.StructureModifiers;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.common.world.StructureModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class StructureModifierRegister {
    public static final DeferredRegister<Codec<? extends StructureModifier>> REGISTER = DeferredRegister.create(ForgeRegistries.Keys.STRUCTURE_MODIFIER_SERIALIZERS, SimpleAndUnadorned.MODID);
    public static final RegistryObject<Codec<StructureModifiers.AddSpawnsStructureModifier>> ADD_SPAWNS_STRUCTURE_MODIFIER = REGISTER.register("add_spawns", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    Structure.CODEC.fieldOf("structure").forGetter(StructureModifiers.AddSpawnsStructureModifier::structure),
                    new ExtraCodecs.EitherCodec<>(MobSpawnSettings.SpawnerData.CODEC.listOf(), MobSpawnSettings.SpawnerData.CODEC).xmap(
                            either -> either.map(Function.identity(), List::of), // convert list/singleton to list when decoding
                            list -> list.size() == 1 ? Either.right(list.get(0)) : Either.left(list) // convert list to singleton/list when encoding
                    ).fieldOf("spawners").forGetter(StructureModifiers.AddSpawnsStructureModifier::spawners)
            ).apply(builder, StructureModifiers.AddSpawnsStructureModifier::new))
    );
}
