package com.AutomaticalEchoes.SimpleAndUnadorned.client.Layer;

import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.SuspiciousEnderman.SuspiciousEnderman;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ItemLayer<T extends LivingEntity, M extends EntityModel<T> & ArmedModel> extends RenderLayer<T, M> {
    private final ItemInHandRenderer itemInHandRenderer;
    public ItemLayer(RenderLayerParent<T, M> p_117346_, ItemInHandRenderer p_234847_) {
        super(p_117346_);
        this.itemInHandRenderer = p_234847_;
    }

    public void render(PoseStack p_117204_, MultiBufferSource p_117205_, int p_117206_, T p_117207_, float p_117208_, float p_117209_, float p_117210_, float p_117211_, float p_117212_, float p_117213_) {
        if (p_117207_ instanceof SuspiciousEnderman enderman && !enderman.isCarryItemEmpty()) {
            p_117204_.pushPose();
            if (this.getParentModel().young) {
                float f = 0.5F;
                p_117204_.translate(0.0D, 0.75D, 0.0D);
                p_117204_.scale(0.5F, 0.5F, 0.5F);
            }
            this.renderArmWithItem(p_117207_, enderman.getCarriedItem(), ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, p_117204_, p_117205_, p_117206_);
            p_117204_.popPose();
        }
    }

    protected void renderArmWithItem(LivingEntity p_117185_, ItemStack p_117186_, ItemTransforms.TransformType p_117187_, HumanoidArm p_117188_, PoseStack p_117189_, MultiBufferSource p_117190_, int p_117191_) {
        if (!p_117186_.isEmpty()) {
            p_117189_.pushPose();
            this.getParentModel().translateToHand(p_117188_, p_117189_);
            p_117189_.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
            p_117189_.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            boolean flag = p_117188_ == HumanoidArm.LEFT;
//            p_117189_.translate((double)((float)(flag ? -1 : 1) / 16.0F), 0.125D, -0.625D);
            p_117189_.translate(0, 0.125D, -1.725D);
            this.itemInHandRenderer.renderItem(p_117185_, p_117186_, p_117187_, flag, p_117189_, p_117190_, p_117191_);
            p_117189_.popPose();
        }
    }
}
