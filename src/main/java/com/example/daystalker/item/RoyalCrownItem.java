package com.example.daystalker.item;

import com.example.daystalker.client.armor.CustomArmorClientExtension;
import com.example.daystalker.client.armor.ModArmorModelLayers;
import com.example.daystalker.client.armor.RoyalCrownModel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class RoyalCrownItem extends ArmorItem {

    public RoyalCrownItem() {
        super(ModArmorMaterial.ROYAL, Type.HELMET, new Properties());
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new CustomArmorClientExtension(
                ModArmorModelLayers.ROYAL_CROWN,
                RoyalCrownModel::new
        ));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if (!level.isClientSide && entity instanceof Player player) {
            ItemStack helmet = player.getInventory().getArmor(3);
            if (helmet.getItem() == this) {
                player.addEffect(new MobEffectInstance(MobEffects.LUCK, 40, 0, false, false));
            }
        }
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "daystalker:textures/models/armor/royal_layer_1.png";
    }
}
