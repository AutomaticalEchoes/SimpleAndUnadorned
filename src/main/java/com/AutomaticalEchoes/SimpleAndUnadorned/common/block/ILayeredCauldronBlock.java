package com.AutomaticalEchoes.SimpleAndUnadorned.common.block;

import com.AutomaticalEchoes.SimpleAndUnadorned.api.Function.IFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Predicate;

public class ILayeredCauldronBlock extends LayeredCauldronBlock {
    private @Nullable IFunction.QuadFunction<BlockState,Level,BlockPos,Entity,Boolean> IFluidFunction ;
    public ILayeredCauldronBlock(Properties p_153522_, Predicate<Biome.Precipitation> p_153523_, Map<Item, CauldronInteraction> p_153524_) {
        super(p_153522_, p_153523_, p_153524_);
    }

    public ILayeredCauldronBlock FluidFunction(IFunction.QuadFunction<BlockState,Level,BlockPos,Entity,Boolean> IFluidFunction){
       this.IFluidFunction = IFluidFunction;
       return this;
    }

    public void entityInside(BlockState p_153534_, Level p_153535_, BlockPos p_153536_, Entity p_153537_) {
        if (!p_153535_.isClientSide && this.isEntityInsideContent(p_153534_, p_153536_, p_153537_) && IFluidFunction != null) {
            if(p_153537_.isOnFire()) {
                p_153537_.clearFire();
                if (p_153537_.mayInteract(p_153535_, p_153536_)) {
                    this.handleEntityOnFireInside(p_153534_, p_153535_, p_153536_);
                }
            }
            IFluidFunction.apply(p_153534_, p_153535_, p_153536_, p_153537_);
        }
    }

}
