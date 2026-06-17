package com.example.daystalker;

import com.example.daystalker.init.ModEntities;
import com.example.daystalker.worldgen.ModFeatures;
import com.example.daystalker.worldgen.WorldGenEvents;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DaystalkerMod.MOD_ID)
public class DaystalkerMod {

    public static final String MOD_ID = "daystalker";

    public DaystalkerMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEntities.register(modEventBus);
        ModFeatures.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onAttributeCreate);

        WorldGenEvents.register();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            SpawnPlacements.register(
                ModEntities.DAYSTALKER.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules
            );
            SpawnPlacements.register(
                ModEntities.DRAGON.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) -> true
            );
        });
    }

    private void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(ModEntities.DAYSTALKER.get(),
                com.example.daystalker.entity.DaystalkerEntity.createAttributes().build());
        event.put(ModEntities.DRAGON.get(),
                com.example.daystalker.entity.DragonEntity.createAttributes().build());
    }
}
