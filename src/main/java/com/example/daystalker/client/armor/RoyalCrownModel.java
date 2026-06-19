package com.example.daystalker.client.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

/**
 * Royal Crown armor model.
 * Renders a crown with 3 spikes and gem accents on top of the player's head.
 * Texture: 64x32 (royal_layer_1.png)
 */
public class RoyalCrownModel extends HumanoidModel<LivingEntity> {

    private final ModelPart crownBase;
    private final ModelPart spikeLeft;
    private final ModelPart spikeMiddle;
    private final ModelPart spikeRight;

    public RoyalCrownModel(ModelPart root) {
        super(root);
        ModelPart head = root.getChild("head");
        this.crownBase   = head.getChild("crown_base");
        this.spikeLeft   = head.getChild("spike_left");
        this.spikeMiddle = head.getChild("spike_middle");
        this.spikeRight  = head.getChild("spike_right");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.getChild("head");

        // Crown band - thin ring around the base of the head
        head.addOrReplaceChild("crown_base",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.5F, -8.8F, -4.5F, 9.0F, 2.0F, 9.0F,
                                new CubeDeformation(0.3F)),
                PartPose.ZERO);

        // Left spike
        head.addOrReplaceChild("spike_left",
                CubeListBuilder.create()
                        .texOffs(0, 11)
                        .addBox(-3.5F, -12.0F, -1.0F, 2.0F, 4.0F, 2.0F,
                                new CubeDeformation(0.0F)),
                PartPose.ZERO);

        // Middle spike (tallest)
        head.addOrReplaceChild("spike_middle",
                CubeListBuilder.create()
                        .texOffs(8, 11)
                        .addBox(-1.0F, -14.0F, -1.0F, 2.0F, 6.0F, 2.0F,
                                new CubeDeformation(0.0F)),
                PartPose.ZERO);

        // Right spike
        head.addOrReplaceChild("spike_right",
                CubeListBuilder.create()
                        .texOffs(16, 11)
                        .addBox(1.5F, -12.0F, -1.0F, 2.0F, 4.0F, 2.0F,
                                new CubeDeformation(0.0F)),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        // Only render the head parts (crown)
        head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
