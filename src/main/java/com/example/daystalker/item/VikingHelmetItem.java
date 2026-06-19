package com.example.daystalker.item;

import com.example.daystalker.client.armor.CustomArmorClientExtension;
import com.example.daystalker.client.armor.ModArmorModelLayers;
import com.example.daystalker.client.armor.VikingHelmetModel;
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

public class VikingHelmetItem extends ArmorItem {

    public VikingHelmetItem() {
        super(ModArmorMaterial.VIKING, Type.HELMET, new Properties());
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new CustomArmorClientExtension(
                ModArmorModelLayers.VIKING_HELMET,
                VikingHelmetModel::new
        ));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if (!level.isClientSide && entity instanceof Player player) {
            ItemStack helmet = player.getInventory().getArmor(3);
            if (helmet.getItem() == this) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 40, 0, false, false));
            }
        }
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "daystalker:textures/models/armor/viking_layer_1.png";
    }
}
