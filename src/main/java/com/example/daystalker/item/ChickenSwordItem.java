package com.example.daystalker.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ChickenSwordItem extends SwordItem {

    public ChickenSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    // Set enemy on fire when hit
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    
        boolean result = super.hurtEnemy(stack, target, attacker);

         // Spawn chicken when enemy dies from this hit
        if (!target.level().isClientSide && target.getHealth() <= 0
                && !(target instanceof Chicken)) {
            Level level = target.level();
            Chicken chicken = new Chicken(EntityType.CHICKEN, level);
            chicken.setPos(target.getX(), target.getY(), target.getZ());
            chicken.setBaby(false);
            level.addFreshEntity(chicken);
        }
        return result;
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
