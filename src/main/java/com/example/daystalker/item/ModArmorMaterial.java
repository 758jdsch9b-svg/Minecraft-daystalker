package com.example.daystalker.item;

import com.example.daystalker.DaystalkerMod;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Items;

public enum ModArmorMaterial implements ArmorMaterial {

    ROYAL("royal",
            20,                         // durability multiplier (iron=15, diamond=33)
            new int[]{2, 5, 6, 2},      // defense: feet, legs, chest, head
            12,                         // enchantability (iron=9, gold=25)
            SoundEvents.ARMOR_EQUIP_GOLD,
            1.0F,                       // toughness
            0.0F,                       // knockback resistance
            () -> Ingredient.of(Items.GOLD_INGOT)),

    VIKING("viking",
            18,
            new int[]{2, 5, 6, 3},
            10,
            SoundEvents.ARMOR_EQUIP_IRON,
            0.0F,
            0.0F,
            () -> Ingredient.of(Items.IRON_INGOT));

    private static final int[] BASE_DURABILITY = {13, 15, 16, 11};

    private final String name;
    private final int durabilityMultiplier;
    private final int[] slotProtections;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final java.util.function.Supplier<Ingredient> repairIngredient;

    ModArmorMaterial(String name, int durabilityMultiplier, int[] slotProtections,
                     int enchantmentValue, SoundEvent sound, float toughness,
                     float knockbackResistance, java.util.function.Supplier<Ingredient> repairIngredient) {
        this.name = DaystalkerMod.MOD_ID + ":" + name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.slotProtections = slotProtections;
        this.enchantmentValue = enchantmentValue;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override public int getDurabilityForType(ArmorItem.Type type) { return BASE_DURABILITY[type.getSlot().getIndex()] * durabilityMultiplier; }
    @Override public int getDefenseForType(ArmorItem.Type type) { return slotProtections[type.getSlot().getIndex()]; }
    @Override public int getEnchantmentValue() { return enchantmentValue; }
    @Override public SoundEvent getEquipSound() { return sound; }
    @Override public Ingredient getRepairIngredient() { return repairIngredient.get(); }
    @Override public String getName() { return name; }
    @Override public float getToughness() { return toughness; }
    @Override public float getKnockbackResistance() { return knockbackResistance; }
}
