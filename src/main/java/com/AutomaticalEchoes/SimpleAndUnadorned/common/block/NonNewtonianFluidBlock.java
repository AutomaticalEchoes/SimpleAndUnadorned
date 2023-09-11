package com.AutomaticalEchoes.SimpleAndUnadorned.common.block;

import com.AutomaticalEchoes.SimpleAndUnadorned.api.Function.IFunction;
import com.AutomaticalEchoes.SimpleAndUnadorned.api.Utils;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.ItemsRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class NonNewtonianFluidBlock extends HalfTransparentBlock implements BucketPickup, EntityBlock {
    public static final VoxelShape STABLE_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private Supplier<? extends  Fluid> fluidSupplier = ForgeRegistries.FLUIDS.getDelegateOrThrow(Fluids.WATER);
    private Supplier<? extends Item> bucketPickupItem = ItemsRegister.SUSPICIOUS_WATER_BUCKET;
    private IFunction.QuadFunction<BlockState, BlockGetter, BlockPos, CollisionContext,Boolean> CustomCollisionShape = (state, blockGetter, pos, collisionContext) -> false;
    private BiFunction<BlockPos,BlockState,BlockEntity> blockEntity = (pos, state) -> null;
    private IFunction.SixConsumer<BlockState, Level, BlockPos, Block, BlockPos, Boolean> neighborChanged = (state, level, pos, block, pos2, o) -> { };
    private BlockEntityTickerFunc ticker = (level, state, blockEntityType) -> null;
    private GameEventListenerFunc gameEventListener = (serverLevel, blockEntity) -> null;
    private IFunction.PentConsumer<BlockState , Level , BlockPos , BlockState , Boolean> onRemove = (state, level, pos, state2, aBoolean) -> {
        if (state.hasBlockEntity() && (!state.is(state2.getBlock()) || !state2.hasBlockEntity())) {
            level.removeBlockEntity(pos);
        }
    };
    private double YieldingStress = 1;
    public NonNewtonianFluidBlock(Properties p_49795_) {
        super(p_49795_);
    }

    public NonNewtonianFluidBlock CustomCollisionShape(IFunction.QuadFunction<BlockState, BlockGetter, BlockPos, CollisionContext,Boolean> customCollisionShape){
        this.CustomCollisionShape = customCollisionShape;
        return this;
    }

    public NonNewtonianFluidBlock YieldingStress(Double yieldingStress){
        this.YieldingStress = yieldingStress;
        return this;
    }

    public NonNewtonianFluidBlock Fluid(Supplier<? extends Fluid> iFluid){
        this.fluidSupplier = iFluid;
        return this;
    }

    public NonNewtonianFluidBlock BucketPickupItem(Supplier<? extends Item> Item){
        this.bucketPickupItem = Item;
        return this;
    }

    public NonNewtonianFluidBlock BlockEntity(BiFunction<BlockPos,BlockState,BlockEntity>  blockEntity){
        this.blockEntity = blockEntity;
        return this;
    }

    public NonNewtonianFluidBlock NeighborChanged(IFunction.SixConsumer<BlockState, Level, BlockPos, Block, BlockPos, Boolean> neighborChanged){
        this.neighborChanged = neighborChanged;
        return this;
    }

    public NonNewtonianFluidBlock OnRemove(IFunction.PentConsumer<BlockState,Level,BlockPos,BlockState,Boolean>  onRemove){
        this.onRemove = onRemove;
        return this;
    }

    public NonNewtonianFluidBlock Ticker(BlockEntityTickerFunc<? extends BlockEntity> func){
        this.ticker = func;
        return this;
    }
    public NonNewtonianFluidBlock Listener(GameEventListenerFunc<? extends BlockEntity> func){
        this.gameEventListener = func;
        return this;
    }

    public VoxelShape getCollisionShape(BlockState p_54760_, BlockGetter p_54761_, BlockPos p_54762_, CollisionContext p_54763_) {
        boolean flag;
        boolean flag1 = shapeRule(p_54760_, p_54761_, p_54762_, p_54763_);
        Boolean flag2 = CustomCollisionShape.apply(p_54760_, p_54761_, p_54762_, p_54763_);
        flag = flag1 ? flag2 : false;
        return  flag ? Shapes.block() : Shapes.empty();

    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return Shapes.empty();
    }

    @Override
    public float getFriction(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        return entity != null && entity.getDeltaMovement().length() > YieldingStress?  0.6F : 0.8F;
    }

    public void fallOn(Level p_154567_, BlockState p_154568_, BlockPos p_154569_, Entity p_154570_, float p_154571_) {
        if (p_154570_.isSuppressingBounce() || Math.abs(p_154570_.getDeltaMovement().y) > YieldingStress) {
            super.fallOn(p_154567_, p_154568_, p_154569_, p_154570_, p_154571_);
        } else {
            p_154570_.causeFallDamage(p_154571_, 0.0F, DamageSource.FALL);
        }
    }

    public void updateEntityAfterFallOn(BlockGetter p_56406_, Entity p_56407_) {
        if (p_56407_.isSuppressingBounce() || Math.abs(p_56407_.getDeltaMovement().y) > YieldingStress) {
            super.updateEntityAfterFallOn(p_56406_, p_56407_);
        } else {
            this.bounceUp(p_56407_);
        }
    }

    @Override
    public FluidState getFluidState(BlockState p_60577_) {
        return fluidSupplier.get().defaultFluidState();
    }

    private void bounceUp(Entity p_56404_) {
        Vec3 vec3 = p_56404_.getDeltaMovement();
        if (vec3.y < 0.0D) {
            double d0 = p_56404_ instanceof LivingEntity ? 1.0D : 0.8D;
            p_56404_.setDeltaMovement(vec3.x, -vec3.y * d0, vec3.z);
        }

    }

    @Override
    public ItemStack pickupBlock(LevelAccessor p_152719_, BlockPos p_152720_, BlockState p_152721_) {
        p_152719_.setBlock(p_152720_, Blocks.AIR.defaultBlockState(),11);
        return bucketPickupItem.get().getDefaultInstance();
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.empty();
    }

    @Override
    public void onRemove(BlockState p_60515_, Level p_60516_, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_) {
        onRemove.apply(p_60515_,p_60516_,p_60517_,p_60518_,p_60519_);
    }

    private boolean shapeRule(BlockState p_54760_, BlockGetter p_54761_, BlockPos p_54762_, CollisionContext p_54763_){
        if(p_54763_ instanceof EntityCollisionContext collisionContext && collisionContext.getEntity() !=null && !Utils.inside(STABLE_SHAPE, p_54762_, collisionContext.getEntity())){
            Vec3 deltaMovement = collisionContext.getEntity().getDeltaMovement();
            if(deltaMovement.length() > 0.1 * YieldingStress){
                return true;
            }
            if(deltaMovement.y > -0.1 * YieldingStress && Math.abs(deltaMovement.y) < 0.06) {
                return false;
            }
        }
        return false;
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return blockEntity.apply(p_153215_, p_153216_);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return ticker.apply(p_153212_,p_153213_,p_153214_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> GameEventListener getListener(ServerLevel p_221121_, T p_221122_) {
        return gameEventListener.apply(p_221121_,p_221122_);
    }
    @Override
    public void neighborChanged(BlockState p_55666_, Level p_55667_, BlockPos p_55668_, Block p_55669_, BlockPos p_55670_, boolean p_55671_) {
        neighborChanged.apply(p_55666_,p_55667_,p_55668_,p_55669_,p_55670_,p_55671_);
    }

    public interface BlockEntityTickerFunc<T extends BlockEntity>{
        BlockEntityTicker<T> apply(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_);
    }

    public interface GameEventListenerFunc<T extends BlockEntity>{
        GameEventListener apply(ServerLevel p_221121_, T p_221122_);
    }
}
