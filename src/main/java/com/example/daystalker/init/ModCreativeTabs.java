package com.example.daystalker.init;

import com.example.daystalker.DaystalkerMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DaystalkerMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> DAYSTALKER_TAB =
            CREATIVE_MODE_TABS.register("daystalker_tab",
                    () -> CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.daystalker"))
                            .icon(() -> new ItemStack(ModItems.ROYAL_CROWN.get()))
                            .displayItems((parameters, output) -> {
                                output.accept(ModItems.LAVA_SWORD.get());
                                output.accept(ModItems.CHICKEN_SWORD.get());
                                output.accept(ModItems.ROYAL_CROWN.get());
                                output.accept(ModItems.VIKING_HELMET.get());
                                output.accept(ModItems.HORN.get());
                                output.accept(ModItems.VIKING_BOOTS.get());
                            })
                            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
