package com.AutomaticalEchoes.SimpleAndUnadorned.api.Function;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.blockEntity.SusSlimeBase;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.SuspiciousSlime;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.BlockRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;

import javax.annotation.Nullable;

public interface BlockFunction{
    static Boolean EmptyWithSlime(BlockState p_54760_, BlockGetter p_54761_, BlockPos p_54762_, CollisionContext p_54763_) {
        if (p_54763_ instanceof EntityCollisionContext collisionContext && collisionContext.getEntity() != null) {
            return !(collisionContext.getEntity() instanceof SuspiciousSlime);
        }
        return true;
    }

    @Nullable
    static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> p_152133_, BlockEntityType<E> p_152134_, BlockEntityTicker<? super E> p_152135_) {
        return p_152134_ == p_152133_ ? (BlockEntityTicker<A>)p_152135_ : null;
    }


    static  <T extends BlockEntity> BlockEntityTicker SusSlimeTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return p_153212_.isClientSide ? null : createTickerHelper(p_153214_, BlockRegister.BlockEntityRegister.SUS_SLIME_BASE.get(), SusSlimeBase::serverTick);
    }


}
