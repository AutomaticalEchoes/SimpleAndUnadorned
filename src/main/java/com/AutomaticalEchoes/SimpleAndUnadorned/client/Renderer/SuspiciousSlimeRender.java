package com.AutomaticalEchoes.SimpleAndUnadorned.client.Renderer;

import com.AutomaticalEchoes.SimpleAndUnadorned.SimpleAndUnadorned;
import com.AutomaticalEchoes.SimpleAndUnadorned.client.model.SusSlimeModel;
import com.AutomaticalEchoes.SimpleAndUnadorned.client.model.SusSlimeOuterLayer;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousSlime.SuspiciousSlime;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SuspiciousSlimeRender extends MobRenderer<SuspiciousSlime, SusSlimeModel<SuspiciousSlime>> {
    private static final ResourceLocation SLIME_LOCATION = new ResourceLocation(SimpleAndUnadorned.MODID ,"textures/model/entity/sus_slime/slime.png");
    private final ItemRenderer itemRenderer;
    public SuspiciousSlimeRender(EntityRendererProvider.Context p_174391_) {
        super(p_174391_, new SusSlimeModel<>(p_174391_.bakeLayer(SusSlimeModel.LAYER_LOCATION_SLIME)), 0.25F);
        this.addLayer(new SusSlimeOuterLayer<>(this, p_174391_.getModelSet()));
        this.itemRenderer = p_174391_.getItemRenderer();
    }

    public void render(SuspiciousSlime p_115976_, float p_115977_, float p_115978_, PoseStack p_115979_, MultiBufferSource p_115980_, int p_115981_) {
        this.shadowRadius = 0.25F * (float)p_115976_.getSize();
        super.render(p_115976_, p_115977_, p_115978_, p_115979_, p_115980_, p_115981_);
//        if (!p_115976_.isCarryItemEmpty()) {
//            p_115979_.pushPose();
//
////            float f7 = this.getBob(p_115976_, p_115978_);
//            float f = Mth.rotLerp(p_115978_, p_115976_.yBodyRotO,p_115976_.yBodyRot);
//            p_115979_.mulPose(Vector3f.YP.rotationDegrees(180.0F - f));
//
//            float f2 = Mth.lerp(p_115981_, p_115976_.oSquish, p_115976_.squish) / ( 0.5F + 1.0F);
//            float f3 = 1.0F / (f2 + 1.0F);
//            p_115979_.scale(1.0F / f3 , f3 , 1.0F / f3 );
//            this.renderItem(p_115976_, p_115976_.getCarriedItem(), ItemTransforms.TransformType.HEAD,p_115979_, p_115980_,p_115981_);
//            p_115979_.popPose();
//        }
    }

    protected void scale(SuspiciousSlime p_115983_, PoseStack p_115984_, float p_115985_) {
        float f = 0.999F;
        p_115984_.scale(0.999F, 0.999F, 0.999F);
        p_115984_.translate(0.0D, (double)0.001F, 0.0D);
        float f1 = (float)p_115983_.getSize();
        float f2 = Mth.lerp(p_115985_, p_115983_.oSquish, p_115983_.squish) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        p_115984_.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
    }

    public ResourceLocation getTextureLocation(SuspiciousSlime p_115974_) {
        return SLIME_LOCATION;
    }

    protected void renderItem(LivingEntity p_117185_, ItemStack p_117186_, ItemTransforms.TransformType p_117187_, PoseStack p_117189_, MultiBufferSource p_117190_, int p_117191_) {
        if (!p_117186_.isEmpty()) {
            p_117189_.translate(0,0.75,0);
            this.itemRenderer.renderStatic(p_117185_, p_117186_, p_117187_, false, p_117189_, p_117190_,p_117185_.level, p_117191_, LivingEntityRenderer.getOverlayCoords(p_117185_,0.0F),p_117185_.getId());
        }
    }
}
