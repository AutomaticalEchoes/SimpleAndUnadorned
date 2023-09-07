package com.AutomaticalEchoes.SimpleAndUnadorned.api.DipolarUtils;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.projectile.DipolarTubeProjectile;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.apache.logging.log4j.util.TriConsumer;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public  class BaseDipolarTubeFunc implements DipolarTubeFunc {
    private BiConsumer<DipolarTubeProjectile,HitResult> HIT_FUNC = (dipolarTubeProjectile, hitResult) -> {

    };
    private BiConsumer<DipolarTubeProjectile, BlockHitResult> BLOCK_HIT_FUNC = (dipolarTubeProjectile, blockHitResult) -> {

    };
    private BiConsumer<DipolarTubeProjectile, EntityHitResult> ENTITY_HIT_FUNC = (dipolarTubeProjectile, entityHitResult) -> {

    };
    private BiConsumer<DipolarTubeProjectile, Integer> TICK = (dipolarTubeProjectile, tickCount) -> {

    };
    private BiFunction<DipolarTubeProjectile, Entity ,Boolean> SHOULD_HIT_ENTITY = (projectile,target) -> true;
    private boolean shouldHitBlock = true;

    @Override
    public boolean shouldHitEntity(DipolarTubeProjectile projectile,Entity target) {
        return SHOULD_HIT_ENTITY.apply(projectile,target);
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
    public BaseDipolarTubeFunc ShouldHitEntity(BiFunction<DipolarTubeProjectile,Entity,Boolean> func){
        this.SHOULD_HIT_ENTITY = func;
        return this;
    }
}