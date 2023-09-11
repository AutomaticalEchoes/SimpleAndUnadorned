package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.MiniSusCreeper;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.MiniSusCreeper.Goal.ILookControl;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.MiniSusCreeper.Goal.IMoveCont;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.MiniSusCreeper.Goal.KeepLookAtGoal;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.MiniSusCreeper.Goal.WantedHeight;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class MiniSusCreeper extends Monster  {
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(MiniSusCreeper.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Direction> DATA_ATTACH_FACE_ID = SynchedEntityData.defineId(MiniSusCreeper.class, EntityDataSerializers.DIRECTION);
    private static final EntityDataAccessor<Boolean> IS_LOCKED = SynchedEntityData.defineId(MiniSusCreeper.class,EntityDataSerializers.BOOLEAN);
    protected static final AABB Y_AABB = new AABB(-0.3D,0,-0.3D,0.3D,1.0D,0.3D);
    protected static final AABB X_AABB = new AABB(-0.5D,0.2D,-0.3D,0.5D,0.8D,0.3D);
    protected static final AABB Z_AABB = new AABB(-0.3D,0.2D,-0.5D,0.3D,0.8D,0.5D);
    protected static final AABB BASE_AABB = new AABB(-0.5D,0,-0.5D,0.5D,1.0D,0.5D);
    protected static final List<Direction> WANTED_POSITION = List.of(Direction.EAST,Direction.WEST,Direction.SOUTH,Direction.NORTH,Direction.DOWN);
    private static final UUID SLOW_FALLING_ID = UUID.fromString("A5B6CF2A-2F7C-31EF-9022-7C3E7D5E6ABA");
    private static final AttributeModifier SLOW_FALLING = new AttributeModifier(SLOW_FALLING_ID, "Slow falling acceleration reduction", -0.07, AttributeModifier.Operation.ADDITION); // Add -0.07 to 0.08 so we get the vanilla default of 0.01
    private Direction lastDirection = Direction.DOWN;
    private boolean IonGround = true;
    private BlockPos effectBlockPos = BlockPos.ZERO;
    private int lazyTick = 0;
    public Map<Integer,Direction> DIRECTION_MAP = Map.of(0,Direction.UP,1,Direction.NORTH,2,Direction.SOUTH,3,Direction.WEST,4,Direction.EAST,5,Direction.DOWN);

    public MiniSusCreeper(EntityType<? extends MiniSusCreeper> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.lookControl = new ILookControl(this);
        this.moveControl = new IMoveCont(this);
        this.navigation = new WallClimberNavigation(this,this.level);
        initFace(findAttachableSurface(new BlockPos(this.position().add(0,0.5,0))));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.8D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ATTACH_FACE_ID, Direction.DOWN);
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
        this.entityData.define(IS_LOCKED,false);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> p_21104_) {
        if (DATA_ATTACH_FACE_ID.equals(p_21104_)) refreshDimensions();
        super.onSyncedDataUpdated(p_21104_);
    }

    @Override
    public float getEyeHeightAccess(Pose pose, EntityDimensions size) {
        float f = 0.85F;
        Direction attachFace = this.getAttachFace();
        if(attachFace == Direction.UP){
            f = 0.15F;
        }else if(attachFace != Direction.DOWN){
            f = 0.5F;
        }
        return size.height * f;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new WantedHeight(this,0.25D));
        this.goalSelector.addGoal(5, new KeepLookAtGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if(this.level.isClientSide) return;
        initFace(findAttachableSurface(new BlockPos(this.position().add(0,0.5,0))));
    }

    @Override
    public void move(MoverType p_19973_, Vec3 p_19974_) {
        super.move(p_19973_, p_19974_);
        if(this.level.isClientSide) return;
        this.onGround = IonGround;
    }

    public static boolean checkMiniCreeperSpawnRules(EntityType<? extends Monster> p_219014_, ServerLevelAccessor p_219015_, MobSpawnType p_219016_, BlockPos p_219017_, RandomSource p_219018_) {
        return p_219015_.getDifficulty() != Difficulty.PEACEFUL && p_219015_.getBrightness(LightLayer.BLOCK, p_219017_) < 8 && checkMobSpawnRules(p_219014_, p_219015_, p_219016_, p_219017_, p_219018_);
    }

    @Override
    public void travel(Vec3 p_21280_) {
        if (this.isEffectiveAi() || this.isControlledByLocalInstance()) {
            AttributeInstance gravity = this.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
            boolean flag = this.getDeltaMovement().y <= 0.0D;
            if (flag && this.hasEffect(MobEffects.SLOW_FALLING)) {
                if (!gravity.hasModifier(SLOW_FALLING)) gravity.addTransientModifier(SLOW_FALLING);
                this.resetFallDistance();
            } else if (gravity.hasModifier(SLOW_FALLING)) {
                gravity.removeModifier(SLOW_FALLING);
            }
            double d0 = gravity.getValue();

            FluidState fluidstate = this.level.getFluidState(this.blockPosition());
            if ((this.isInWater() || (this.isInFluidType(fluidstate) && fluidstate.getFluidType() != net.minecraftforge.common.ForgeMod.LAVA_TYPE.get())) && this.isAffectedByFluids() && !this.canStandOnFluid(fluidstate)) {
                if (this.isInWater() || (this.isInFluidType(fluidstate) && !this.moveInFluid(fluidstate, p_21280_, d0))) {
                    double d9 = this.getY();
                    float f4 = this.isSprinting() ? 0.9F : this.getWaterSlowDown();
                    float f5 = 0.02F;
                    float f6 = (float) EnchantmentHelper.getDepthStrider(this);
                    if (f6 > 3.0F) {
                        f6 = 3.0F;
                    }

                    if (!this.onGround) {
                        f6 *= 0.5F;
                    }

                    if (f6 > 0.0F) {
                        f4 += (0.54600006F - f4) * f6 / 3.0F;
                        f5 += (this.getSpeed() - f5) * f6 / 3.0F;
                    }

                    if (this.hasEffect(MobEffects.DOLPHINS_GRACE)) {
                        f4 = 0.96F;
                    }

                    f5 *= (float)this.getAttribute(net.minecraftforge.common.ForgeMod.SWIM_SPEED.get()).getValue();
                    this.moveRelative(f5, p_21280_);
                    this.move(MoverType.SELF, this.getDeltaMovement());
                    Vec3 vec36 = this.getDeltaMovement();
                    if (this.horizontalCollision && this.onClimbable()) {
                        vec36 = new Vec3(vec36.x, 0.2D, vec36.z);
                    }

                    this.setDeltaMovement(vec36.multiply((double)f4, (double)0.8F, (double)f4));
                    Vec3 vec32 = this.getFluidFallingAdjustedMovement(d0, flag, this.getDeltaMovement());
                    this.setDeltaMovement(vec32);
                    if (this.horizontalCollision && this.isFree(vec32.x, vec32.y + (double)0.6F - this.getY() + d9, vec32.z)) {
                        this.setDeltaMovement(vec32.x, (double)0.3F, vec32.z);
                    }
                }
            } else if (this.isInLava() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidstate)) {
                double d8 = this.getY();
                this.moveRelative(0.02F, p_21280_);
                this.move(MoverType.SELF, this.getDeltaMovement());
                if (this.getFluidHeight(FluidTags.LAVA) <= this.getFluidJumpThreshold()) {
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.5D, (double)0.8F, 0.5D));
                    Vec3 vec33 = this.getFluidFallingAdjustedMovement(d0, flag, this.getDeltaMovement());
                    this.setDeltaMovement(vec33);
                } else {
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
                }

                if (!this.isNoGravity()) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -d0 / 4.0D, 0.0D));
                }

                Vec3 vec34 = this.getDeltaMovement();
                if (this.horizontalCollision && this.isFree(vec34.x, vec34.y + (double)0.6F - this.getY() + d8, vec34.z)) {
                    this.setDeltaMovement(vec34.x, 0.3F, vec34.z);
                }
            } else {
                float f2 = this.level.getBlockState(effectBlockPos).getFriction(level, this.getBlockPosBelowThatAffectsMyMovement(), this);
                float f3 = this.onGround ? f2 * 0.91F : 0.91F;
                Vec3 vec35 = this.handleRelativeFrictionAndCalculateMovement(p_21280_, f2);
                double d2 = vec35.y;
                if (this.hasEffect(MobEffects.LEVITATION)) {
                    d2 += (0.05D * (double)(this.getEffect(MobEffects.LEVITATION).getAmplifier() + 1) - vec35.y) * 0.2D;
                    this.resetFallDistance();
                } else if (this.level.isClientSide && !this.level.hasChunkAt(effectBlockPos)) {
                    if (this.getY() > (double)this.level.getMinBuildHeight()) {
                        d2 = -0.1D;
                    } else {
                        d2 = 0.0D;
                    }
                }

                Vec3 movement = new Vec3(vec35.x, d2, vec35.z) ;
                if (!this.shouldDiscardFriction() ) {
                    movement = movement.multiply(getFriction(f3));
                }

                if(!this.isNoGravity()){
                    movement = this.IonGround && deathTime <=0 ? movement.add(getGravity(d0)) : movement.add(0, -d0,0);
                }

                this.setDeltaMovement(movement);
            }
        }

        this.calculateEntityAnimation(this, this instanceof FlyingAnimal);
    }

    // --------------------------------------------------------------------------------------------------------------------------------------------------

    public void setLocked(boolean lock){
        this.entityData.set(IS_LOCKED,lock);
    }

    public boolean isLocked(){
        return this.entityData.get(IS_LOCKED);
    }

    public Direction getAttachFace() {
        return this.entityData.get(DATA_ATTACH_FACE_ID);
    }

    private void setAttachFace(Direction p_149789_) {
        this.entityData.set(DATA_ATTACH_FACE_ID, p_149789_);
    }

    public Vec3 getGravity(double num){
        return switch (lastDirection){
            case DOWN -> new Vec3(0,-num,0);
            case UP -> new Vec3(0,num,0);
            case NORTH -> new Vec3(0,0,-num);
            case SOUTH -> new Vec3(0,0,num);
            case WEST -> new Vec3(-num,0,0);
            case EAST -> new Vec3(num,0,0);
        };
    }

    public Vec3 getFriction(double num){
        return switch (lastDirection){
            case UP, DOWN -> new Vec3(num,0.88F,num);
            case NORTH ,SOUTH -> new Vec3(num,num,0.88F);
            case WEST ,EAST ->  new Vec3(0.88F,num,num);
        };
    }

    @Nullable
    protected Direction findAttachableSurface(BlockPos p_149811_) {
        for (int i = 0; i < DIRECTION_MAP.size(); i++) {
            if(this.canStayAt(p_149811_, DIRECTION_MAP.get(i)) && ( !IonGround || isValidDirection(DIRECTION_MAP.get(i) ) )){
                return DIRECTION_MAP.get(i);
            }
        }
        return null;
    }

    boolean isValidDirection(Direction direction) {
        Direction attachFace = this.getAttachFace();
        if(direction == attachFace){
            return true;
        }else {
            boolean flag = switch (direction){
                case UP -> attachFace != Direction.DOWN;
                case EAST, WEST, SOUTH, NORTH, DOWN -> attachFace == Direction.DOWN;
            };

            if(flag) {
                ((IMoveCont) (this.getMoveControl())).waited();
                this.getNavigation().stop();
            }


            return flag;
        }
    }

    public void initFace(Direction attachableSurface){
        if(this.isInFluidType()) attachableSurface = Direction.DOWN;
        this.lazyTick = attachableSurface != null ? 1 : lazyTick-1;
        this.setAttachFace(attachableSurface != null ? attachableSurface : lastDirection);
        this.onGround = this.IonGround = !this.isInFluidType() &&( attachableSurface !=null || lazyTick >= 0 );
        this.lastDirection = getAttachFace();
        this.effectBlockPos = this.blockPosition().relative(getAttachFace());
    }

    @Override
    public Vec3 handleRelativeFrictionAndCalculateMovement(Vec3 p_21075_, float p_21076_) {
        return super.handleRelativeFrictionAndCalculateMovement(p_21075_, p_21076_).multiply(1.0F,this.IonGround && this.getAttachFace()!=Direction.DOWN? 0.025F : 1.0F,1.0F);
    }

    public boolean canStayAt(BlockPos p_149786_, Direction p_149787_) {
        if (this.isPositionBlocked(p_149786_)) {
            return false;
        } else {
            Direction direction = p_149787_.getOpposite();
            if (!this.level.loadedAndEntityCanStandOnFace(p_149786_.relative(p_149787_), this, direction)) {
                return false;
            } else {
                AABB aabb = getBoundingBox().deflate(1.0E-6D);
                return this.level.noCollision(this, aabb);
            }
        }
    }

    private boolean isPositionBlocked(BlockPos p_149813_) {
        BlockState blockstate = this.level.getBlockState(p_149813_);
        if (blockstate.isAir()) {
            return false;
        } else {
            boolean flag = blockstate.is(Blocks.MOVING_PISTON) && p_149813_.equals(this.blockPosition());
            return !flag;
        }
    }

    @Nullable
    public Vec3 getWantedPosition(){
        BlockPos offset = blockPosition().offset(8, 2, 8);
        ArrayList<BlockPos> blockPosArrayList = new ArrayList<>();
        for (int j = 0; j < 512; j++) {
            int jy = j / 289;
            int jz = (j % 289) / 17;
            int jx = j % 17;
            BlockPos blockPos = offset.offset(-jx,-jy,-jz);
            if(level.getBlockState(blockPos).getMaterial().isSolid() && canWantedStayAt(blockPos)) {
                blockPosArrayList.add(blockPos);
            }
        }
        if(blockPosArrayList.size() <=0) return null;

        List<BlockPos> collect = blockPosArrayList.stream().sorted(Comparator.comparingDouble(Vec3i::getY).reversed())
                .takeWhile(blockPos -> blockPos.getY() == blockPosArrayList.stream().max(Comparator.comparingDouble(Vec3i::getY)).get().getY())
                .collect(Collectors.toList());
        Optional<BlockPos> min = collect.stream()
                .min(Comparator.comparingDouble(o -> o.distToCenterSqr(position())));
        return min.map(blockPos -> new Vec3(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D)).orElse(null);
    }

    public boolean canWantedStayAt(BlockPos blockPos){
        return WANTED_POSITION.stream().anyMatch(direction -> level.getBlockState(blockPos.relative(direction)).isAir());
    }

    private Vec3 collide(Vec3 p_20273_) {
        AABB aabb = this.getBoundingBox();
        List<VoxelShape> list = this.level.getEntityCollisions(this, aabb.expandTowards(p_20273_));
        Vec3 vec3 = p_20273_.lengthSqr() == 0.0D ? p_20273_ : collideBoundingBox(this, p_20273_, aabb, this.level, list);
        boolean flag = p_20273_.x != vec3.x;
        boolean flag1 = p_20273_.y != vec3.y;
        boolean flag2 = p_20273_.z != vec3.z;
        boolean flag3 = this.onGround || flag1 && p_20273_.y < 0.0D;
        float stepHeight = getStepHeight();
        if (stepHeight > 0.0F && flag3 && (flag || flag2)) {
            Vec3 vec31 = collideBoundingBox(this, new Vec3(p_20273_.x, (double)stepHeight, p_20273_.z), aabb, this.level, list);
            Vec3 vec32 = collideBoundingBox(this, new Vec3(0.0D, (double)stepHeight, 0.0D), aabb.expandTowards(p_20273_.x, 0.0D, p_20273_.z), this.level, list);
            if (vec32.y < (double)stepHeight) {
                Vec3 vec33 = collideBoundingBox(this, new Vec3(p_20273_.x, 0.0D, p_20273_.z), aabb.move(vec32), this.level, list).add(vec32);
                if (vec33.horizontalDistanceSqr() > vec31.horizontalDistanceSqr()) {
                    vec31 = vec33;
                }
            }
            if (vec31.horizontalDistanceSqr() > vec3.horizontalDistanceSqr()) {
                return vec31.add(collideBoundingBox(this, new Vec3(0.0D, -vec31.y + p_20273_.y, 0.0D), aabb.move(vec31), this.level, list));
            }
        }
        return vec3;
    }


}
