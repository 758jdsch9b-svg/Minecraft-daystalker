package com.example.daystalker.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class DaystalkerEntity extends Monster {

    public DaystalkerEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        // Movement & behaviour goals (lower number = higher priority)
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        // Target goals
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)           // 10 hearts
                .add(Attributes.MOVEMENT_SPEED, 0.28D)       // Slightly faster than zombie
                .add(Attributes.ATTACK_DAMAGE, 4.0D)         // 2 hearts per hit
                .add(Attributes.FOLLOW_RANGE, 35.0D)         // Sees player from far away
                .add(Attributes.ARMOR, 2.0D);
    }

    // Only naturally spawns during daytime
    public static boolean checkDaystalkerSpawnRules(
            EntityType<DaystalkerEntity> type,
            ServerLevelAccessor level,
            MobSpawnType spawnType,
            net.minecraft.core.BlockPos pos,
            net.minecraft.util.RandomSource random) {

        // Use default monster rules (light level etc.) but require daytime
        if (!Monster.checkMonsterSpawnRules(type, level, spawnType, pos, random)) {
            return false;
        }

        // Only spawn during day (time 0-12000 = day)
        long dayTime = level.getLevelData().getDayTime() % 24000L;
        return dayTime < 12000L;
    }

    // Diamond drop on death
    @Override
    protected void dropCustomDeathLoot(
            net.minecraft.world.damagesource.DamageSource source,
            int lootingLevel,
            boolean recentlyHit) {

        super.dropCustomDeathLoot(source, lootingLevel, recentlyHit);

        // Drop 1-2 diamonds (+1 per looting level)
        int count = 1 + this.random.nextInt(2) + lootingLevel;
        this.spawnAtLocation(new ItemStack(Items.DIAMOND, count));
    }

    // Does NOT burn in sunlight (unlike zombies)
    @Override
    public boolean isSensitiveToWater() {
        return false;
    }
}
