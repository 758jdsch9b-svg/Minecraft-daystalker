package com.example.daystalker.client.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

/**
 * Viking Helmet armor model.
 * Renders a helmet with two curved horns on the sides.
 * Texture: 64x32 (viking_layer_1.png)
 */
public class VikingHelmetModel extends HumanoidModel<LivingEntity> {

    private final ModelPart helmBase;
    private final ModelPart hornLeft;
    private final ModelPart hornRight;
    private final ModelPart noseGuard;

    public VikingHelmetModel(ModelPart root) {
        super(root);
        ModelPart head = root.getChild("head");
        this.helmBase  = head.getChild("helm_base");
        this.hornLeft  = head.getChild("horn_left");
        this.hornRight = head.getChild("horn_right");
        this.noseGuard = head.getChild("nose_guard");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.getChild("head");

        // Helmet base - slightly larger than head
        head.addOrReplaceChild("helm_base",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.5F, -8.5F, -4.5F, 9.0F, 9.0F, 9.0F,
                                new CubeDeformation(0.6F)),
                PartPose.ZERO);

        // Left horn - angled outward and upward
        head.addOrReplaceChild("horn_left",
                CubeListBuilder.create()
                        .texOffs(0, 18)
                        // Lower horn segment
                        .addBox(-7.0F, -9.0F, -1.0F, 3.0F, 3.0F, 2.0F,
                                new CubeDeformation(0.0F))
                        // Upper horn segment (curves up)
                        .texOffs(10, 18)
                        .addBox(-8.0F, -13.0F, -0.5F, 2.0F, 4.0F, 1.0F,
                                new CubeDeformation(0.0F)),
                PartPose.ZERO);

        // Right horn - mirrored
        head.addOrReplaceChild("horn_right",
                CubeListBuilder.create()
                        .texOffs(0, 18)
                        .addBox(4.0F, -9.0F, -1.0F, 3.0F, 3.0F, 2.0F,
                                new CubeDeformation(0.0F))
                        .texOffs(10, 18)
                        .addBox(6.0F, -13.0F, -0.5F, 2.0F, 4.0F, 1.0F,
                                new CubeDeformation(0.0F)),
                PartPose.ZERO);

        // Nose guard - small strip down the front
        head.addOrReplaceChild("nose_guard",
                CubeListBuilder.create()
                        .texOffs(20, 18)
                        .addBox(-0.5F, -5.0F, -5.2F, 1.0F, 4.0F, 1.0F,
                                new CubeDeformation(0.0F)),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
