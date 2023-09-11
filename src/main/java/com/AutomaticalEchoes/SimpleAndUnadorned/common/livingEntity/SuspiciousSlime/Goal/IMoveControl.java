package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.Goal;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.SuspiciousSlime;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class IMoveControl extends MoveControl {
        private float yRot;
        private int jumpDelay;
        private final SuspiciousSlime slime;
        private boolean isAggressive;
        private boolean isScare;

        public IMoveControl(SuspiciousSlime p_33668_) {
            super(p_33668_);
            this.slime = p_33668_;
            this.yRot = 180.0F * p_33668_.getYRot() / (float)Math.PI;
        }

        public void setDirection(float p_33673_, boolean p_33674_,boolean  p_33675_) {
            this.yRot = p_33673_;
            this.isAggressive = p_33674_;
            this.isScare = p_33675_;
        }

        public void setWantedMovement(double p_33671_) {
            this.speedModifier = p_33671_;
            this.operation = Operation.MOVE_TO;
        }

        public void tick() {
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.yRot, 90.0F));
            this.mob.yHeadRot = this.mob.getYRot();
            this.mob.yBodyRot = this.mob.getYRot();
            if (this.operation != Operation.MOVE_TO) {
                this.mob.setZza(0.0F);
            } else {
                this.operation = Operation.WAIT;
                if (this.mob.isOnGround()) {
                    float speed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    this.mob.setSpeed(this.isScare? speed * 2 : speed);
                    if (this.jumpDelay-- <= 0) {
                        this.jumpDelay = this.slime.getJumpDelay();
                        if (this.isAggressive) {
                            this.jumpDelay /= 3;
                        }else if(this.isScare){
                            this.jumpDelay /= 10;
                        }

                        this.slime.getJumpControl().jump();
                        if (this.slime.doPlayJumpSound()) {
                            this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), this.slime.getSoundPitch());
                        }
                    } else {
                        this.slime.xxa = 0.0F;
                        this.slime.zza = 0.0F;
                        this.mob.setSpeed(0.0F);
                    }
                } else {
                    this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                }

            }
        }

    public int getJumpDelay() {
        return jumpDelay;
    }
}
