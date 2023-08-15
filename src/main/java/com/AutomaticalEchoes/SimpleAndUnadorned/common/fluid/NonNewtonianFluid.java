package com.AutomaticalEchoes.SimpleAndUnadorned.common.fluid;

import com.AutomaticalEchoes.SimpleAndUnadorned.register.ItemsRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class NonNewtonianFluid extends FlowingFluid {
    private Supplier<? extends Block> block = ForgeRegistries.BLOCKS.getDelegateOrThrow(Blocks.AIR);
    private Supplier<FluidType> fluidType = ForgeMod.WATER_TYPE;
    private Supplier<Item> bucketItem = ItemsRegister.SUSPICIOUS_WATER_BUCKET;
    public NonNewtonianFluid LegacyBlock (Supplier<? extends Block> block){
        this.block = block;
        return this;
    }
    public NonNewtonianFluid FluidType(Supplier<FluidType> fluidType){
        this.fluidType = fluidType;
        return this;
    }
    public NonNewtonianFluid BucketItem(Supplier<Item> item){
        this.bucketItem = item;
        return this;
    }
    @Override
    public Item getBucket() {
        return bucketItem.get();
    }

    @Override
    protected boolean canBeReplacedWith(FluidState p_76127_, BlockGetter p_76128_, BlockPos p_76129_, Fluid p_76130_, Direction p_76131_) {
        return false;
    }


    @Override
    public Fluid getFlowing() {
        return this;
    }

    @Override
    public Fluid getSource() {
        return this;
    }

    @Override
    protected boolean canConvertToSource() {
        return false;
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor p_76002_, BlockPos p_76003_, BlockState p_76004_) {

    }

    @Override
    protected int getSlopeFindDistance(LevelReader p_76074_) {
        return 0;
    }

    @Override
    protected int getDropOff(LevelReader p_76087_) {
        return 0;
    }

    @Override
    public int getTickDelay(LevelReader p_76120_) {
        return 0;
    }

    @Override
    protected float getExplosionResistance() {
        return 100F;
    }

    private static boolean hasSameAbove(FluidState p_76089_, BlockGetter p_76090_, BlockPos p_76091_) {
        return p_76089_.getType().isSame(p_76090_.getFluidState(p_76091_.above()).getType());
    }

    public float getHeight(FluidState p_76050_, BlockGetter p_76051_, BlockPos p_76052_) {
        return hasSameAbove(p_76050_, p_76051_, p_76052_) ? 1.0F : p_76050_.getOwnHeight();
    }

    public float getOwnHeight(FluidState p_76048_) {
        return (float)p_76048_.getAmount() / 9.0F;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState p_76136_) {
        return block.get().defaultBlockState();
    }

    @Override
    public boolean isSource(FluidState p_76140_) {
        return true;
    }

    @Override
    public int getAmount(FluidState p_76141_) {
        return 9;
    }

    @Override
    public VoxelShape getShape(FluidState p_76137_, BlockGetter p_76138_, BlockPos p_76139_) {
        return Shapes.block();
    }

    @Override
    public FluidType getFluidType() {
        return fluidType.get() ;
    }



}
