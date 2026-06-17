package com.example.daystalker.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Optional;

/**
 * A general-purpose feature that places an NBT structure in the world.
 *
 * Usage — register a new feature with this class and pass a config:
 *
 *   new BuildingFeature(BuildingFeatureConfig.CODEC)
 *
 * Config parameters:
 *   - structure:      ResourceLocation of the .nbt file
 *                     e.g. "daystalker:bamboo_tower"
 *                     → src/main/resources/data/daystalker/structures/bamboo_tower.nbt
 *   - random_rotation: if true, randomly rotates the structure (0/90/180/270°)
 *   - offset_y:       vertical offset in blocks (positive = higher up, negative = underground)
 *
 * Example configured_feature JSON:
 * {
 *   "type": "daystalker:building",
 *   "config": {
 *     "structure": "daystalker:bamboo_tower",
 *     "random_rotation": true,
 *     "offset_y": 0
 *   }
 * }
 */
public class BuildingFeature extends Feature<BuildingFeatureConfig> {

    private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
    
    public BuildingFeature(Codec<BuildingFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BuildingFeatureConfig> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        BuildingFeatureConfig config = context.config();

        LOGGER.info("[BuildingFeature] Attempting to place: {}", config.structure());

        // Find ground level
        BlockPos ground = findGround(level, origin);
        if (ground == null) {
            LOGGER.info("[BuildingFeature] Could not find ground at {}", origin);
            return false;
        }

        LOGGER.info("[BuildingFeature] Ground found at {}", ground);

        // Apply vertical offset from config
        ground = ground.above(config.offsetY());

        // Load the structure template
        StructureTemplateManager templateManager = level.getLevel().getStructureManager();
        Optional<StructureTemplate> templateOpt = templateManager.get(config.structure());

        if (templateOpt.isEmpty()) {
            LOGGER.warn("[BuildingFeature] Could not find structure: {}", config.structure());
            return false;
        }

        LOGGER.info("[BuildingFeature] Structure loaded, placing at {}", ground);

        StructureTemplate template = templateOpt.get();

        // Pick rotation
        Rotation rotation = config.randomRotation()
                ? Rotation.values()[random.nextInt(4)]
                : Rotation.NONE;

        // Center the structure on the origin
        Vec3i size = template.getSize(rotation);
        BlockPos offset = new BlockPos(-(size.getX() / 2), 0, -(size.getZ() / 2));
        BlockPos placePos = ground.offset(offset);

        // Place settings
        StructurePlaceSettings settings = new StructurePlaceSettings()
                .setRotation(rotation)
                .setMirror(Mirror.NONE)
                .setIgnoreEntities(false);

        // Place the structure
        template.placeInWorld(level, placePos, placePos, settings, random, 2);

        LOGGER.info("[BuildingFeature] Successfully placed at {}", placePos);
        return true;
    }

    /**
     * Walks downward from origin to find the first solid ground block.
     */
    private BlockPos findGround(WorldGenLevel level, BlockPos origin) {
        // Start from y=320 and search all the way down to y=0
        BlockPos pos = new BlockPos(origin.getX(), 320, origin.getZ());
        for (int i = 0; i < 300; i++) {
            pos = pos.below();
            if (pos.getY() <= 0) return null;
            if (!level.getBlockState(pos).isAir()
                    && level.getBlockState(pos.above()).isAir()) {
                return pos;
            }
        }
        return null;
    }
}
