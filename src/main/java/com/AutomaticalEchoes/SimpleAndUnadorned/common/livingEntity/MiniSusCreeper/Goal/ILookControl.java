package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.MiniSusCreeper.Goal;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.MiniSusCreeper.MiniSusCreeper;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;

import java.util.Optional;

public class ILookControl extends LookControl {
    public ILookControl(Mob p_24945_) {
        super(p_24945_);
    }

    @Override
    public void tick() {
        if(this.mob instanceof MiniSusCreeper miniSusCreeper){
            Direction attachFace = miniSusCreeper.getAttachFace();
            if (this.resetXRotOnTick()) {
                this.mob.setXRot(0.0F);
            }

            if (this.lookAtCooldown > 0) {
                --this.lookAtCooldown;
                this.getYRotD(attachFace).ifPresent((p_181130_) -> {
                    this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, p_181130_, this.yMaxRotSpeed);
                });
                this.getXRotD(attachFace).ifPresent((p_181128_) -> {
                    this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), p_181128_, this.xMaxRotAngle));
                });
            } else {
                this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, 10.0F);
            }

            this.clampHeadRotationToBody();
            return;
        }
        super.tick();
    }

    protected Optional<Float> getYRotD(Direction attachFace) {
        double dx = this.wantedX - this.mob.getX();
        double dz = this.wantedZ - this.mob.getZ();
        double dy = this.wantedY - this.mob.getY();
        double d0 = dx;
        double d1 = dz;
        switch (attachFace) {
            case SOUTH, NORTH -> {
                d0 = dx;
                d1 = dy;
            }
            case WEST, EAST -> {
                d0 = dz;
                d1 = dy;
            }
        }
      return !(Math.abs(d1) > (double)1.0E-5F) && !(Math.abs(d0) > (double)1.0E-5F) ? Optional.empty() : Optional.of((float)(Mth.atan2(d1, d0) * (double)(180F / (float)Math.PI)) - 90.0F);
    }

    protected Optional<Float> getXRotD(Direction attachFace) {
        double d0 = this.wantedX - this.mob.getX();
        double d1 = this.wantedY - this.mob.getEyeY();
        double d2 = this.wantedZ - this.mob.getZ();
        double df = Math.sqrt(d0 * d0 + d2 * d2);
        double dh = d1;
        switch (attachFace) {
            case SOUTH, NORTH -> {
                df = Math.sqrt(d0 * d0 + d1 * d1);
                dh = d2;
            }
            case WEST, EAST -> {
                df = Math.sqrt(d1 * d1 + d2 * d2);
                dh = d0;
            }
        }
        return !(Math.abs(dh) > (double)1.0E-5F) && !(Math.abs(df) > (double)1.0E-5F) ? Optional.empty() : Optional.of((float)(-(Mth.atan2(dh, df) * (double)(180F / (float)Math.PI))));
    }

    @Override
    protected float rotateTowards(float p_24957_, float p_24958_, float p_24959_) {
        float f = Mth.degreesDifference(p_24957_, p_24958_);
        float f1 = Mth.clamp(f, -p_24959_, p_24959_);
        return p_24957_ + f1;
    }
}
