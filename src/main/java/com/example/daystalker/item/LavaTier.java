package com.example.daystalker.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Items;

public enum LavaTier implements Tier {
    INSTANCE;

    @Override
    public int getUses() {
        return 1561; // Same durability as diamond
    }

    @Override
    public float getSpeed() {
        return 8.0F; // Same mining speed as diamond
    }

    @Override
    public float getAttackDamageBonus() {
        return 3.0F; // Base attack (SwordItem adds +3 on top = 6 total, same as diamond sword)
    }

    @Override
    public int getLevel() {
        return 3; // Diamond level = can mine obsidian etc.
    }

    @Override
    public int getEnchantmentValue() {
        return 14; // Same as diamond
    }

    @Override
    public Ingredient getRepairIngredient() {
        // Repair with magma cream or blaze rod
        return Ingredient.of(Items.MAGMA_CREAM);
    }
}
