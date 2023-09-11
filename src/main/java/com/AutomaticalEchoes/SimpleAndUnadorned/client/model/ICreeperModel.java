package com.AutomaticalEchoes.SimpleAndUnadorned.client.model;

import com.AutomaticalEchoes.SimpleAndUnadorned.SimpleAndUnadorned;
import com.AutomaticalEchoes.SimpleAndUnadorned.common.livingEntity.MiniSusCreeper.MiniSusCreeper;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ICreeperModel <T extends MiniSusCreeper> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(SimpleAndUnadorned.MODID, "mini_creeper"), "main");
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;
    private static final int Y_OFFSET = 6;

    public ICreeperModel(ModelPart p_170524_) {
        this.root = p_170524_;
        this.head = p_170524_.getChild("head");
        this.leftHindLeg = p_170524_.getChild("right_hind_leg");
        this.rightHindLeg = p_170524_.getChild("left_hind_leg");
        this.leftFrontLeg = p_170524_.getChild("right_front_leg");
        this.rightFrontLeg = p_170524_.getChild("left_front_leg");
    }

    public static LayerDefinition createBodyLayer() {
        CubeDeformation p_170526_ = CubeDeformation.NONE;
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, p_170526_), PartPose.offset(0.0F, 6.0F, 0.0F));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, p_170526_), PartPose.offset(0.0F, 6.0F, 0.0F));
        CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, p_170526_);
        partdefinition.addOrReplaceChild("right_hind_leg", cubelistbuilder, PartPose.offset(-2.0F, 18.0F, 4.0F));
        partdefinition.addOrReplaceChild("left_hind_leg", cubelistbuilder, PartPose.offset(2.0F, 18.0F, 4.0F));
        partdefinition.addOrReplaceChild("right_front_leg", cubelistbuilder, PartPose.offset(-2.0F, 18.0F, -4.0F));
        partdefinition.addOrReplaceChild("left_front_leg", cubelistbuilder, PartPose.offset(2.0F, 18.0F, -4.0F));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    public ModelPart root() {
        return this.root;
    }

    public void setupAnim(T p_102463_, float p_102464_, float p_102465_, float p_102466_, float p_102467_, float p_102468_) {
        Direction attachFace = p_102463_.getAttachFace();
        float xHeadRot = switch (attachFace) {
            case SOUTH, EAST, UP -> -p_102468_;
            default -> p_102468_;
        };
        float yHeadRot = p_102467_;
        this.head.yRot = yHeadRot * ((float)Math.PI / 180F);
        this.head.xRot = xHeadRot * ((float)Math.PI / 180F);
        this.rightHindLeg.xRot = Mth.cos(p_102464_ * 0.6662F) * 1.4F * p_102465_;
        this.leftHindLeg.xRot = Mth.cos(p_102464_ * 0.6662F + (float)Math.PI) * 1.4F * p_102465_;
        this.rightFrontLeg.xRot = Mth.cos(p_102464_ * 0.6662F + (float)Math.PI) * 1.4F * p_102465_;
        this.leftFrontLeg.xRot = Mth.cos(p_102464_ * 0.6662F) * 1.4F * p_102465_;
    }
}
