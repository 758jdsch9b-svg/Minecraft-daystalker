package com.example.daystalker.command;

import com.example.daystalker.worldgen.BuildingFeature;
import com.example.daystalker.worldgen.BuildingFeatureConfig;
import com.example.daystalker.worldgen.ModFeatures;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber
public class DebugCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        // /spawnbuilding <structure>
        // Example: /spawnbuilding daystalker:lava_tower
        dispatcher.register(
            Commands.literal("spawnbuilding")
                .requires(src -> src.hasPermission(2))
                .then(Commands.argument("structure",
                        net.minecraft.commands.arguments.ResourceLocationArgument.id())
                    .executes(ctx -> {
                        ResourceLocation structure = net.minecraft.commands.arguments.ResourceLocationArgument.getId(ctx, "structure");
                        CommandSourceStack source = ctx.getSource();
                        ServerLevel level = source.getLevel();
                        BlockPos pos = BlockPos.containing(source.getPosition());

                        BuildingFeatureConfig config = new BuildingFeatureConfig(structure, false, 0);

                        try {
                            boolean result = ModFeatures.BUILDING.get().place(
                                new FeaturePlaceContext<>(
                                    Optional.empty(),
                                    level,
                                    level.getChunkSource().getGenerator(),
                                    level.getRandom(),
                                    pos,
                                    config
                                )
                            );

                            if (result) {
                                source.sendSuccess(() -> Component.literal("Placed structure: " + structure), false);
                            } else {
                                source.sendFailure(Component.literal("Failed to place: " + structure + " (check log for details)"));
                            }
                        } catch (Exception e) {
                            source.sendFailure(Component.literal("Error: " + e.getMessage()));
                            e.printStackTrace();
                        }

                        return 1;
                    }))
        );
    }
}
