package com.example.daystalker.entity;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

/**
 * Simple "follow owner" goal for a flying tamed mob.
 * Moves toward the owner if too far away, stops if close enough.
 */
public class DragonFollowOwnerGoal extends Goal {

    private final TamableAnimal mob;
    private Player owner;
    private final double speedModifier;
    private final float startDistance;
    private final float stopDistance;
    private final boolean canFly;
    private int timeToRecalcPath;

    public DragonFollowOwnerGoal(TamableAnimal mob, double speedModifier, float startDistance, float stopDistance, boolean canFly) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
        this.canFly = canFly;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        Player owner = this.mob.getOwner() instanceof Player p ? p : null;
        if (owner == null || this.mob.isOrderedToSit() || this.mob.isVehicle()) {
            return false;
        }
        if (owner.isSpectator()) {
            return false;
        }
        if (this.mob.distanceToSqr(owner) < (double) (this.startDistance * this.startDistance)) {
            return false;
        }
        this.owner = owner;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob.getNavigation().isDone()) {
            return false;
        }
        if (this.mob.isOrderedToSit() || this.mob.isVehicle()) {
            return false;
        }
        return !(this.mob.distanceToSqr(this.owner) <= (double) (this.stopDistance * this.stopDistance));
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
    }

    @Override
    public void stop() {
        this.owner = null;
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.owner == null) return;

        this.mob.getLookControl().setLookAt(this.owner, 10.0F, (float) this.mob.getMaxHeadXRot());

        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            if (!this.mob.isLeashed() && !this.mob.isPassenger()) {
                this.mob.getNavigation().moveTo(this.owner, this.speedModifier);
            }
        }
    }
}
