package com.example.daystalker.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LavaSwordItem extends SwordItem {

    public LavaSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    // Set enemy on fire when hit
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Set target on fire for 5 seconds
        target.setSecondsOnFire(5);

        // Optional: apply weakness briefly
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0));

        return super.hurtEnemy(stack, target, attacker);
    }

    // Glow in the dark (emit light) - visual effect via tick
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        // Sword holder won't burn from their own sword
        if (entity instanceof LivingEntity living) {
            if (isSelected) {
                living.clearFire();
            }
        }
    }

    // Sword doesn't burn in lava
    @Override
    public boolean isFireResistant() {
        return true;
    }
}
