package com.example.daystalker.worldgen;

import com.example.daystalker.DaystalkerMod;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, DaystalkerMod.MOD_ID);

    // Existing bamboo tower — untouched
    public static final RegistryObject<BambooTowerFeature> BAMBOO_TOWER =
            FEATURES.register("bamboo_tower",
                    () -> new BambooTowerFeature(NoneFeatureConfiguration.CODEC));

    // Generic NBT building feature — use this for any .nbt structure
    public static final RegistryObject<BuildingFeature> BUILDING =
            FEATURES.register("building",
                    () -> new BuildingFeature(BuildingFeatureConfig.CODEC));

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}