package com.example.daystalker.worldgen;

import com.example.daystalker.DaystalkerMod;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

// In Forge 1.20.1, BiomeLoadingEvent is gone.
// We use BiomeModifiers via JSON datagen OR the newer forge biome modifier system.
// Simplest approach: register via the forge biome modifier registry in JSON.
// This class is intentionally left minimal — see resources/data for the JSON approach.
public class WorldGenEvents {
    public static void register() {
        // Biome modification is handled via data/daystalker/forge/biome_modifier/bamboo_tower.json
    }
}
