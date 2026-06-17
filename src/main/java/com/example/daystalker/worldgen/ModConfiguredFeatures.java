package com.example.daystalker.worldgen;

import com.example.daystalker.DaystalkerMod;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> BAMBOO_TOWER_KEY =
            ResourceKey.create(Registries.CONFIGURED_FEATURE,
                    new ResourceLocation(DaystalkerMod.MOD_ID, "bamboo_tower"));

    public static final ResourceKey<PlacedFeature> BAMBOO_TOWER_PLACED_KEY =
            ResourceKey.create(Registries.PLACED_FEATURE,
                    new ResourceLocation(DaystalkerMod.MOD_ID, "bamboo_tower"));

    public static void bootstrapConfigured(BootstapContext<ConfiguredFeature<?, ?>> context) {
        context.register(BAMBOO_TOWER_KEY,
                new ConfiguredFeature<>(ModFeatures.BAMBOO_TOWER.get(), NoneFeatureConfiguration.INSTANCE));
    }

    public static void bootstrapPlaced(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> configured = features.getOrThrow(BAMBOO_TOWER_KEY);

        context.register(BAMBOO_TOWER_PLACED_KEY,
                new PlacedFeature(configured, List.of(
                        RarityFilter.onAverageOnceEvery(180),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING),
                        BiomeFilter.biome()
                )));
    }
}
