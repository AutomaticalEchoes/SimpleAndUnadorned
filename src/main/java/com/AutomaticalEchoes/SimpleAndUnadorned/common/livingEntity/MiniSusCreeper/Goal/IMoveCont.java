package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.MiniSusCreeper.Goal;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.MiniSusCreeper.MiniSusCreeper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.phys.shapes.VoxelShape;

public class IMoveCont extends MoveControl {
    private float zPlus;
    private float xPlus;
    private float yPlus;
    public IMoveCont(MiniSusCreeper p_24983_) {
        super(p_24983_);
    }

    @Override
    public void tick() {
        if (this.operation == Operation.STRAFE) {
            float f = (float)this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
            float f1 = (float)this.speedModifier * f;
            float f2 = this.strafeForwards;
            float f3 = this.strafeRight;
            float f4 = Mth.sqrt(f2 * f2 + f3 * f3);
            if (f4 < 1.0F) {
                f4 = 1.0F;
            }

            f4 = f1 / f4;
            f2 *= f4;
            f3 *= f4;
            float f5 = Mth.sin(this.mob.getYRot() * ((float)Math.PI / 180F));
            float f6 = Mth.cos(this.mob.getYRot() * ((float)Math.PI / 180F));
            float f7 = f2 * f6 - f3 * f5;
            float f8 = f3 * f6 + f2 * f5;
            if (!this.isWalkable(f7, f8)) {
                this.strafeForwards = 1.0F;
                this.strafeRight = 0.0F;
            }

            this.mob.setSpeed(f1);
            this.mob.setZza(this.strafeForwards);
            this.mob.setXxa(this.strafeRight);
            this.operation = Operation.WAIT;
        } else if (this.operation == Operation.MOVE_TO) {
            this.operation = Operation.WAIT;
            intFacePos();
            double d0 = this.wantedX + 0.5 - (this.mob.getX() + xPlus);
            double d1 = this.wantedZ + 0.5 - (this.mob.getZ() + zPlus);
            double d2 = this.wantedY - (this.mob.getY() + yPlus);
            double d3 = d0 * d0 + d2 * d2 + d1 * d1;
            if (d3 < (double)2.5000003E-7F) {
                this.mob.setZza(0.0F);
                return;
            }

            float f9 = (float)(Mth.atan2(d1, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f9, 90.0F));
            this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            BlockPos blockpos = this.mob.blockPosition();
            BlockState blockstate = this.mob.level.getBlockState(blockpos);
            VoxelShape voxelshape = blockstate.getCollisionShape(this.mob.level, blockpos);
            if (d2 > (double)this.mob.getStepHeight() && d0 * d0 + d1 * d1 < (double)Math.max(1.0F, this.mob.getBbWidth()) || !voxelshape.isEmpty() && this.mob.getY() < voxelshape.max(Direction.Axis.Y) + (double)blockpos.getY() && !blockstate.is(BlockTags.DOORS) && !blockstate.is(BlockTags.FENCES)) {
                this.mob.getJumpControl().jump();
                this.operation = Operation.JUMPING;
            }
        } else if (this.operation == Operation.JUMPING) {
            this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            if (this.mob.isOnGround()) {
                this.operation = Operation.WAIT;
            }
        } else {
            this.mob.setZza(0.0F);
        }

    }

    private boolean isWalkable(float p_24997_, float p_24998_) {
        PathNavigation pathnavigation = this.mob.getNavigation();
        NodeEvaluator nodeevaluator = pathnavigation.getNodeEvaluator();
        return nodeevaluator.getBlockPathType(this.mob.level, Mth.floor(this.mob.getX() + (double) p_24997_), this.mob.getBlockY(), Mth.floor(this.mob.getZ() + (double) p_24998_)) == BlockPathTypes.WALKABLE;
    }

    public void waited(){
        this.operation = Operation.WAIT;
    }

    public void intFacePos(){
        if(! (this.mob instanceof MiniSusCreeper miniSusCreeper)) return;
        xPlus = yPlus = zPlus = 0.0F;
        switch (miniSusCreeper.getAttachFace()){
            case UP -> yPlus = 1.0F;
            case DOWN -> yPlus = 0.0F;
            case EAST -> xPlus = 0.5F;
            case WEST -> xPlus = -0.5F;
            case SOUTH -> zPlus = 0.5F;
            case NORTH -> zPlus = -0.5F;
        }
    }
}
