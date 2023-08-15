package com.AutomaticalEchoes.SimpleAndUnadorned.api.DipolarUtils;


import com.AutomaticalEchoes.SimpleAndUnadorned.common.projectile.DipolarTubeProjectile;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DipolarUtils{
    public static DipolarTubeProjectile makeProjectile(Level level, ItemStack itemStack, LivingEntity owner){
        return new DipolarTubeProjectile(owner, level , itemStack)
                .AddFunc(new BaseDipolarTubeFunc().HitEntity(DipolarTubeFunc::ApplyEffectOnHitLivingEntity))
                .AddFunc(new BaseDipolarTubeFunc().Tick(DipolarTubeFunc::CreateAreaEffectCloudTick));
    }

}
