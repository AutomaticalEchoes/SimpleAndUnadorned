package com.AutomaticalEchoes.SimpleAndUnadorned.client.Layer;

import com.AutomaticalEchoes.SimpleAndUnadorned.SimpleAndUnadorned;
import com.AutomaticalEchoes.SimpleAndUnadorned.client.model.ICreeperModel;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.MiniSusCreeper.MiniSusCreeper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class MiniCreeperEyes extends EyesLayer<MiniSusCreeper, ICreeperModel<MiniSusCreeper>> {
    private static final ResourceLocation EYES_LOCATION_TOTAL = new ResourceLocation(SimpleAndUnadorned.MODID,"textures/model/entity/mini_sus_creeper/creeper_eyes.png");
    private static final ResourceLocation EYES_LOCATION_LIGHT = new ResourceLocation(SimpleAndUnadorned.MODID,"textures/model/entity/mini_sus_creeper/creeper_eyes_light.png");
    private static final ResourceLocation EYES_LOCATION_WARNING = new ResourceLocation(SimpleAndUnadorned.MODID,"textures/model/entity/mini_sus_creeper/creeper_eyes_warning.png");
    public MiniCreeperEyes(RenderLayerParent<MiniSusCreeper, ICreeperModel<MiniSusCreeper>> p_116981_) {
        super(p_116981_);
    }

    @Override
    public void render(PoseStack p_116983_, MultiBufferSource p_116984_, int p_116985_, MiniSusCreeper p_116986_, float p_116987_, float p_116988_, float p_116989_, float p_116990_, float p_116991_, float p_116992_) {
        RenderType renderType = RenderType.entityCutoutNoCull(EYES_LOCATION_TOTAL);
        if(p_116986_.level.getDayTime() >= 13000 || p_116986_.getLightLevelDependentMagicValue() <= 0.5f) renderType = RenderType.eyes(EYES_LOCATION_LIGHT);
        if(p_116986_.isLocked()) renderType = RenderType.eyes(EYES_LOCATION_WARNING) ;

        VertexConsumer vertexconsumer = p_116984_.getBuffer(renderType);
        this.getParentModel().renderToBuffer(p_116983_, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public RenderType renderType() {
        return null;
    }
}
