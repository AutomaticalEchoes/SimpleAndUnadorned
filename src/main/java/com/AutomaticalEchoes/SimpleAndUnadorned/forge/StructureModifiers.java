package com.AutomaticalEchoes.SimpleAndUnadorned.forge;

import com.AutomaticalEchoes.SimpleAndUnadorned.register.StructureModifierRegister;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.world.*;

import java.util.List;

public class StructureModifiers {
    public record AddSpawnsStructureModifier(Holder<Structure> structure, List<MobSpawnSettings.SpawnerData> spawners) implements StructureModifier
    {
        /**
         * Convenience method for using a single spawn data.
         * @param structure Biomes to add mob spawns to.
         * @param spawner SpawnerData specifying EntityTYpe, weight, and pack size.
         * @return AddSpawnsStructureModifier that adds a single spawn entry to the specified biomes.
         */
        public static AddSpawnsStructureModifier singleSpawn(Holder<Structure> structure, MobSpawnSettings.SpawnerData spawner)
        {
            return new AddSpawnsStructureModifier(structure, List.of(spawner));
        }

        @Override
        public void modify(Holder<Structure> structure, Phase phase, ModifiableStructureInfo.StructureInfo.Builder builder) {
            if (phase == Phase.ADD && this.structure == structure) {
                StructureSettingsBuilder.StructureSpawnOverrideBuilder spawns = builder.getStructureSettings().getOrAddSpawnOverrides(MobCategory.MONSTER);
                for (MobSpawnSettings.SpawnerData spawner : this.spawners)
                {
                    spawns.addSpawn(spawner);
                }
            }
        }

        @Override
        public Codec<? extends StructureModifier> codec()
        {
            return StructureModifierRegister.ADD_SPAWNS_STRUCTURE_MODIFIER.get();
        }
    }
}
