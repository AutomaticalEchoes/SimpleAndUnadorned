package com.AutomaticalEchoes.SimpleAndUnadorned.client.Layer;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousCreeper.SuspiciousCreeper;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PowerLayer extends EnergySwirlLayer<SuspiciousCreeper, CreeperModel<SuspiciousCreeper>> {
    private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final CreeperModel<SuspiciousCreeper> model;

    public PowerLayer(RenderLayerParent<SuspiciousCreeper, CreeperModel<SuspiciousCreeper>> p_174471_, EntityModelSet p_174472_) {
        super(p_174471_);
        this.model = new CreeperModel<>(p_174472_.bakeLayer(ModelLayers.CREEPER_ARMOR));
    }

    protected float xOffset(float p_116683_) {
        return p_116683_ * 0.01F;
    }

    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    protected EntityModel<SuspiciousCreeper> model() {
        return this.model;
    }
}
