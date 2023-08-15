package com.AutomaticalEchoes.SimpleAndUnadorned.api.Function;


import com.AutomaticalEchoes.SimpleAndUnadorned.common.entity.SuspiciousSlime;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.event.SusSlimeSummonEvent;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.ItemsRegister;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

public interface FluidFunction {
    float FRICTION = 0.6F;
    float MUCUS_FRICTION = 0.8F;
    int[] ALL = {0, 1, 2, 3};
    int[] PART ={0,1};

    static void MucusItem(ItemEntity itemEntity){
        itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().scale(0.0000001));
        TransformOrSummon(itemEntity);
    }

    static boolean MucusMove(FluidState state, LivingEntity entity, Vec3 movementVector, double gravity){
        if(entity instanceof SuspiciousSlime) return false;
        return NonNewtonianFluidMove(state, entity, movementVector, gravity);
    }

    static boolean NonNewtonianFluidMove(FluidState state, LivingEntity entity, Vec3 movementVector, double gravity){
        entity.setSprinting(false);
        boolean flag = entity.getDeltaMovement().y <= 0.0D;
        double d9 = entity.getY();
        float f1 = MUCUS_FRICTION;// 不少于0.6F  unless then 0.6F
        float f2 = (float) ((1 - f1) * 10E-5);
        float f3 = (float) (movementVector.length() + f2 * 10) ;
        float f4 =  f2 / f3;
        float f5 = 0.02F;
        f5 *= (float)entity.getAttribute(net.minecraftforge.common.ForgeMod.SWIM_SPEED.get()).getValue();
        entity.moveRelative(f5, movementVector);
        entity.move(MoverType.SELF, entity.getDeltaMovement());
        Vec3 vec36 = entity.getDeltaMovement();
        if (entity.horizontalCollision && entity.onClimbable()) {
            vec36 = new Vec3(vec36.x, 0.2D, vec36.z);
        }
        entity.setDeltaMovement(vec36.multiply(f4,f4,f4));
        Vec3 vec32 = entity.getFluidFallingAdjustedMovement(gravity, flag, entity.getDeltaMovement());
        entity.setDeltaMovement(vec32);
        if (entity.horizontalCollision && entity.isFree(vec32.x, vec32.y + (double)0.6F - entity.getY() + d9, vec32.z)){
            entity.setDeltaMovement(vec32.x, (double)0.3F, vec32.z);
        }
        return true;
    }

    static boolean HurtArmor(FluidState state, LivingEntity livingEntity, Vec3 movementVector, double gravity){
        return HurtArmor(livingEntity);
    }

    static boolean HurtArmor(LivingEntity livingEntity){
        if(livingEntity instanceof Player player){
            player.getInventory().hurtArmor(DamageSource.GENERIC,4,player.isSwimming() && player.isSprinting()?  ALL: PART);
        }
        return false;
    }

    static void Transform(ItemEntity itemEntity){
        Item item = itemEntity.getItem().getItem();
        if(itemEntity.getAge() > 600 && (item instanceof ArmorItem || item instanceof TieredItem)){
            int i1 = itemEntity.getItem().getMaxDamage() - itemEntity.getItem().getDamageValue();
            int num = i1 / 100;
            int exp = i1 % 100;
            for (int i = 0; i < num; i++) {
                ExperienceOrb experienceOrb = new ExperienceOrb(itemEntity.level, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), 100);
                itemEntity.level.addFreshEntity(experienceOrb);
            }
            ExperienceOrb experienceOrb = new ExperienceOrb(itemEntity.level, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), exp);
            itemEntity.level.addFreshEntity(experienceOrb);
            itemEntity.discard();
        }
    }

    static void Summon(ItemEntity entity){
        if(entity.getItem().getItem() == ItemsRegister.SUSPICIOUS_SLIME_BALL.get() && entity.level instanceof ServerLevel level){
            entity.getItem().shrink(1);
            MinecraftForge.EVENT_BUS.post(new SusSlimeSummonEvent(level,entity.position()));
        }
    }

    static void TransformOrSummon(ItemEntity entity){
        Transform(entity);
        Summon(entity);
    }

}
