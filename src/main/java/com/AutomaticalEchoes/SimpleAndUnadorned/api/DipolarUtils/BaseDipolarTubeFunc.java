package com.AutomaticalEchoes.SimpleAndUnadorned.api.DipolarUtils;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.projectile.DipolarTubeProjectile;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.apache.logging.log4j.util.TriConsumer;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public  class BaseDipolarTubeFunc implements DipolarTubeFunc {
    private BiConsumer<DipolarTubeProjectile,HitResult> HIT_FUNC = (dipolarTubeProjectile, hitResult) -> {

    };
    private BiConsumer<DipolarTubeProjectile, BlockHitResult> BLOCK_HIT_FUNC = (dipolarTubeProjectile, blockHitResult) -> {

    };
    private BiConsumer<DipolarTubeProjectile, EntityHitResult> ENTITY_HIT_FUNC = (dipolarTubeProjectile, entityHitResult) -> {

    };
    private BiConsumer<DipolarTubeProjectile, Integer> TICK = (dipolarTubeProjectile, tickCount) -> {

    };

    @Override
    public boolean CanUse(DipolarTubeProjectile projectile) {
        return true;
    }

    @Override
    public void onHit(DipolarTubeProjectile dipolarTubeProjectile, HitResult hitResult) {
        HIT_FUNC.accept(dipolarTubeProjectile, hitResult);
    }

    @Override
    public void onHitEntity(DipolarTubeProjectile dipolarTubeProjectile, EntityHitResult entityHitResult) {
        ENTITY_HIT_FUNC.accept(dipolarTubeProjectile, entityHitResult );
    }

    @Override
    public void onHitBlock(DipolarTubeProjectile dipolarTubeProjectile, BlockHitResult blockHitResult) {
        BLOCK_HIT_FUNC.accept(dipolarTubeProjectile, blockHitResult );
    }

    @Override
    public void tick(DipolarTubeProjectile dipolarTubeProjectile , Integer tickCount) {
        TICK.accept(dipolarTubeProjectile, tickCount);
    }

    public BaseDipolarTubeFunc Hit( BiConsumer<DipolarTubeProjectile,HitResult> func){
        this.HIT_FUNC = func;
        return this;
    }

    public BaseDipolarTubeFunc HitBlock(BiConsumer<DipolarTubeProjectile, BlockHitResult> func){
        this.BLOCK_HIT_FUNC = func;
        return this;
    }

    public BaseDipolarTubeFunc HitEntity(BiConsumer<DipolarTubeProjectile, EntityHitResult> func){
        this.ENTITY_HIT_FUNC = func;
        return this;
    }
    public BaseDipolarTubeFunc Tick(BiConsumer<DipolarTubeProjectile, Integer> func){
        this.TICK = func;
        return this;
    }
}