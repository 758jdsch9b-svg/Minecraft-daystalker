package com.example.daystalker.entity.client;

import com.example.daystalker.DaystalkerMod;
import com.example.daystalker.entity.DaystalkerEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renderer for Daystalker.
 * Uses the vanilla Zombie body model (HumanoidModel) as a placeholder.
 * Replace the TEXTURE path with your own texture in:
 *   assets/daystalker/textures/entity/daystalker.png
 */
public class DaystalkerRenderer extends HumanoidMobRenderer<DaystalkerEntity, HumanoidModel<DaystalkerEntity>> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(DaystalkerMod.MOD_ID, "textures/entity/daystalker.png");

    public DaystalkerRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.ZOMBIE)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(DaystalkerEntity entity) {
        return TEXTURE;
    }
}
