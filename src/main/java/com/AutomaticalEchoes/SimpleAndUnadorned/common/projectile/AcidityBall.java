package com.AutomaticalEchoes.SimpleAndUnadorned.common.projectile;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.SuspiciousSlime;
import com.AutomaticalEchoes.SimpleAndUnadorned.config.ModCommonConfig;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.EffectsRegister;
import com.AutomaticalEchoes.SimpleAndUnadorned.register.EntityRegister;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class AcidityBall extends AbstractHurtingProjectile {
    private final float radius = ModCommonConfig.SUSPICIOUS_SLIME_ACIDITY_AREA_EFFECT_RADIUS.get();
    private final int duration = ModCommonConfig.SUSPICIOUS_SLIME_ACIDITY_AREA_EFFECT_DURATION_TIME.get() * 20;
    private Vec3 moveLand;
    public static AcidityBall Create(EntityType<? extends AcidityBall> acidity  , Level p_36834_){
        return new AcidityBall(EntityRegister.ACIDITY.get(),p_36834_);
    }
    public AcidityBall(Level level, LivingEntity owner){
        this(EntityRegister.ACIDITY.get(),level);
        this.setOwner(owner);
    }
    protected AcidityBall(EntityType<? extends AcidityBall> p_36833_, Level p_36834_) {
        super(p_36833_, p_36834_);
    }
    protected void onHit(HitResult p_36913_) {
        super.onHit(p_36913_);
        if (p_36913_.getType() != HitResult.Type.ENTITY || !this.ownedBy(((EntityHitResult)p_36913_).getEntity())) {
            if (!this.level.isClientSide) {
                List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D));
                AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
                Entity entity = this.getOwner();
                if (entity instanceof LivingEntity) {
                    areaeffectcloud.setOwner((LivingEntity)entity);
                }

                areaeffectcloud.setParticle(ParticleTypes.DRAGON_BREATH);
                areaeffectcloud.setRadius(entity instanceof SuspiciousSlime suspiciousSlime? radius * suspiciousSlime.getSize() / 4 : radius);
                areaeffectcloud.setDuration(duration);
                areaeffectcloud.setRadiusPerTick((7.0F - areaeffectcloud.getRadius()) / (float)areaeffectcloud.getDuration());
                areaeffectcloud.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,20,2));
                areaeffectcloud.addEffect(new MobEffectInstance(EffectsRegister.ACID_EROSION.get(),20,64));
                areaeffectcloud.addEffect(new MobEffectInstance(EffectsRegister.INVALID_ARMOR.get(),60,1));
                if (!list.isEmpty()) {
                    for(LivingEntity livingentity : list) {
                        double d0 = this.distanceToSqr(livingentity);
                        if (d0 < 16.0D) {
                            areaeffectcloud.setPos(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                            break;
                        }
                    }
                }
                this.level.levelEvent(2006, this.blockPosition(), this.isSilent() ? -1 : 1);
                this.level.addFreshEntity(areaeffectcloud);
                this.discard();
            }

        }
    }

    @Override
    public void tick() {
        super.tick();
        if(moveLand!=null) setDeltaMovement(moveLand);
    }

    public boolean isPickable() {
        return false;
    }

    public boolean hurt(DamageSource p_36910_, float p_36911_) {
        return false;
    }

    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.DRAGON_BREATH;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    public void setMoveLand(Vec3 moveLand) {
        this.moveLand = moveLand;
    }
}
