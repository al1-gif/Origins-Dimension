package meh.shuuphe.originsdimension.entity.ability.nulljaw;

import meh.shuuphe.originsdimension.registry.ModEntities;
import meh.shuuphe.originsdimension.registry.ModSounds;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Nulljaw extends TameableEntity implements GeoEntity {

    private static final RawAnimation IDLE =
            RawAnimation.begin().thenLoop("animation.nulljaw.idle");
    private static final RawAnimation HOVER =
            RawAnimation.begin().thenLoop("animation.nulljaw.hover");
    private static final RawAnimation HAND_HOLDING =
            RawAnimation.begin().thenLoop("animation.nulljaw.hand_holding");
    private static final RawAnimation GRUMBLE_1 =
            RawAnimation.begin().thenPlay("animation.nulljaw.grumble1");
    private static final RawAnimation GRUMBLE_2 =
            RawAnimation.begin().thenPlay("animation.nulljaw.grumble2");
    private static final RawAnimation GRUMBLE_3 =
            RawAnimation.begin().thenPlay("animation.nulljaw.grumble3");
    private static final RawAnimation EAT =
            RawAnimation.begin().thenPlay("animation.nulljaw.eat");
    private static final RawAnimation HURT_ANIM =
            RawAnimation.begin().thenPlay("animation.nulljaw.hurt");
    private static final RawAnimation DIE_ANIM =
            RawAnimation.begin().thenPlayAndHold("animation.nulljaw.die");

    private static final double RIDER_SPEED         = 0.32;
    private static final double RIDER_ASCEND_SPEED  = 0.16;
    private static final double RIDER_DESCEND_SPEED = 0.18;
    private static final double FLOAT_DRAG          = 0.96;
    private static final int    TAME_CHANCE         = 5;
    private static final int    HOVER_GRACE_TICKS   = 8;
    private int   hoverGraceTicks = 0;
    private int   eatTimer        = 0;
    private int   ambientTimer    = 0;
    private float bankAngle       = 0f;
    private float prevBankAngle   = 0f;
    private float prevYawTrack    = 0f;

    private static final TrackedData<Boolean> HOVER_ACTIVE =
            DataTracker.registerData(Nulljaw.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Float> FLIGHT_PITCH =
            DataTracker.registerData(Nulljaw.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Boolean> EATING =
            DataTracker.registerData(Nulljaw.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> AMBIENT_TYPE =
            DataTracker.registerData(Nulljaw.class, TrackedDataHandlerRegistry.INTEGER);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public Nulljaw(EntityType<? extends TameableEntity> type, World world) {
        super(type, world);
        this.setNoGravity(true);
        this.moveControl = new NulljawFlyMoveControl(this);
    }
    private static final class NulljawFlyMoveControl extends MoveControl {
        private final Nulljaw entity;

        NulljawFlyMoveControl(Nulljaw entity) {
            super(entity);
            this.entity = entity;
        }

        @Override
        public void tick() {
            if (this.state != State.MOVE_TO) return;

            Vec3d target = new Vec3d(this.targetX, this.targetY, this.targetZ);
            Vec3d pos    = entity.getEntityPos();
            Vec3d delta  = target.subtract(pos);
            double dist  = delta.length();

            if (dist < 1.0 || this.speed <= 0) {
                this.state = State.WAIT;
                entity.setVelocity(entity.getVelocity().multiply(0.6));
                entity.velocityDirty = true;
                return;
            }

            Vec3d dir = delta.normalize();

            float wantYaw = (float) Math.toDegrees(Math.atan2(-dir.x, dir.z));
            float yawDiff = MathHelper.wrapDegrees(wantYaw - entity.getYaw());
            float newYaw  = entity.getYaw() + MathHelper.clamp(yawDiff, -10f, 10f);
            entity.setYaw(newYaw);
            entity.bodyYaw = newYaw;
            entity.headYaw = newYaw;

            Vec3d current = entity.getVelocity();
            Vec3d desired = dir.multiply(0.18 * this.speed);
            entity.setVelocity(
                    current.add(desired.subtract(current).multiply(0.12)).multiply(0.92)
            );
            entity.velocityDirty = true;
        }
    }
    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH,    200.0)
                .add(EntityAttributes.MOVEMENT_SPEED,  0.30)
                .add(EntityAttributes.FLYING_SPEED,    0.80)
                .add(EntityAttributes.FOLLOW_RANGE,   48.0)
                .add(EntityAttributes.TEMPT_RANGE,    48.0)
                .add(EntityAttributes.ARMOR,            8.0);
    }
    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(HOVER_ACTIVE, false);
        builder.add(FLIGHT_PITCH, 0f);
        builder.add(EATING,       false);
        builder.add(AMBIENT_TYPE, 0);
    }
    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation nav = new BirdNavigation(this, world);
        nav.setCanSwim(false);
        return nav;
    }
    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SitGoal(this));
        this.goalSelector.add(2, new TemptGoal(this, 1.0,
                Ingredient.ofItems(Items.CHORUS_FRUIT), false));
        this.goalSelector.add(4, new NulljawFloatGoal(this));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 12.0f));
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("movement", 6, state -> {
            if (isDead()) return PlayState.STOP;
            if (dataTracker.get(HOVER_ACTIVE)) return state.setAndContinue(HOVER);
            return state.setAndContinue(IDLE);
        }));
        controllers.add(new AnimationController<>("actions", 2, state -> {
            if (dataTracker.get(EATING)) return state.setAndContinue(EAT);
            return switch (dataTracker.get(AMBIENT_TYPE)) {
                case 1  -> state.setAndContinue(GRUMBLE_1);
                case 2  -> state.setAndContinue(GRUMBLE_2);
                case 3  -> state.setAndContinue(GRUMBLE_3);
                default -> PlayState.STOP;
            };
        }));
        controllers.add(new AnimationController<>("instant", 1, state -> {
            if (isDead())     return state.setAndContinue(DIE_ANIM);
            if (hurtTime > 0) return state.setAndContinue(HURT_ANIM);
            return PlayState.STOP;
        }));
        controllers.add(new AnimationController<>("mounted", 2, state -> {
            if (!hasPassengers()) return PlayState.STOP;
            return state.setAndContinue(HAND_HOLDING);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    @Override @Nullable
    protected SoundEvent getDeathSound() { return ModSounds.NULLJAW_DIE; }

    @Override @Nullable
    protected SoundEvent getHurtSound(DamageSource source) { return ModSounds.NULLJAW_HURT; }

    @Override @Nullable
    protected SoundEvent getAmbientSound() { return null; }
    @Override
    public void tick() {
        super.tick();
        setNoGravity(true);

        prevBankAngle = bankAngle;
        float yawDelta = MathHelper.wrapDegrees(getYaw() - prevYawTrack);
        bankAngle      = MathHelper.lerp(0.1f, bankAngle, yawDelta * -3.0f);
        bankAngle      = MathHelper.clamp(bankAngle, -30f, 30f);
        prevYawTrack   = getYaw();

        if (!getEntityWorld().isClient()) {
            serverTick();
        }
    }

    private void serverTick() {
        boolean moving = getVelocity().lengthSquared() > 0.0015;
        if (moving) {
            hoverGraceTicks = HOVER_GRACE_TICKS;
        } else if (hoverGraceTicks > 0) {
            hoverGraceTicks--;
        }
        boolean active = hoverGraceTicks > 0;
        if (dataTracker.get(HOVER_ACTIVE) != active) dataTracker.set(HOVER_ACTIVE, active);

        float targetPitch = (float) MathHelper.clamp(-getVelocity().y * 1.5, -0.7, 0.7);
        float curPitch    = dataTracker.get(FLIGHT_PITCH);
        dataTracker.set(FLIGHT_PITCH, curPitch + (targetPitch - curPitch) * 0.15f);

        if (eatTimer > 0) {
            eatTimer--;
            if (!dataTracker.get(EATING)) dataTracker.set(EATING, true);
        } else {
            if (dataTracker.get(EATING)) dataTracker.set(EATING, false);
        }

        if (ambientTimer > 0) {
            ambientTimer--;
            if (ambientTimer == 0) dataTracker.set(AMBIENT_TYPE, 0);
        } else if (!isDead() && !hasPassengers() && getRandom().nextInt(400) == 0) {
            int type = 1 + getRandom().nextInt(3);
            dataTracker.set(AMBIENT_TYPE, type);
            ambientTimer = 60;
            SoundEvent grumble = switch (type) {
                case 1  -> ModSounds.NULLJAW_GRUMBLE1;
                case 2  -> ModSounds.NULLJAW_GRUMBLE2;
                default -> ModSounds.NULLJAW_GRUMBLE3;
            };
            playSound(grumble, 1.0f, 0.9f + getRandom().nextFloat() * 0.2f);
        }
    }

    public float getBankAngle(float partialTick) {
        return MathHelper.lerp(partialTick, prevBankAngle, bankAngle);
    }
    public float getFlightPitch() {
        return dataTracker.get(FLIGHT_PITCH);
    }

    @Override @Nullable
    public LivingEntity getControllingPassenger() {
        Entity first = getFirstPassenger();
        return first instanceof PlayerEntity p ? p : null;
    }

    @Override
    protected void tickControlled(PlayerEntity player, Vec3d movementInput) {
        super.tickControlled(player, movementInput);
        float diff   = MathHelper.wrapDegrees(player.getYaw() - getYaw());
        float newYaw = getYaw() + diff * 0.35f;
        setYaw(newYaw);
        bodyYaw     = newYaw;
        headYaw     = newYaw;
        lastBodyYaw = newYaw;
        lastHeadYaw = newYaw;
        setPitch(0f);

        float yawRad   = newYaw * ((float) Math.PI / 180f);
        float pitchRad = player.getPitch() * ((float) Math.PI / 180f);
        Vec3d forward  = new Vec3d(
                -MathHelper.sin(yawRad) * MathHelper.cos(pitchRad),
                -MathHelper.sin(pitchRad),
                MathHelper.cos(yawRad) * MathHelper.cos(pitchRad)
        );
        Vec3d strafeDir = new Vec3d(MathHelper.cos(yawRad), 0.0, MathHelper.sin(yawRad));

        float fwd = (float) movementInput.z;
        float str = (float) movementInput.x;
        if (fwd < 0) fwd *= 0.5f;

        Vec3d desired = forward.multiply(fwd).add(strafeDir.multiply(str * 0.55));
        if (desired.lengthSquared() > 1e-6) desired = desired.normalize().multiply(RIDER_SPEED);

        double vertical = desired.y;
        if (player.isSprinting()) vertical += RIDER_ASCEND_SPEED;
        if (player.isSneaking())  vertical -= RIDER_DESCEND_SPEED;

        Vec3d cur     = getVelocity();
        Vec3d blended = cur.add(desired.subtract(cur).multiply(0.25));
        blended = new Vec3d(
                blended.x * FLOAT_DRAG,
                MathHelper.clamp(vertical, -0.45, 0.45),
                blended.z * FLOAT_DRAG
        );

        setMovementSpeed((float) RIDER_SPEED);
        move(MovementType.SELF, blended);
        setVelocity(blended);
        velocityDirty = true;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack held = player.getStackInHand(hand);
        if (player.getVehicle() == this) return ActionResult.PASS;

        if (held.isOf(Items.CHORUS_FRUIT)) {
            if (!getEntityWorld().isClient()) {
                if (!player.getAbilities().creativeMode) held.decrement(1);
                eatTimer = 30;
                playSound(ModSounds.NULLJAW_EAT, 1.0f, 0.9f + getRandom().nextFloat() * 0.2f);
                setHealth(Math.min(getHealth() + 6f, getMaxHealth()));
                if (!isTamed()) {
                    if (getRandom().nextInt(TAME_CHANCE) == 0) {
                        setOwner(player);
                        getNavigation().stop();
                        getEntityWorld().sendEntityStatus(this, (byte) 7);
                    } else {
                        getEntityWorld().sendEntityStatus(this, (byte) 6);
                    }
                } else {
                    getEntityWorld().sendEntityStatus(this, (byte) 7);
                }
            }
            return ActionResult.SUCCESS;
        }

        if (isTamed() && isOwner(player)) {
            if (player.isSneaking() && hand == Hand.MAIN_HAND && held.isEmpty()) {
                if (!getEntityWorld().isClient()) setInSittingPose(!isInSittingPose());
                return ActionResult.SUCCESS;
            }
            if (!player.isSneaking() && hand == Hand.MAIN_HAND) {
                if (!getEntityWorld().isClient()) player.startRiding(this);
                return ActionResult.SUCCESS;
            }
        }

        return super.interactMob(player, hand);
    }

    @Override
    public boolean handleFallDamage(double fallDistance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty,
                                 SpawnReason spawnReason, @Nullable EntityData entityData) {
        entityData = super.initialize(world, difficulty, spawnReason, entityData);
        setHealth(getMaxHealth());
        return entityData;
    }

    @Override @Nullable
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.NULLJAW.create(world, SpawnReason.BREEDING);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.CHORUS_FRUIT);
    }
}