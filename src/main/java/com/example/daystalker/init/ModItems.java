package com.example.daystalker.init;

import com.example.daystalker.DaystalkerMod;

import com.example.daystalker.item.LavaSwordItem;
import com.example.daystalker.item.ChickenSwordItem;
import com.example.daystalker.item.HornItem;
import com.example.daystalker.item.LavaTier;
import com.example.daystalker.item.RoyalCrownItem;
import com.example.daystalker.item.VikingHelmetItem;
import com.example.daystalker.item.VikingBootsItem;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DaystalkerMod.MOD_ID);

    public static final RegistryObject<LavaSwordItem> LAVA_SWORD =
            ITEMS.register("lava_sword",
                    () -> new LavaSwordItem(
                            LavaTier.INSTANCE,
                            3,      // +3 attack damage on top of tier bonus = 7 total (like diamond sword)
                            -2.4F,  // Standard sword attack speed
                            new Item.Properties()
                    ));

    public static final RegistryObject<ChickenSwordItem> CHICKEN_SWORD =
            ITEMS.register("chicken_sword",
                    () -> new ChickenSwordItem(
                            LavaTier.INSTANCE,
                            3,      // +3 attack damage on top of tier bonus = 7 total (like diamond sword)
                            -2.4F,  // Standard sword attack speed
                            new Item.Properties()
                    ));

    
    public static final RegistryObject<RoyalCrownItem> ROYAL_CROWN =
            ITEMS.register("royal_crown", RoyalCrownItem::new);

    public static final RegistryObject<VikingHelmetItem> VIKING_HELMET =
            ITEMS.register("viking_helmet", VikingHelmetItem::new);

    public static final RegistryObject<HornItem> HORN =
            ITEMS.register("horn", HornItem::new);

    public static final RegistryObject<VikingBootsItem> VIKING_BOOTS =
            ITEMS.register("viking_boots", VikingBootsItem::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
