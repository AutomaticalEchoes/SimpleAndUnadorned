package com.AutomaticalEchoes.SimpleAndUnadorned.api;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.block.NonNewtonianFluidBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public class Utils {
    public static final Random RANDOM = new Random();
    public static boolean inside(VoxelShape p_82886_, BlockPos p_82887_, Entity entity){
        return entity.getBlockStateOn().getBlock() instanceof NonNewtonianFluidBlock;
    }

}
