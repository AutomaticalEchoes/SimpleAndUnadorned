package com.AutomaticalEchoes.SimpleAndUnadorned.client.Renderer;

import com.AutomaticalEchoes.SimpleAndUnadorned.client.Layer.ItemLayer;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.SuspiciousEnderman;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EnderEyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class SuspiciousEndermanRender extends MobRenderer<SuspiciousEnderman, EndermanModel<SuspiciousEnderman>> {
    private static final ResourceLocation ENDERMAN_LOCATION = new ResourceLocation("textures/entity/enderman/enderman.png");
    private final RandomSource random = RandomSource.create();
    public SuspiciousEndermanRender(EntityRendererProvider.Context p_173992_) {
        super(p_173992_, new EndermanModel<>(p_173992_.bakeLayer(ModelLayers.ENDERMAN)), 0.5F);
        this.addLayer(new EnderEyesLayer<>(this));
        this.addLayer(new ItemLayer<>(this, p_173992_.getItemInHandRenderer()));
    }

    @Nullable
    @Override
    protected RenderType getRenderType(SuspiciousEnderman p_115322_, boolean p_115323_, boolean p_115324_, boolean p_115325_) {
        return p_115322_.isSusBreak()? RenderType.endGateway() :  super.getRenderType(p_115322_, p_115323_, p_115324_, p_115325_);
    }

    @Override
    public boolean shouldRender(SuspiciousEnderman p_115468_, Frustum p_115469_, double p_115470_, double p_115471_, double p_115472_) {
        if (super.shouldRender(p_115468_, p_115469_, p_115470_, p_115471_, p_115472_)) {
            return true;
        } else {
            if (p_115468_.getTarget()!=null) {
                LivingEntity livingentity = p_115468_.getTarget();
                if (livingentity != null) {
                    Vec3 vec3 = this.getPosition(livingentity, (double)livingentity.getBbHeight() * 0.5D, 1.0F);
                    Vec3 vec31 = this.getPosition(p_115468_, (double)p_115468_.getEyeHeight(), 1.0F);
                    return p_115469_.isVisible(new AABB(vec31.x, vec31.y, vec31.z, vec3.x, vec3.y, vec3.z));
                }
            }
            return false;
        }
    }

    private Vec3 getPosition(LivingEntity p_114803_, double p_114804_, float p_114805_) {
        double d0 = Mth.lerp(p_114805_, p_114803_.xOld, p_114803_.getX());
        double d1 = Mth.lerp(p_114805_, p_114803_.yOld, p_114803_.getY()) + p_114804_;
        double d2 = Mth.lerp(p_114805_, p_114803_.zOld, p_114803_.getZ());
        return new Vec3(d0, d1, d2);
    }


    public void render(SuspiciousEnderman p_114829_, float p_114830_, float p_114831_, PoseStack p_114832_, MultiBufferSource p_114833_, int p_114834_) {
        EndermanModel<SuspiciousEnderman> endermanmodel = this.getModel();
        endermanmodel.creepy = p_114829_.isScared();
        endermanmodel.carrying = !p_114829_.getPassengers().isEmpty();
        super.render(p_114829_, p_114830_, p_114831_, p_114832_, p_114833_, p_114834_);
    }

    @Override
    public ResourceLocation getTextureLocation(SuspiciousEnderman p_114482_) {
        return ENDERMAN_LOCATION;
    }

    private static void vertex(VertexConsumer p_114842_, Matrix4f p_114843_, Matrix3f p_114844_, float p_114845_, float p_114846_, float p_114847_, float p_114851_, float p_114852_) {
        p_114842_.vertex(p_114843_, p_114845_, p_114846_, p_114847_).color(255).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(p_114844_, 0.0F, 1.0F, 0.0F).endVertex();
    }


    public Vec3 getRenderOffset(SuspiciousEnderman p_114336_, float p_114337_) {
        if (p_114336_.isScared()) {
            double d0 = 0.02D;
            return new Vec3(this.random.nextGaussian() * 0.02D, 0.0D, this.random.nextGaussian() * 0.02D);
        } else {
            return super.getRenderOffset(p_114336_, p_114337_);
        }
    }

}
