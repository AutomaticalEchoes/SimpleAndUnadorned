package com.AutomaticalEchoes.SimpleAndUnadorned.client.Renderer;

import com.AutomaticalEchoes.SimpleAndUnadorned.SimpleAndUnadorned;
import com.AutomaticalEchoes.SimpleAndUnadorned.client.Layer.PowerLayer;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousCreeper.SuspiciousCreeper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SuspiciousCreeperRender extends MobRenderer<SuspiciousCreeper,CreeperModel<SuspiciousCreeper>> {
    private static final ResourceLocation BOOMER_LOCATION = new ResourceLocation(SimpleAndUnadorned.MODID,"textures/model/entity/mini_sus_creeper/creeper.png");
    private static final ResourceLocation EYES_LOCATION_WARNING = new ResourceLocation(SimpleAndUnadorned.MODID,"textures/model/entity/mini_sus_creeper/creeper_eyes_warning.png");
    public SuspiciousCreeperRender(EntityRendererProvider.Context p_173958_) {
        super(p_173958_, new CreeperModel<>(p_173958_.bakeLayer(ModelLayers.CREEPER)), 0.5F);
        this.addLayer(new PowerLayer(this, p_173958_.getModelSet()));
        this.addLayer(new EyesLayer<>(this) {
            @Override
            public RenderType renderType() {
                return RenderType.eyes(EYES_LOCATION_WARNING);
            }
        });
    }

    protected void scale(SuspiciousCreeper p_114046_, PoseStack p_114047_, float p_114048_) {
        float f = p_114046_.getSwelling(p_114048_);
        float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
        f = Mth.clamp(f, 0.0F, 1.0F);
        f *= f;
        f *= f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        p_114047_.scale(f2, f3, f2);
    }

    protected float getWhiteOverlayProgress(SuspiciousCreeper p_114043_, float p_114044_) {
        float f = p_114043_.getSwelling(p_114044_);
        return (int)(f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
    }


    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SuspiciousCreeper p_114482_) {
        return BOOMER_LOCATION;
    }
}
