package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SusPillager.Goal;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SusPillager.SusPillager;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.CrossbowItem;

import java.util.EnumSet;

public class SusPillagerCrossbowAttackGoal extends Goal {
    public static final UniformInt PATHFINDING_DELAY_RANGE = TimeUtil.rangeOfSeconds(1, 2);
    private final SusPillager mob;
    private final double speedModifier;
    private int seeTime;

    private int updatePathDelay;

    public SusPillagerCrossbowAttackGoal(SusPillager p_25814_, double p_25815_) {
        this.mob = p_25814_;
        this.speedModifier = p_25815_;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        return this.isValidTarget() && this.isHoldingCrossbow();
    }

    private boolean isHoldingCrossbow() {
        return this.mob.isHolding(is -> is.getItem() instanceof CrossbowItem);
    }

    public boolean canContinueToUse() {
        return this.isValidTarget() && (this.canUse() || !this.mob.getNavigation().isDone()) && this.isHoldingCrossbow();
    }

    private boolean isValidTarget() {
        return this.mob.getTarget() != null && this.mob.getTarget().isAlive();
    }

    public void stop() {
        super.stop();
        this.mob.setAggressive(false);
        this.mob.setTarget((LivingEntity)null);
        this.seeTime = 0;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null) {
            boolean flag = this.mob.getSensing().hasLineOfSight(livingentity);
            boolean flag1 = this.seeTime > 0;
            if (flag != flag1) {
                this.seeTime = 0;
            }
            if (flag) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }
            if (seeTime < 5) {
                --this.updatePathDelay;
                if (this.updatePathDelay <= 0) {
                    this.mob.getNavigation().moveTo(livingentity, this.canRun() ? this.speedModifier : this.speedModifier * 0.5D);
                    this.updatePathDelay = PATHFINDING_DELAY_RANGE.sample(this.mob.getRandom());
                }
            } else {
                this.updatePathDelay = 0;
                this.mob.getNavigation().stop();
            }

            this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            if(mob.getBullet() <=0 && mob.getCrossbowState() != SusPillager.CrossbowState.CHARGING){
                mob.StartChargeBullet();
            }else if (mob.getCrossbowState() == SusPillager.CrossbowState.CHARGED && flag) {
                mob.Attack(livingentity,false);
            }

        }
    }

    private boolean canRun() {
        return mob.getCrossbowState() != SusPillager.CrossbowState.CHARGING;
    }


}
