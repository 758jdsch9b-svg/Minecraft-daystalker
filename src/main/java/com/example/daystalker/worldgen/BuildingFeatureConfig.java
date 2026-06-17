package com.example.daystalker.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

/**
 * Configuration for BuildingFeature.
 *
 * Parameters:
 *   structure       - ResourceLocation pointing to the .nbt file
 *   random_rotation - whether to randomize rotation (true/false)
 *   offset_y        - vertical offset in blocks (0 = ground level)
 */
public record BuildingFeatureConfig(
        ResourceLocation structure,
        boolean randomRotation,
        int offsetY
) implements FeatureConfiguration {

    public static final Codec<BuildingFeatureConfig> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    ResourceLocation.CODEC
                            .fieldOf("structure")
                            .forGetter(BuildingFeatureConfig::structure),
                    Codec.BOOL
                            .optionalFieldOf("random_rotation", true)
                            .forGetter(BuildingFeatureConfig::randomRotation),
                    Codec.INT
                            .optionalFieldOf("offset_y", 0)
                            .forGetter(BuildingFeatureConfig::offsetY)
            ).apply(instance, BuildingFeatureConfig::new));
}
