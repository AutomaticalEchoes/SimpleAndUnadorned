package com.AutomaticalEchoes.SimpleAndUnadorned.api.DipolarUtils;

import net.minecraft.tags.EntityTypeTags;

public class Polarities {
    public static final DipolarTubeFunc SWASH = new BaseDipolarTubeFunc().Tick(DipolarTubeFunc::SWASH);
    public static final DipolarTubeFunc PASTE = new BaseDipolarTubeFunc().HitEntity(DipolarTubeFunc::PASTE);
    public static final DipolarTubeFunc HIDE = new BaseDipolarTubeFunc().ShouldHitEntity(DipolarTubeFunc::Hide_ShouldHitEntity).HitEntity(DipolarTubeFunc::Hide);
    public static final DipolarTubeFunc BURST = new BaseDipolarTubeFunc().HitEntity(DipolarTubeFunc::Burst);
    public static final DipolarTubeFunc RAIDERS_FRIENDLY = new BaseDipolarTubeFunc().ShouldHitEntity((dipolarTubeProjectile, entity) -> !entity.getType().is(EntityTypeTags.RAIDERS));
    public static final DipolarTubeFunc RAIDERS_ONLY = new BaseDipolarTubeFunc().ShouldHitEntity((dipolarTubeProjectile, entity) -> entity.getType().is(EntityTypeTags.RAIDERS));
}
