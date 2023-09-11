package com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.Goal;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.SuspiciousSlime;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class LocateItem extends ProjectGoal {
    private @Nullable ItemEntity itemTarget;
    public LocateItem(SuspiciousSlime suspiciousSlime){
        super(suspiciousSlime);
    }

    @Override
    public boolean canUse() {
        if(slime.level instanceof ServerLevel serverLevel && slime.wantCollectItem()){
            List<Entity> entities = serverLevel.getEntities(slime, new AABB(slime.getX() - 10, slime.getY() - 3, slime.getZ() - 10, slime.getX() + 10, slime.getY() + 3, slime.getZ() + 10), new Predicate<Entity>() {
                @Override
                public boolean test(Entity entity) {
                    if(entity instanceof ItemEntity itemEntity){
                        return slime.getInventory().canAddItem(itemEntity.getItem());
                    }
                    return false;
                }
            });
            if(entities.isEmpty()) return false;
            this.itemTarget = (ItemEntity) SuspiciousSlime.getNearestEntity(entities,slime);
            return true ;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return itemTarget != null && itemTarget.isAlive() && slime.hasLineOfSight(itemTarget);
    }


    @Override
    public void tick() {
        if (itemTarget != null && itemTarget.isAlive()) {
            if(slime.distanceTo(itemTarget) < slime.getBbWidth() / 2) {
                slime.tryPickUp(itemTarget);
                return;
            }
            this.slime.lookAt(itemTarget, 10.0F, 10.0F);
            ((IMoveControl)this.slime.getMoveControl()).setDirection(this.slime.getYRot(), true,false);
        }else {
            stop();
        }
    }

    @Override
    public void stop() {
        super.stop();
    }
}
