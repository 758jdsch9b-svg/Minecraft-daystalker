package com.example.daystalker.entity.client;

import com.example.daystalker.DaystalkerMod;
import com.example.daystalker.entity.DragonEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DragonRenderer extends MobRenderer<DragonEntity, DragonModel> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(DaystalkerMod.MOD_ID, "textures/entity/dragon.png");

    public DragonRenderer(EntityRendererProvider.Context context) {
        super(context, new DragonModel(context.bakeLayer(ModDragonModelLayer.DRAGON)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(DragonEntity entity) {
        return TEXTURE;
    }

    @Override
    protected void scale(DragonEntity entity, com.mojang.blaze3d.vertex.PoseStack poseStack, float partialTickTime) {
        float scale = 1.0F;
        poseStack.scale(scale, scale, scale);
    }
}
