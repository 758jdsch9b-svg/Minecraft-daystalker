package com.example.daystalker.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**
 * Tamable flying Dragon.
 * - Tame by feeding raw fish (cod) repeatedly until hearts appear
 * - Once tamed, right-click with a saddle to equip it
 * - With saddle equipped, mount it and fly using normal movement controls
 *   (the player steers; W/A/S/D moves, jump = ascend, sneak = descend)
 */
public class DragonEntity extends TamableAnimal implements PlayerRideableJumping {

    private static final EntityDataAccessor<Boolean> DATA_SADDLE_ID =
            SynchedEntityData.defineId(DragonEntity.class, EntityDataSerializers.BOOLEAN);

    private float playerJumpPendingScale;
    private boolean isFlying;

    public DragonEntity(EntityType<? extends DragonEntity> type, Level level) {
        super(type, level);
        this.setMaxUpStep(1.0F);
        this.moveControl = new FlyingMoveControl(this, 10, true);
        this.lookControl = new net.minecraft.world.entity.ai.control.LookControl(this);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
    }

    // ---------------------------------------------------------------
    // Attributes
    // ---------------------------------------------------------------

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.FLYING_SPEED, 0.6D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SADDLE_ID, false);
    }

    // ---------------------------------------------------------------
    // Goals / AI
    // ---------------------------------------------------------------

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5D));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new DragonFollowOwnerGoal(this, 1.0D, 8.0F, 2.0F, true));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.0D, net.minecraft.world.item.crafting.Ingredient.of(Items.COD, Items.SALMON), false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation nav = new FlyingPathNavigation(this, level);
        nav.setCanOpenDoors(false);
        nav.setCanFloat(true);
        nav.setCanPassDoors(true);
        return nav;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.COD) || stack.is(Items.SALMON);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(net.minecraft.server.level.ServerLevel level, AgeableMob otherParent) {
        return null; // breeding not implemented for this simple version
    }

    // ---------------------------------------------------------------
    // Saddle handling
    // ---------------------------------------------------------------

    public boolean isSaddled() {
        return this.entityData.get(DATA_SADDLE_ID);
    }

    public void setSaddled(boolean saddled) {
        this.entityData.set(DATA_SADDLE_ID, saddled);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Taming with fish
        if (!this.isTame() && isFood(stack)) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            if (!this.level().isClientSide) {
                if (this.random.nextInt(3) == 0) {
                    this.tame(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.setOrderedToSit(true);
                    this.level().broadcastEntityEvent(this, (byte) 7); // hearts particle
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6); // smoke particle
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        // Equip saddle
        if (this.isTame() && !this.isSaddled() && stack.is(Items.SADDLE)) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            this.setSaddled(true);
            this.playSound(SoundEvents.HORSE_SADDLE, 0.5F, 1.0F);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        // Toggle sit (owner, empty hand)
        if (this.isTame() && this.isOwnedBy(player) && stack.isEmpty() && !this.isSaddled()) {
            this.setOrderedToSit(!this.isOrderedToSit());
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        // Mount
        if (this.isSaddled() && this.isTame() && this.isOwnedBy(player) && !this.level().isClientSide) {
            this.setOrderedToSit(false);
            player.startRiding(this);
            return InteractionResult.sidedSuccess(false);
        }

        return super.mobInteract(player, hand);
    }

    // ---------------------------------------------------------------
    // Riding / Flight controls
    // ---------------------------------------------------------------

    public boolean canBeControlledByRider() {
        Entity passenger = this.getControllingPassenger();
        return passenger instanceof Player && this.isSaddled();
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity passenger = this.getFirstPassenger();
        if (passenger instanceof Player) {
            return (Player) passenger;
        }
        return null;
    }

    protected Vec3 getRiddenInput(Player player, Vec3 deltaIn) {
        // Forward/strafe from movement keys
        float forward = player.zza;
        float strafe = player.xxa;
        return new Vec3(strafe * 0.5D, 0.0D, forward);
    }

    @Override
    protected float getRiddenSpeed(Player player) {
        return (float) this.getAttributeValue(Attributes.FLYING_SPEED) * 2.0F;
    }

    @Override
    public void travel(Vec3 input) {
        if (this.isAlive()) {
            if (this.isVehicle() && this.canBeControlledByRider()) {
                LivingEntity rider = this.getControllingPassenger();
                if (rider instanceof Player player) {
                    this.setYRot(player.getYRot());
                    this.yRotO = this.getYRot();
                    this.setXRot(player.getXRot() * 0.5F);
                    this.setRot(this.getYRot(), this.getXRot());
                    this.yBodyRot = this.getYRot();
                    this.yHeadRot = this.getYRot();

                    float speed = this.getRiddenSpeed(player);
                    Vec3 ridden = this.getRiddenInput(player, input);

                    // Vertical movement: jump = up, sneak = down
                    double vertical = 0.0D;
                    if (this.playerJumpPendingScale > 0) {
                        vertical = 1.0D;
                    } else if (player.isShiftKeyDown()) {
                        vertical = -1.0D;
                    }

                    Vec3 move = new Vec3(ridden.x * speed, vertical * speed, ridden.z * speed);
                    this.setSpeed(speed);
                    super.travel(move);

                    this.isFlying = true;
                    return;
                }
            }

            // Not ridden - normal flying AI movement
            this.setSpeed((float) this.getAttributeValue(Attributes.FLYING_SPEED));
            super.travel(input);
        }
    }

    @Override
    public void onPlayerJump(int jumpPower) {
        if (jumpPower < 0) jumpPower = 0;
        if (jumpPower >= 90) {
            this.playerJumpPendingScale = 1.0F;
        } else {
            this.playerJumpPendingScale = 0.4F + 0.4F * jumpPower / 90.0F;
        }
    }

    @Override
    public boolean canJump() {
        return this.isSaddled();
    }

    @Override
    public void handleStartJump(int jumpPower) {
        // Used for jump-boost on takeoff; not strictly needed for flight
    }

    @Override
    public void handleStopJump() {
        this.playerJumpPendingScale = 0.0F;
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction callback) {
        super.positionRider(passenger, callback);
        if (passenger instanceof LivingEntity living) {
            living.yBodyRot = this.yBodyRot;
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.8D;
    }

    // Prevent fall damage while flying/ridden
    @Override
    public boolean causeFallDamage(float distance, float multiplier, net.minecraft.world.damagesource.DamageSource source) {
        return false;
    }

    public boolean isFlyingMount() {
        return this.isFlying || (!this.onGround() && !this.isInWater());
    }

    // ---------------------------------------------------------------
    // Misc
    // ---------------------------------------------------------------

    @Override
    protected void checkFallDamage(double y, boolean onGround, net.minecraft.world.level.block.state.BlockState state, BlockPos pos) {
        // no-op: dragons take no fall damage
    }

    @Override
    public boolean hurt(net.minecraft.world.damagesource.DamageSource source, float amount) {
        // Eject rider if owner attacks accidentally is handled by vanilla; just pass through
        return super.hurt(source, amount);
    }
}
