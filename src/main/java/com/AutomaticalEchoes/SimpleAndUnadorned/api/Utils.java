package com.AutomaticalEchoes.SimpleAndUnadorned.api;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.block.NonNewtonianFluidBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Utils {
    public static float RadiusWithSpeed(double dealtMovementL){
        return (float) Math.floor(dealtMovementL * 3);
    }
    public static Vec3 ReadPos(CompoundTag compoundTag){
        if(!compoundTag.contains("pos_x") || !compoundTag.contains("pos_y") || !compoundTag.contains("pos_z")){
            return Vec3.ZERO;
        }
        double pos_x = compoundTag.getDouble("pos_x");
        double pos_y = compoundTag.getDouble("pos_y");
        double pos_z = compoundTag.getDouble("pos_z");
        return new Vec3(pos_x,pos_y,pos_z);
    }

    public static CompoundTag SavePos(Vec3 vec3){
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putDouble("pos_x",vec3.x);
        compoundTag.putDouble("pos_y",vec3.y);
        compoundTag.putDouble("pos_z",vec3.z);
        return compoundTag;
    }

    public static Vec3 PosToVec(BlockPos blockPos){
       return new Vec3(blockPos.getX(),blockPos.getY(),blockPos.getZ());
    }


    public static BlockPos RandomVecWithRange(LivingEntity livingEntity,double Range){
        return RandomVecWithRange(livingEntity.getRandomX(Range),255,livingEntity.getRandomZ(Range),livingEntity.level);
    }

    public static BlockPos RandomVecWithRange(double p_32544_, double p_32545_, double p_32546_ , Level level){
    BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(p_32544_, p_32545_, p_32546_);

        while(blockpos$mutableblockpos.getY() > level.getMinBuildHeight() && !level.getBlockState(blockpos$mutableblockpos).getMaterial().blocksMotion()) {
        blockpos$mutableblockpos.move(Direction.DOWN);
    }

    BlockState blockstate = level.getBlockState(blockpos$mutableblockpos);
    boolean flag = blockstate.getMaterial().blocksMotion();
    if(flag) return blockpos$mutableblockpos;
    return BlockPos.ZERO;
    }


    public static boolean inside(VoxelShape p_82886_, BlockPos p_82887_, Entity entity){
        return entity.getBlockStateOn().getBlock() instanceof NonNewtonianFluidBlock;
    }

    public static int IColor(int k){
        float f = 0.0F;
        float f1 = 0.0F;
        float f2 = 0.0F;
        int j = 1;
        f += (float)(k >> 16 & 255) / 255.0F;
        f1 += (float) (k >> 8 & 255) / 255.0F;
        f2 += (float)(k >> 0 & 255) / 255.0F;
        f = f / (float)j * 255.0F;
        f1 = f1 / (float)j * 255.0F;
        f2 = f2 / (float)j * 255.0F;
        return (int)f << 16 | (int)f1 << 8 | (int)f2;
    }

}
