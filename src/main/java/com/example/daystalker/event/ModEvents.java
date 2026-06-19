package com.example.daystalker.event;

import com.example.daystalker.DaystalkerMod;
import com.example.daystalker.init.ModItems;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.entity.item.ItemEntity;

@Mod.EventBusSubscriber(modid = DaystalkerMod.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onCowDeath(LivingDropsEvent event) {
        if (event.getEntity() instanceof Cow || event.getEntity() instanceof MushroomCow) {
            // Drop 1-2 horns
            int count = 1 + event.getEntity().getRandom().nextInt(2);
            ItemStack horns = new ItemStack(ModItems.HORN.get(), count);
            ItemEntity itemEntity = new ItemEntity(
                    event.getEntity().level(),
                    event.getEntity().getX(),
                    event.getEntity().getY(),
                    event.getEntity().getZ(),
                    horns
            );
            event.getDrops().add(itemEntity);
        }
    }
}
