package com.AutomaticalEchoes.SimpleAndUnadorned.client.Renderer;

import com.AutomaticalEchoes.SimpleAndUnadorned.SimpleAndUnadorned;
import com.AutomaticalEchoes.SimpleAndUnadorned.client.Layer.MiniCreeperEyes;
import com.AutomaticalEchoes.SimpleAndUnadorned.client.model.ICreeperModel;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.MiniSusCreeper.MiniSusCreeper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class MiniSusCreeperRender extends MobRenderer<MiniSusCreeper, ICreeperModel<MiniSusCreeper>> {
    private static final ResourceLocation BOOMER_LOCATION = new ResourceLocation(SimpleAndUnadorned.MODID,"textures/model/entity/mini_sus_creeper/creeper.png");

    public MiniSusCreeperRender(EntityRendererProvider.Context p_173958_) {
        super(p_173958_, new ICreeperModel<>(p_173958_.bakeLayer(ICreeperModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(new MiniCreeperEyes(this));
    }

    @Override
    protected void setupRotations(MiniSusCreeper p_115317_, PoseStack p_115318_, float p_115319_, float p_115320_, float p_115321_) {
        Direction attachFace = p_115317_.getAttachFace();
        p_115318_.translate(0.0, 0.5F,0.0);
        p_115318_.mulPose(attachFace.getOpposite().getRotation());
        p_115318_.translate(0.0, -0.5F,0.0);
        float yRot = switch (attachFace) {
            case UP, EAST, NORTH -> p_115320_;
            case WEST, SOUTH -> -p_115320_;
            default -> 180.0F - p_115320_;
        };

        p_115318_.mulPose(Vector3f.YP.rotationDegrees(yRot));

        if (p_115317_.deathTime > 0) {
            float f = ((float)p_115317_.deathTime + p_115321_ - 1.0F) / 20.0F * 1.6F;
            f = Mth.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }
            p_115318_.mulPose(Vector3f.ZP.rotationDegrees(f * this.getFlipDegrees(p_115317_)));
        }


    }

    protected void scale(MiniSusCreeper p_114046_, PoseStack p_114047_, float p_114048_) {
        p_114047_.scale(0.6F, 0.6F, 0.6F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull MiniSusCreeper p_114482_) {
        return BOOMER_LOCATION;
    }
}
