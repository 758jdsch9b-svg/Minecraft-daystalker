package com.example.daystalker.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BambooTowerFeature extends Feature<NoneFeatureConfiguration> {

    public BambooTowerFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        // Find solid ground
        BlockPos ground = findGround(level, origin);
        if (ground == null) return false;

        int towerHeight = 10 + random.nextInt(6); // 10-15 blocks tall

        buildTower(level, ground, towerHeight, random);
        return true;
    }

    private BlockPos findGround(WorldGenLevel level, BlockPos origin) {
        BlockPos pos = origin.above(10);
        for (int i = 0; i < 20; i++) {
            pos = pos.below();
            if (!level.getBlockState(pos).isAir() && level.getBlockState(pos.above()).isAir()) {
                return pos;
            }
        }
        return null;
    }

    private void buildTower(WorldGenLevel level, BlockPos ground, int height, RandomSource random) {
        BlockState bambooBlock  = Blocks.BAMBOO_BLOCK.defaultBlockState();
        BlockState bambooPlank  = Blocks.BAMBOO_PLANKS.defaultBlockState();
        BlockState bambooSlab   = Blocks.BAMBOO_SLAB.defaultBlockState();
        BlockState bambooFence  = Blocks.BAMBOO_FENCE.defaultBlockState();
        BlockState bambooStair  = Blocks.BAMBOO_STAIRS.defaultBlockState()
                .setValue(StairBlock.FACING, Direction.NORTH)
                .setValue(StairBlock.HALF, Half.BOTTOM)
                .setValue(StairBlock.SHAPE, StairsShape.STRAIGHT);
        BlockState bambooLeaves = Blocks.JUNGLE_LEAVES.defaultBlockState();
        BlockState air          = Blocks.AIR.defaultBlockState();

        // ---- Foundation (3x3 bamboo planks) ----
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                setBlock(level, ground.offset(x, 0, z), bambooPlank);
            }
        }

        // ---- Tower walls (hollow 3x3 cross-section) ----
        for (int y = 1; y <= height; y++) {
            BlockPos base = ground.above(y);
            // Four corner pillars
            setBlock(level, base.offset(-1, 0, -1), bambooBlock);
            setBlock(level, base.offset(1, 0, -1), bambooBlock);
            setBlock(level, base.offset(-1, 0, 1), bambooBlock);
            setBlock(level, base.offset(1, 0, 1), bambooBlock);

            // Fill walls every 3 levels for detail
            if (y % 3 == 0) {
                for (int x = -1; x <= 1; x++) {
                    setBlock(level, base.offset(x, 0, -1), bambooBlock);
                    setBlock(level, base.offset(x, 0, 1), bambooBlock);
                    setBlock(level, base.offset(-1, 0, x), bambooBlock);
                    setBlock(level, base.offset(1, 0, x), bambooBlock);
                }
            }

            // Add a floor platform every 4 levels (intermediate floor)
            if (y % 4 == 0 && y < height) {
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        setBlock(level, base.offset(x, 0, z), bambooPlank);
                    }
                }
                // Hollow out the center
                setBlock(level, base, air);
            }
        }

        // ---- Top floor / roof platform ----
        int top = height + 1;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                setBlock(level, ground.above(top), bambooPlank);
                setBlock(level, ground.above(top).offset(x, 0, z), bambooPlank);
            }
        }

        // Fence railing around top
        Direction[] dirs = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
        for (Direction dir : dirs) {
            BlockPos fencePos = ground.above(top + 1).relative(dir);
            setBlock(level, fencePos, bambooFence);
            // Corner fences
        }
        setBlock(level, ground.above(top + 1).offset(-1, 0, -1), bambooFence);
        setBlock(level, ground.above(top + 1).offset(1, 0, -1), bambooFence);
        setBlock(level, ground.above(top + 1).offset(-1, 0, 1), bambooFence);
        setBlock(level, ground.above(top + 1).offset(1, 0, 1), bambooFence);

        // ---- Decorative roof cap (bamboo + leaves) ----
        setBlock(level, ground.above(top + 2), bambooBlock);
        setBlock(level, ground.above(top + 3), bambooBlock);
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (Math.abs(x) + Math.abs(z) <= 3) {
                    setBlock(level, ground.above(top + 2).offset(x, 0, z), bambooLeaves);
                }
            }
        }
        // Overwrite the center pillar so it sticks through
        setBlock(level, ground.above(top + 2), bambooBlock);
        setBlock(level, ground.above(top + 3), bambooBlock);

        // ---- Entrance (clear a 2-block doorway on south side) ----
        setBlock(level, ground.above(1).south(), air);
        setBlock(level, ground.above(2).south(), air);
    }

    private void setBlock(WorldGenLevel level, BlockPos pos, BlockState state) {
        level.setBlock(pos, state, 2);
    }
}
