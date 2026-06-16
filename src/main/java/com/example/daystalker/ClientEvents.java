package com.example.daystalker;

import com.example.daystalker.entity.client.DaystalkerRenderer;
import com.example.daystalker.entity.client.DragonModel;
import com.example.daystalker.entity.client.DragonRenderer;
import com.example.daystalker.entity.client.ModDragonModelLayer;
import com.example.daystalker.init.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DaystalkerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.DAYSTALKER.get(), DaystalkerRenderer::new);
        event.registerEntityRenderer(ModEntities.DRAGON.get(), DragonRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModDragonModelLayer.DRAGON, DragonModel::createBodyLayer);
    }
}
