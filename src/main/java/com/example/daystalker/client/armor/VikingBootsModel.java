package com.example.daystalker.client.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

/**
 * Viking Boots armor model.
 * Renders boots with a distinctive Viking design.
 * Texture: 64x32 (viking_layer_2.png)
 */
public class VikingBootsModel extends HumanoidModel<LivingEntity> {

    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart rightBoot;
    private final ModelPart leftBoot;

    public VikingBootsModel(ModelPart root) {
        super(root);
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
        this.rightLeg.skipDraw = true;
        this.leftLeg.skipDraw = true;
        this.rightBoot = this.rightLeg.getChild("right_boot");
        this.leftBoot = this.leftLeg.getChild("left_boot");
        setBootsVisible(false);
    }

    public void setBootsVisible(boolean visible) {
        this.rightLeg.visible = visible;
        this.leftLeg.visible = visible;
        this.rightBoot.visible = visible;
        this.leftBoot.visible = visible;
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition root = mesh.getRoot();

        // Attach boot parts to the existing leg parts so they follow leg transforms
        PartDefinition rightLeg = root.getChild("right_leg");
        PartDefinition leftLeg = root.getChild("left_leg");

        // Right boot - positioned below the leg so it appears on the foot
        rightLeg.addOrReplaceChild("right_boot",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F,
                    new CubeDeformation(0.6F)),
            PartPose.offset(0.0F, 12.0F, 0.0F));

        // Left boot - mirrored on the left leg and positioned below it
        leftLeg.addOrReplaceChild("left_boot",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F,
                    new CubeDeformation(0.6F)),
            PartPose.offset(0.0F, 12.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        // Render only the leg parts and their children (boots) so the model
        // follows the existing leg transforms instead of the body transform.
        this.rightLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.leftLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
