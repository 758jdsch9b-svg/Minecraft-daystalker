package com.example.daystalker.entity.client;

import com.example.daystalker.entity.DragonEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

/**
 * Simple low-poly dragon model: body, neck+head, tail, 4 legs, 2 wings.
 * Built entirely from boxes (no Blockbench file needed).
 * Texture: 64x64
 */
public class DragonModel extends EntityModel<DragonEntity> {

    private final ModelPart body;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart tail;
    private final ModelPart legFrontLeft;
    private final ModelPart legFrontRight;
    private final ModelPart legBackLeft;
    private final ModelPart legBackRight;
    private final ModelPart wingLeft;
    private final ModelPart wingRight;

    public DragonModel(ModelPart root) {
        this.body = root.getChild("body");
        this.neck = body.getChild("neck");
        this.head = neck.getChild("head");
        this.tail = body.getChild("tail");
        this.legFrontLeft = body.getChild("leg_front_left");
        this.legFrontRight = body.getChild("leg_front_right");
        this.legBackLeft = body.getChild("leg_back_left");
        this.legBackRight = body.getChild("leg_back_right");
        this.wingLeft = body.getChild("wing_left");
        this.wingRight = body.getChild("wing_right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition body = root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-5.0F, -4.0F, -8.0F, 10.0F, 8.0F, 16.0F),
                PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition neck = body.addOrReplaceChild("neck",
                CubeListBuilder.create()
                        .texOffs(0, 24)
                        .addBox(-2.5F, -2.5F, -7.0F, 5.0F, 5.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, -2.0F, -8.0F, -0.3F, 0.0F, 0.0F));

        neck.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(24, 24)
                        .addBox(-3.0F, -3.0F, -6.0F, 6.0F, 6.0F, 6.0F)
                        // snout
                        .texOffs(24, 36)
                        .addBox(-1.5F, -1.0F, -9.0F, 3.0F, 2.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -7.0F, 0.2F, 0.0F, 0.0F));

        body.addOrReplaceChild("tail",
                CubeListBuilder.create()
                        .texOffs(0, 47)
                        .addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 14.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, 8.0F, 0.15F, 0.0F, 0.0F));

        // Legs
        CubeListBuilder legBuilder = CubeListBuilder.create()
                .texOffs(0, 16)
                .addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F);

        body.addOrReplaceChild("leg_front_left",
                legBuilder, PartPose.offset(4.0F, 4.0F, -5.0F));
        body.addOrReplaceChild("leg_front_right",
                legBuilder, PartPose.offset(-4.0F, 4.0F, -5.0F));
        body.addOrReplaceChild("leg_back_left",
                legBuilder, PartPose.offset(4.0F, 4.0F, 5.0F));
        body.addOrReplaceChild("leg_back_right",
                legBuilder, PartPose.offset(-4.0F, 4.0F, 5.0F));

        // Wings - flat boxes
        CubeListBuilder wingBuilder = CubeListBuilder.create()
                .texOffs(0, 40)
                .addBox(0.0F, -1.0F, -8.0F, 1.0F, 1.0F, 16.0F)
                .texOffs(0, 57)
                .addBox(0.0F, 0.0F, -8.0F, 1.0F, 0.0F, 16.0F);

        body.addOrReplaceChild("wing_left",
                wingBuilder, PartPose.offsetAndRotation(-5.0F, -3.0F, -2.0F, 0.0F, 0.5F, 0.0F));
        body.addOrReplaceChild("wing_right",
                wingBuilder, PartPose.offsetAndRotation(5.0F, -3.0F, -2.0F, 0.0F, -0.5F, (float) Math.PI));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(DragonEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Head/neck look
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = headPitch * ((float) Math.PI / 180F);

        boolean flying = entity.isFlyingMount();

        if (flying) {
            // Flap wings
            float flap = Mth.sin(ageInTicks * 1.2F) * 0.8F;
            this.wingLeft.zRot = 0.5F + flap;
            this.wingRight.zRot = (float) -Math.PI - 0.5F - flap * -1.0F;
            this.wingRight.zRot = -0.5F - flap + (float) Math.PI;

            // Tuck legs while flying
            this.legFrontLeft.xRot = -0.5F;
            this.legFrontRight.xRot = -0.5F;
            this.legBackLeft.xRot = -0.5F;
            this.legBackRight.xRot = -0.5F;

            // Tail sway
            this.tail.yRot = Mth.sin(ageInTicks * 0.3F) * 0.2F;
        } else {
            // Idle wings folded
            this.wingLeft.zRot = 0.4F;
            this.wingRight.zRot = (float) Math.PI - 0.4F;

            // Walking animation
            this.legFrontLeft.xRot = Mth.cos(limbSwing * 0.6662F) * 1.0F * limbSwingAmount;
            this.legFrontRight.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.0F * limbSwingAmount;
            this.legBackLeft.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.0F * limbSwingAmount;
            this.legBackRight.xRot = Mth.cos(limbSwing * 0.6662F) * 1.0F * limbSwingAmount;

            this.tail.yRot = Mth.sin(ageInTicks * 0.1F) * 0.1F;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
