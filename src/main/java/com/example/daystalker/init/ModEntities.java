package com.example.daystalker.init;

import com.example.daystalker.DaystalkerMod;
import com.example.daystalker.entity.DaystalkerEntity;
import com.example.daystalker.entity.DragonEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DaystalkerMod.MOD_ID);

    public static final RegistryObject<EntityType<DaystalkerEntity>> DAYSTALKER =
            ENTITY_TYPES.register("daystalker",
                    () -> EntityType.Builder.<DaystalkerEntity>of(DaystalkerEntity::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.95f)  // Same size as zombie
                            .build("daystalker"));

    public static final RegistryObject<EntityType<DragonEntity>> DRAGON =
            ENTITY_TYPES.register("dragon",
                    () -> EntityType.Builder.<DragonEntity>of(DragonEntity::new, MobCategory.CREATURE)
                            .sized(1.5f, 1.8f)
                            .clientTrackingRange(10)
                            .build("dragon"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
