package meh.shuuphe.originsdimension.entity.ability.cindervane;

import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import meh.shuuphe.originsdimension.registry.ModSounds;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class Cindervane extends PathAwareEntity implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final TrackedData<Boolean> IS_FLYING =
            DataTracker.registerData(Cindervane.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_TAKEOFF =
            DataTracker.registerData(Cindervane.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_LANDING =
            DataTracker.registerData(Cindervane.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_HOVERING =
            DataTracker.registerData(Cindervane.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_FIRE_BODY =
            DataTracker.registerData(Cindervane.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> SYNC_FLIGHT_POSE =
            DataTracker.registerData(Cindervane.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Byte> SYNC_SLEEP_STAGE =
            DataTracker.registerData(Cindervane.class, TrackedDataHandlerRegistry.BYTE);

    static final int FPOSE_SURFACE = 0;
    static final int FPOSE_TAKEOFF = 1;
    static final int FPOSE_CLIMB = 2;
    static final int FPOSE_CRUISE = 3;
    static final int FPOSE_LAND_CLOSE = 4;
    static final int FPOSE_LAND_FAR = 5;

    private enum FlightPhase {
        NONE, VERTICAL, CLIMB, CRUISE, LAND_CLOSE, LAND_FAR
    }
    private FlightPhase flightPhase = FlightPhase.NONE;

    public  static final int TAKEOFF_TICKS = 25;
    private static final int CLIMB_MIN = 50;
    private static final int CLIMB_MAX = 80;
    private static final double CLIMB_ANGLE_DEG = 35.0;
    private static final double VERTICAL_VY = 0.22;
    private static final double CLIMB_SPEED_START = 0.18;
    private static final double CLIMB_SPEED_MAX = 0.65;
    private static final double CLIMB_ACCEL = 0.009;
    private static final double CRUISE_SPEED_MAX = 0.75;
    private static final double CRUISE_ACCEL = 0.003;
    private static final int FLIGHT_MIN = 600;
    private static final int FLIGHT_MAX = 2400;
    private static final double FAR_LAND_HEIGHT = 14.0;
    private static final double FAR_LAND_SPEED = 0.52;
    private static final int LANDING_SETTLE = 4;
    private static final int GROUND_MIN = 300;
    private static final int GROUND_MAX = 600;
    private static final double FIRE_BODY_CRASH_DROP = 7.0;
    private static final float FIRE_BODY_AURA_RADIUS = 3.5f;
    private static final float FIRE_BODY_DAMAGE = 3.0f;
    private static final float BITE_DAMAGE = 12.0f;
    private static final double BITE_RANGE = 10.0;
    private static final double BITE_ANGLE_DEG = 35.0;
    private static final int BITE_STARTUP = 5;
    private static final int BITE_ACTIVE = 2;
    private static final int BITE_RECOVERY = 5;
    private static final int BITE_COOLDOWN_TICKS = 15;
    private static final int ROAR_STARTUP = 6;
    private static final int ROAR_ACTIVE = 30;
    private static final int ROAR_RECOVERY = 10;
    private static final int ROAR_COOLDOWN_TICKS = 20;
    private static final int VOLLEY_STARTUP = 3;
    private static final int VOLLEY_RECOVERY = 5;
    private static final int VOLLEY_COOLDOWN_TICKS = 60;
    private static final int SLASH_TICKS = 35;
    private static final int SLASH_COOLDOWN_TICKS = 7;
    private static final float SLASH_DMG_1 = 5.0f;
    private static final float SLASH_DMG_2 = 7.0f;
    private static final int SLASH_HIT1_TICK = 16;
    private static final int SLASH_DISMOUNT_TICK = 26;
    private static final int FALL_ASLEEP_ANIM_TICKS = 40;
    private static final int WAKE_UP_ANIM_TICKS = 40;

    private static final RawAnimation IDLE =
            RawAnimation.begin().thenLoop("animation.cindervane.idle");
    private static final RawAnimation WALK =
            RawAnimation.begin().thenLoop("animation.cindervane.walk");
    private static final RawAnimation RUN =
            RawAnimation.begin().thenLoop("animation.cindervane.run");
    private static final RawAnimation SWIM =
            RawAnimation.begin().thenLoop("animation.cindervane.swim");
    private static final RawAnimation FLAP =
            RawAnimation.begin().thenLoop("animation.cindervane.flap");
    private static final RawAnimation SPRINT_FLAP=
            RawAnimation.begin().thenLoop("animation.cindervane.sprint_flap");
    private static final RawAnimation FALLING =
            RawAnimation.begin().thenLoop("animation.cindervane.falling");
    private static final RawAnimation SLEEP =
            RawAnimation.begin().thenLoop("animation.cindervane.sleep");
    private static final RawAnimation TAKEOFF_A =
            RawAnimation.begin().thenPlay("animation.cindervane.takeoff");
    private static final RawAnimation LANDED_A =
            RawAnimation.begin().thenPlay("animation.cindervane.landed");
    private static final RawAnimation BITE_A =
            RawAnimation.begin().thenPlay("animation.cindervane.bite");
    private static final RawAnimation BITE_AIR_A =
            RawAnimation.begin().thenPlay("animation.cindervane.bite_air");
    private static final RawAnimation ROAR_A = RawAnimation.begin().thenPlay("animation.cindervane.roar");
    private static final RawAnimation ROAR_AIR_A =
            RawAnimation.begin().thenPlay("animation.cindervane.roar_air");
    private static final RawAnimation HURT_A =
            RawAnimation.begin().thenPlay("animation.cindervane.hurt");
    private static final RawAnimation DIE_A =
            RawAnimation.begin().thenPlayAndHold("animation.cindervane.die");
    private static final RawAnimation SIT_DOWN_A =
            RawAnimation.begin().thenPlay("animation.cindervane.down");
    private static final RawAnimation SIT_UP_A =
            RawAnimation.begin().thenPlay("animation.cindervane.up");
    private static final RawAnimation FALL_SLEEP =
            RawAnimation.begin().thenPlay("animation.cindervane.fall_asleep");
    private static final RawAnimation WAKE_UP_A =
            RawAnimation.begin().thenPlay("animation.cindervane.wake_up");
    private static final RawAnimation GRUMBLE1 =
            RawAnimation.begin().thenPlay("animation.cindervane.grumble1");
    private static final RawAnimation GRUMBLE2 =
            RawAnimation.begin().thenPlay("animation.cindervane.grumble2");
    private static final RawAnimation GRUMBLE3 =
            RawAnimation.begin().thenPlay("animation.cindervane.grumble3");
    private static final RawAnimation SLASH_A =
            RawAnimation.begin().thenPlay("animation.cindervane.cindervane_slash_left");
    private static final RawAnimation MAGMA_A =
            RawAnimation.begin().thenPlay("animation.cindervane.magma_blast");
    private static final RawAnimation EAT_A =
            RawAnimation.begin().thenPlay("animation.cindervane.eat");

    private enum Phase {
        NONE, BITE_S, BITE_A, BITE_R, ROAR_S, ROAR_A, ROAR_R, VOLLEY_S, VOLLEY_A, VOLLEY_R, SLASH
    }
    private enum SleepRoutine {
        NONE, FALL_ASLEEP_ANIM, SLEEPING_LOOP, WAKE_ANIM
    }

    private Phase phase = Phase.NONE;
    private int phaseTick = 0;
    private boolean hitDone = false;
    private int biteCooldown = 0;
    private int roarCooldown = 0;
    private int volleyCooldown = 0;
    private int slashCooldown  = 0;
    private int hurtTimer = 0;
    private int dieTimer = 0;
    private int ambientCooldown= 200;
    private int fireBodyTicks = 0;
    private int volleyInterval = 0;
    private int volleysFired = 0;
    private int slashTick = 0;
    private boolean slashHit1 = false;
    private boolean slashHit2 = false;
    private int grabbedEntityId= -1;
    private int sleepSuppress = 0;
    private int runSoundTimer = 0;
    private int flapSoundTimer = 0;
    private int hurtSoundCooldown = 0;
    private boolean fireBodyArmed = false;
    private double fireBodyMaxY = 0;
    private SleepRoutine sleepRoutine = SleepRoutine.NONE;
    private int sleepRoutineTicks = 0;
    private int takeoffTimer = 0;
    private int climbTimer = 0;
    private int flightTimer = 0;
    private int landingSettle = 0;
    private int groundTimer = 0;
    private double flightSpeed = 0.0;

    public Cindervane(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
        groundTimer = GROUND_MIN + this.getRandom().nextInt(GROUND_MAX - GROUND_MIN);
        ambientCooldown = 180 + this.getRandom().nextInt(240);
        this.setPathfindingPenalty(PathNodeType.LAVA, 0.0f);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 0.0f);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 0.0f);
    }

    @Override public float  getStepHeight() {
        return 1.5f;
    }
    @Override public double getEyeY() {
        return this.getY() + 2.0;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(IS_FLYING, false);
        builder.add(IS_TAKEOFF, false);
        builder.add(IS_LANDING, false);
        builder.add(IS_HOVERING, false);
        builder.add(IS_FIRE_BODY, false);
        builder.add(SYNC_FLIGHT_POSE, FPOSE_SURFACE);
        builder.add(SYNC_SLEEP_STAGE, (byte) 0);
    }

    private void syncSleepStage(byte stage) {
        if (stage != dataTracker.get(SYNC_SLEEP_STAGE)) dataTracker.set(SYNC_SLEEP_STAGE, stage);
    }
    public byte getSyncedSleepStage() {
        return dataTracker.get(SYNC_SLEEP_STAGE);
    }
    public int  getSyncedFlightPose() {
        return dataTracker.get(SYNC_FLIGHT_POSE);
    }
    private void syncFlightPose(int p) {
        if (p != dataTracker.get(SYNC_FLIGHT_POSE)) dataTracker.set(SYNC_FLIGHT_POSE, p);
    }
    public  boolean isFlying() {
        return dataTracker.get(IS_FLYING);
    }
    public  boolean isTakeoff() {
        return dataTracker.get(IS_TAKEOFF);
    }
    public  boolean isLanding() {
        return dataTracker.get(IS_LANDING);
    }
    public  boolean isHovering() {
        return dataTracker.get(IS_HOVERING);
    }
    public  boolean isFireBodyActive() {
        return dataTracker.get(IS_FIRE_BODY);
    }
    public  void setFireBodyActive(boolean v){
        dataTracker.set(IS_FIRE_BODY, v);
    }
    private void setFlying(boolean v) {
        dataTracker.set(IS_FLYING, v);
    }
    private void setTakeoff(boolean v) {
        dataTracker.set(IS_TAKEOFF, v);
    }
    private void setLanding(boolean v) {
        dataTracker.set(IS_LANDING, v);
    }
    private void setHovering(boolean v) {
        dataTracker.set(IS_HOVERING, v);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 200.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.45)
                .add(EntityAttributes.ATTACK_DAMAGE, 12.0)
                .add(EntityAttributes.FOLLOW_RANGE, 48.0)
                .add(EntityAttributes.ARMOR, 8.0)
                .add(EntityAttributes.KNOCKBACK_RESISTANCE, 0.8);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(2, new CindervaneAirCombatGoal(this));
        this.goalSelector.add(3, new CindervaneGroundCombatGoal(this));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 0.6, 160));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 12.0f));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override protected SoundEvent getHurtSound(DamageSource s) {
        return ModSounds.CINDERVANE_HURT;
    }
    @Override protected SoundEvent getDeathSound() {
        return ModSounds.CINDERVANE_DIE;
    }
    @Override protected SoundEvent getAmbientSound() {
        return ModSounds.CINDERVANE_GRUMBLE;
    }
    @Override protected float getSoundVolume() {
        return 2.0f;
    }

    @Override
    public void tick() {
        super.tick();

        boolean noGrav = isFlying() || isTakeoff() || isHovering()
                || (isLanding() && getSyncedFlightPose() == FPOSE_LAND_FAR);
        this.setNoGravity(noGrav);

        if (isFlying() || isTakeoff()) this.fallDistance = 0f;

        if (!this.getEntityWorld().isClient()) {
            if (isSleepLocked()) {
                this.getNavigation().stop();
                this.setTarget(null);
                this.setVelocity(Vec3d.ZERO);
            }
            tickCooldowns();
            tickFlight();
            tickAbilityPhase();
            tickFireBody();
            tickFireBodyCrash();
            tickSleep();
            tickAmbient();
            tickFlapSound();
            tickRunSound();
        }
    }

    private void tickFlight() {
        switch (flightPhase) {
            case VERTICAL -> tickVertical();
            case CLIMB -> tickClimb();
            case CRUISE -> tickCruise();
            case LAND_CLOSE -> tickLandClose();
            case LAND_FAR -> tickLandFar();
            case NONE -> tickGround();
        }
    }

    private void tickVertical() {
        takeoffTimer--;
        setVelocity(0.0, VERTICAL_VY, 0.0);
        syncFlightPose(FPOSE_TAKEOFF);

        if (takeoffTimer <= 0) {
            flightPhase = FlightPhase.CLIMB;
            climbTimer = CLIMB_MIN + getRandom().nextInt(CLIMB_MAX - CLIMB_MIN);
            flightSpeed = CLIMB_SPEED_START;
            setTakeoff(false);
            setFlying(true);
            syncFlightPose(FPOSE_CLIMB);
        }
    }
    private void tickClimb() {
        climbTimer--;
        syncFlightPose(FPOSE_CLIMB);

        flightSpeed = Math.min(flightSpeed + CLIMB_ACCEL, CLIMB_SPEED_MAX);

        boolean hasCombatTarget = getTarget() != null && getTarget().isAlive();
        if (!hasCombatTarget) {
            float  yawRad = getYaw() * MathHelper.RADIANS_PER_DEGREE;
            Vec3d  fwd = new Vec3d(-MathHelper.sin(yawRad), 0.0, MathHelper.cos(yawRad));
            double angle = Math.toRadians(CLIMB_ANGLE_DEG);
            double vh = flightSpeed * Math.cos(angle);
            double vy = flightSpeed * Math.sin(angle);
            setVelocity(fwd.x * vh, vy, fwd.z * vh);
        }

        if (climbTimer <= 0) {
            flightPhase = FlightPhase.CRUISE;
            flightTimer = FLIGHT_MIN + getRandom().nextInt(FLIGHT_MAX - FLIGHT_MIN);
            syncFlightPose(FPOSE_CRUISE);
        }
    }

    private void tickCruise() {
        if (isTouchingWater()) { beginLanding(); return; }
        flightTimer--;
        syncFlightPose(FPOSE_CRUISE);

        flightSpeed = Math.min(flightSpeed + CRUISE_ACCEL, CRUISE_SPEED_MAX);

        boolean hasCombatTarget = getTarget() != null && getTarget().isAlive();
        if (!hasCombatTarget) {
            Vec3d  vel = getVelocity();
            float  yawRad = getYaw() * MathHelper.RADIANS_PER_DEGREE;
            Vec3d  fwd = new Vec3d(-MathHelper.sin(yawRad), 0.0, MathHelper.cos(yawRad));
            double tx = fwd.x * flightSpeed;
            double tz = fwd.z * flightSpeed;
            double ty = MathHelper.clamp(vel.y * 0.85, -0.04, 0.04);
            setVelocity(
                    vel.x * 0.88 + tx * 0.12,
                    ty,
                    vel.z * 0.88 + tz * 0.12);
        }

        if (flightTimer <= 0) beginLanding();
    }

    private void tickLandClose() {
        Vec3d vel = getVelocity();
        setVelocity(vel.x * 0.70, vel.y, vel.z * 0.70);
        syncFlightPose(FPOSE_LAND_CLOSE);

        if (isOnGround()) {
            landingSettle++;
            if (landingSettle >= LANDING_SETTLE) completeLanding();
        } else {
            landingSettle = 0;
        }
    }

    private void tickLandFar() {
        float  yawRad = getYaw() * MathHelper.RADIANS_PER_DEGREE;
        Vec3d  fwd = new Vec3d(-MathHelper.sin(yawRad), 0.0, MathHelper.cos(yawRad));
        double angle  = Math.toRadians(45.0);
        double vh =  FAR_LAND_SPEED * Math.cos(angle);
        double vy = -FAR_LAND_SPEED * Math.sin(angle);
        setVelocity(fwd.x * vh, vy, fwd.z * vh);
        syncFlightPose(FPOSE_LAND_FAR);

        if (isOnGround()) {
            landingSettle++;
            if (landingSettle >= LANDING_SETTLE) completeLanding();
        } else {
            landingSettle = 0;
        }
    }

    private void tickGround() {
        if (!isOnGround() || isTouchingWater()) return;
        groundTimer--;
        long dayTime = getEntityWorld().getTimeOfDay() % 24000L;
        boolean isNight = dayTime >= 13000L && dayTime < 23000L;
        if (groundTimer <= 0 && getTarget() == null && !isNight) {
            if (getRandom().nextInt(3) == 0) beginFullFlightTakeoff();
            else groundTimer = GROUND_MIN + getRandom().nextInt(GROUND_MAX - GROUND_MIN);
        } else if (groundTimer <= 0) {
            groundTimer = GROUND_MIN + getRandom().nextInt(GROUND_MAX - GROUND_MIN);
        }
    }

    public void beginFullFlightTakeoff() {
        if (isTouchingWater() || flightPhase != FlightPhase.NONE) return;
        if (sleepRoutine != SleepRoutine.NONE) return;

        flightPhase = FlightPhase.VERTICAL;
        takeoffTimer = TAKEOFF_TICKS;
        flightSpeed = 0.0;
        setFlying(false);
        setTakeoff(true);
        setLanding(false);
        setHovering(false);
        syncFlightPose(FPOSE_TAKEOFF);
        this.triggerAnim("instant", "takeoff");
        this.getNavigation().stop();
        this.playSound(ModSounds.CINDERVANE_TAKEOFF, 2.0f, 1.0f);
    }

    public void beginLanding() {
        if (isLanding()) return;
        setFlying(false);
        setTakeoff(false);
        setHovering(false);
        setLanding(true);
        landingSettle = 0;

        int groundY = getEntityWorld().getTopY(
                net.minecraft.world.Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                getBlockX(), getBlockZ());
        double heightAbove = this.getY() - groundY;

        if (heightAbove > FAR_LAND_HEIGHT) {
            flightPhase = FlightPhase.LAND_FAR;
            syncFlightPose(FPOSE_LAND_FAR);
        } else {
            flightPhase = FlightPhase.LAND_CLOSE;
            syncFlightPose(FPOSE_LAND_CLOSE);
        }
    }

    private void completeLanding() {
        setLanding(false);
        flightPhase  = FlightPhase.NONE;
        landingSettle = 0;
        flightSpeed  = 0.0;
        this.setNoGravity(false);
        syncFlightPose(FPOSE_SURFACE);
        this.triggerAnim("actions", "landed");
        this.playSound(ModSounds.CINDERVANE_LANDED, 2.0f, 1.0f);
        groundTimer = GROUND_MIN + getRandom().nextInt(GROUND_MAX - GROUND_MIN);
    }

    private void forceLandNow() {
        if (this.getEntityWorld().isClient()) return;
        int groundY = this.getEntityWorld().getTopY(
                net.minecraft.world.Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                this.getBlockX(), this.getBlockZ());
        this.requestTeleport(this.getX(), groundY + 0.01, this.getZ());
        this.setVelocity(Vec3d.ZERO);
        this.velocityDirty = true;
        setFlying(false); setTakeoff(false); setLanding(false); setHovering(false);
        this.setNoGravity(false);
        flightPhase = FlightPhase.NONE;
        flightSpeed = 0.0;
        syncFlightPose(FPOSE_SURFACE);
        this.triggerAnim("actions", "landed");
        this.playSound(ModSounds.CINDERVANE_LANDED, 2.0f, 1.0f);
        groundTimer = GROUND_MIN + getRandom().nextInt(GROUND_MAX - GROUND_MIN);
    }

    private void tickFlapSound() {
        if (!isFlying() && !isTakeoff()) return;
        if (flapSoundTimer <= 0) {
            this.getEntityWorld().playSound(null, this.getBlockPos(),
                    ModSounds.CINDERVANE_FLAP, SoundCategory.HOSTILE, 1.8f, 1.0f);
            flapSoundTimer = 18;
        }
    }

    private void tickRunSound() {
        if (!isOnGround() || !isSprinting() || getNavigation().isIdle()) return;
        if (runSoundTimer <= 0) {
            this.playSound(ModSounds.CINDERVANE_RUN, 1.0f, 1.0f);
            runSoundTimer = 12;
        }
    }

    private void tickCooldowns() {
        if (biteCooldown      > 0) biteCooldown--;
        if (roarCooldown      > 0) roarCooldown--;
        if (volleyCooldown    > 0) volleyCooldown--;
        if (slashCooldown     > 0) slashCooldown--;
        if (hurtTimer         > 0) hurtTimer--;
        if (dieTimer          > 0) dieTimer--;
        if (ambientCooldown   > 0) ambientCooldown--;
        if (sleepSuppress     > 0) sleepSuppress--;
        if (flapSoundTimer    > 0) flapSoundTimer--;
        if (runSoundTimer     > 0) runSoundTimer--;
        if (hurtSoundCooldown > 0) hurtSoundCooldown--;
        if (sleepRoutineTicks > 0) sleepRoutineTicks--;
    }

    private void tickFireBody() {
        if (!isFireBodyActive()) { fireBodyTicks = 0; return; }
        if (isTouchingWater())   { setFireBodyActive(false); return; }
        fireBodyTicks++;
        if (this.getEntityWorld() instanceof ServerWorld sw) {
            Box area = this.getBoundingBox().expand(FIRE_BODY_AURA_RADIUS, 2.5, FIRE_BODY_AURA_RADIUS);
            List<LivingEntity> targets = sw.getEntitiesByClass(LivingEntity.class, area,
                    e -> e != this && e.isAlive());
            for (LivingEntity t : targets) {
                t.damage(sw, sw.getDamageSources().onFire(), FIRE_BODY_DAMAGE);
                t.setOnFireFor(4);
                Vec3d push = t.getEntityPos().subtract(this.getEntityPos()).normalize().multiply(0.15);
                t.addVelocity(push.x, 0.05, push.z);
            }
            if (fireBodyTicks % 3 == 0) {
                sw.spawnParticles(ParticleTypes.FLAME,
                        this.getX(), this.getY() + this.getHeight() * 0.5, this.getZ(),
                        6, FIRE_BODY_AURA_RADIUS * 0.8, 1.2, FIRE_BODY_AURA_RADIUS * 0.8, 0.05);
                sw.spawnParticles(ParticleTypes.LARGE_SMOKE,
                        this.getX(), this.getY() + 0.5, this.getZ(),
                        3, 1.5, 0.5, 1.5, 0.0);
            }
        }
    }

    private void tickFireBodyCrash() {
        if (isFireBodyActive() && !isOnGround()) {
            if (!fireBodyArmed) fireBodyMaxY = this.getY();
            else if (this.getY() > fireBodyMaxY) fireBodyMaxY = this.getY();
            fireBodyArmed = true;
        }
        if (fireBodyArmed && isOnGround() && isFireBodyActive()) {
            double drop = fireBodyMaxY - this.getY();
            if (drop >= FIRE_BODY_CRASH_DROP) triggerFireBodyCrash();
            fireBodyArmed = false; fireBodyMaxY = 0;
        }
        if (!isFireBodyActive() && isOnGround()) { fireBodyArmed = false; fireBodyMaxY = 0; }
    }

    private void triggerFireBodyCrash() {
        if (!(this.getEntityWorld() instanceof ServerWorld sw)) return;
        double x = this.getX(), y = this.getY(), z = this.getZ();
        float  radius = 15.0f;
        sw.spawnParticles(ParticleTypes.FLAME,       x, y+0.8, z, 150, 2.0, 1.0, 2.0, 0.2);
        sw.spawnParticles(ParticleTypes.LAVA,        x, y+0.5, z,  40, 1.3, 0.6, 1.3, 0.12);
        sw.spawnParticles(ParticleTypes.LARGE_SMOKE, x, y+0.5, z,  80, 2.2, 0.7, 2.2, 0.05);
        Box area = Box.of(this.getEntityPos(), radius*2, radius, radius*2);
        sw.getEntitiesByClass(LivingEntity.class, area, e -> e != this && e.isAlive()
                        && this.squaredDistanceTo(e) <= radius*radius)
                .forEach(t -> { t.damage(sw, sw.getDamageSources().explosion(this, this), 200.0f);
                    t.setOnFireFor(8); });
        net.minecraft.util.math.BlockPos.Mutable pos = new net.minecraft.util.math.BlockPos.Mutable();
        int baseY = this.getBlockY();
        for (int dx = -3; dx <= 3; dx++) for (int dz = -3; dz <= 3; dz++) {
            if (getRandom().nextFloat() > 0.45f) continue;
            pos.set((int)x+dx, baseY, (int)z+dz);
            if (sw.getBlockState(pos).isAir()) {
                net.minecraft.block.BlockState below = sw.getBlockState(pos.down());
                if (!below.isAir() && Blocks.FIRE.getDefaultState().canPlaceAt(sw, pos))
                    sw.setBlockState(pos, Blocks.FIRE.getDefaultState());
            }
        }
        setFireBodyActive(false);
    }

    private void tickAbilityPhase() {
        if (phase == Phase.NONE) return;
        phaseTick++;
        switch (phase) {
            case BITE_S -> {
                if (phaseTick >= BITE_STARTUP) nextPhase(Phase.BITE_A);
            }
            case BITE_A -> {
                if (!hitDone) { performBiteHit(); hitDone = true; }
                if (phaseTick >= BITE_ACTIVE) nextPhase(Phase.BITE_R);
            }
            case BITE_R -> {
                if (phaseTick >= BITE_RECOVERY) endPhase();
            }
            case ROAR_S -> {
                if (phaseTick >= ROAR_STARTUP) { tickRoarShake(); nextPhase(Phase.ROAR_A); }
            }
            case ROAR_A -> {
                tickRoarShake(); if (phaseTick >= ROAR_ACTIVE) nextPhase(Phase.ROAR_R);
            }
            case ROAR_R -> {
                if (phaseTick >= ROAR_RECOVERY) endPhase();
            }
            case VOLLEY_S -> {
                if (phaseTick >= VOLLEY_STARTUP) { volleyInterval = 10; volleysFired = 0; nextPhase(Phase.VOLLEY_A); }
            }
            case VOLLEY_A -> {
                volleyInterval--;
                if (volleyInterval <= 0 && volleysFired < 3) { fireVolley(); volleysFired++; volleyInterval = 10; }
                if (volleysFired >= 3 && volleyInterval <= 0) nextPhase(Phase.VOLLEY_R);
            }
            case VOLLEY_R -> { if (phaseTick >= VOLLEY_RECOVERY) endPhase(); }
            case SLASH -> {
                slashTick++;
                if (!slashHit1 && slashTick >= SLASH_HIT1_TICK)    { performSlashHit(SLASH_DMG_1, false); slashHit1 = true; }
                if (!slashHit2 && slashTick >= SLASH_DISMOUNT_TICK) { performSlashHit(SLASH_DMG_2, true);  slashHit2 = true; grabbedEntityId = -1; }
                if (slashTick >= SLASH_TICKS) endPhase(); else holdGrabbed();
            }
        }
    }

    private void nextPhase(Phase next) {
        phase = next; phaseTick = 0; hitDone = false;
    }
    private void endPhase() {
        phase = Phase.NONE; phaseTick = 0; hitDone = false;
    }
    public void tryBite() {
        if (biteCooldown > 0 || phase != Phase.NONE) return;
        phase = Phase.BITE_S; phaseTick = 0; hitDone = false;
        biteCooldown = BITE_COOLDOWN_TICKS;
        this.triggerAnim("actions", isFlying() ? "bite_air" : "bite");
        this.playSound(ModSounds.CINDERVANE_BITE, 1.5f, 1.0f);
    }

    private void startRoar() {
        if (roarCooldown > 0 || phase != Phase.NONE) return;
        phase = Phase.ROAR_S; phaseTick = 0;
        roarCooldown = ROAR_COOLDOWN_TICKS;
        this.triggerAnim("actions", isFlying() ? "roar_air" : "roar");
        this.playSound(ModSounds.CINDERVANE_ROAR, 3.0f, 1.0f);
    }

    private void startVolley() {
        if (volleyCooldown > 0 || phase != Phase.NONE) return;
        phase = Phase.VOLLEY_S; phaseTick = 0;
        volleyCooldown = VOLLEY_COOLDOWN_TICKS;
        this.triggerAnim("actions", "magma_blast");
        this.playSound(ModSounds.CINDERVANE_MAGMA_BLAST, 2.0f, 1.0f);
    }

    private void startSlash() {
        if (slashCooldown > 0 || phase != Phase.NONE) return;
        phase = Phase.SLASH; phaseTick = 0; slashTick = 0;
        slashHit1 = false; slashHit2 = false; grabbedEntityId = -1;
        slashCooldown = SLASH_COOLDOWN_TICKS;
        this.triggerAnim("actions", "slash");
        this.playSound(ModSounds.CINDERVANE_SLASH, 1.5f, 1.0f);
    }

    public void startSlashPublic()       { startSlash();  }
    public void startVolleyPublic()      { startVolley(); }
    public boolean isAbilityInProgress() { return phase != Phase.NONE; }

    public boolean canTakeoffNow() {
        return sleepRoutine == SleepRoutine.NONE
                && !isTouchingWater()
                && flightPhase == FlightPhase.NONE
                && isOnGround();
    }

    private void performBiteHit() {
        if (!(getEntityWorld() instanceof ServerWorld sw)) return;
        double range    = isFlying() ? BITE_RANGE + 0.6 : BITE_RANGE;
        double cosAngle = Math.cos(Math.toRadians(BITE_ANGLE_DEG));
        Vec3d  facing   = getRotationVec(1.0f).normalize();
        Box    area     = getBoundingBox().expand(range, range * 0.5, range);
        List<LivingEntity> targets = sw.getEntitiesByClass(LivingEntity.class, area, e -> {
            if (e == this || !e.isAlive()) return false;
            return facing.dotProduct(e.getEntityPos().subtract(getEntityPos()).normalize()) >= cosAngle;
        });
        LivingEntity dt = getTarget();
        if (dt != null && dt.isAlive() && squaredDistanceTo(dt) <= (range+2)*(range+2)
                && !targets.contains(dt)) {
            targets = new java.util.ArrayList<>(targets);
            ((java.util.ArrayList<LivingEntity>) targets).add(dt);
        }
        for (LivingEntity t : targets) {
            t.damage(sw, sw.getDamageSources().mobAttack(this), BITE_DAMAGE);
            Vec3d push = getRotationVec(1.0f).multiply(0.3);
            t.addVelocity(push.x, isFlying() ? 0.15 : 0.05, push.z);
        }
    }

    private void tickRoarShake() {
        if (!(getEntityWorld() instanceof ServerWorld sw)) return;
        Box area = getBoundingBox().expand(18, 8, 18);
        sw.getEntitiesByClass(PlayerEntity.class, area, p -> true).forEach(p -> {
            Vec3d away = p.getEntityPos().subtract(getEntityPos()).normalize().multiply(0.08);
            p.addVelocity(away.x * getRandom().nextFloat(),
                    0.04 * getRandom().nextFloat(),
                    away.z * getRandom().nextFloat());
        });
    }

    private void fireVolley() {
        if (!(getEntityWorld() instanceof ServerWorld sw)) return;
        Vec3d facing = getRotationVec(1.0f).normalize();
        Vec3d origin = getEntityPos().add(0, getHeight()*0.5+1.5, 0)
                .add(facing.multiply(getWidth()*0.75));
        float baseYaw = this.getYaw(), basePitch = this.getPitch();
        for (int i = 0; i < 3; i++) {
            float yawOff   = (i-1)*9.5f + (getRandom().nextFloat()-0.5f)*6.0f;
            float pitchOff = (getRandom().nextFloat()-0.5f)*4.0f;
            Vec3d dir = Vec3d.fromPolar(basePitch+pitchOff, baseYaw+yawOff).normalize();
            CindervaneMagmaBlock block = new CindervaneMagmaBlock(sw, this, 20.0f, 7.0, 200);
            block.setPosition(origin);
            block.setVelocity(dir.multiply(0.55).add(0, -0.15, 0));
            sw.spawnEntity(block);
        }
    }

    private void performSlashHit(float damage, boolean fling) {
        if (!(getEntityWorld() instanceof ServerWorld sw)) return;
        Vec3d look   = getRotationVec(1.0f).normalize();
        Vec3d center = getEntityPos().add(look.multiply(4.0)).add(0, getHeight()*0.4, 0);
        Box   area   = Box.of(center, 7.0, 4.0, 7.0);
        sw.getEntitiesByClass(LivingEntity.class, area, e -> {
            if (e == this || !e.isAlive()) return false;
            return look.dotProduct(e.getEntityPos().subtract(getEntityPos()).normalize()) >= -0.2;
        }).forEach(t -> {
            t.damage(sw, sw.getDamageSources().mobAttack(this), damage);
            if (fling) { t.setVelocity(look.multiply(0.8).add(0, 0.5, 0)); t.velocityModified = true; }
            else if (grabbedEntityId == -1) { grabbedEntityId = t.getId(); }
        });
    }

    private void holdGrabbed() {
        if (grabbedEntityId == -1) return;
        net.minecraft.entity.Entity e = getEntityWorld().getEntityById(grabbedEntityId);
        if (!(e instanceof LivingEntity target) || !target.isAlive()) { grabbedEntityId = -1; return; }
        Vec3d look  = getRotationVec(1.0f).normalize();
        Vec3d right = new Vec3d(-look.z, 0, look.x).normalize();
        Vec3d hold  = getEntityPos().add(right.multiply(1.2)).add(look.multiply(1.0)).add(0, 1.0, 0);
        target.setVelocity(hold.subtract(target.getEntityPos()));
        target.velocityModified = true;
        target.fallDistance = 0f;
    }

    private void interruptSleepRoutineToWake(int suppressTicks) {
        if (sleepRoutine == SleepRoutine.NONE || sleepRoutine == SleepRoutine.WAKE_ANIM) return;
        sleepRoutine = SleepRoutine.WAKE_ANIM;
        sleepRoutineTicks = WAKE_UP_ANIM_TICKS;
        syncSleepStage((byte) 3);
        this.triggerAnim("actions", "wake_up");
        this.getNavigation().stop();
        if (suppressTicks > 0) sleepSuppress = Math.max(sleepSuppress, suppressTicks);
    }

    private void exitWakeAnimationIfDone() {
        if (sleepRoutine != SleepRoutine.WAKE_ANIM || sleepRoutineTicks > 0) return;
        sleepRoutine = SleepRoutine.NONE; sleepRoutineTicks = 0;
        syncSleepStage((byte) 0);
        sleepSuppress = Math.max(sleepSuppress, 50);
    }

    private boolean sleepEnvironmentInvalid() {
        return sleepSuppress > 0 || getTarget() != null || !isOnGround()
                || isTouchingWater() || isFlying() || isTakeoff() || isLanding()
                || phase != Phase.NONE;
    }

    private void tickSleep() {
        long dayTime = getEntityWorld().getTimeOfDay() % 24000L;
        boolean night = dayTime >= 13000L && dayTime < 23000L;
        switch (sleepRoutine) {
            case NONE -> {
                if (!night || sleepEnvironmentInvalid()) return;
                sleepRoutine = SleepRoutine.FALL_ASLEEP_ANIM;
                sleepRoutineTicks = FALL_ASLEEP_ANIM_TICKS;
                syncSleepStage((byte) 1);
                this.triggerAnim("actions", "fall_asleep");
                this.getNavigation().stop();
            }
            case FALL_ASLEEP_ANIM -> {
                if (sleepEnvironmentInvalid() || !night) interruptSleepRoutineToWake(120);
                else if (sleepRoutineTicks <= 0) {
                    sleepRoutine = SleepRoutine.SLEEPING_LOOP;
                    sleepRoutineTicks = 0;
                    syncSleepStage((byte) 2);
                }
            }
            case SLEEPING_LOOP -> {
                if (!night) interruptSleepRoutineToWake(0);
                else if (sleepEnvironmentInvalid()) interruptSleepRoutineToWake(160);
            }
            case WAKE_ANIM -> exitWakeAnimationIfDone();
        }
    }

    private void tickAmbient() {
        if (ambientCooldown > 0 || isDead() || hurtTimer > 0
                || phase != Phase.NONE || sleepRoutine != SleepRoutine.NONE) return;
        ambientCooldown = 180 + getRandom().nextInt(240);
        float roll = getRandom().nextFloat();
        if (roll < 0.45f) triggerAnim("actions", "grumble1");
        else if (roll < 0.80f) triggerAnim("actions", "grumble2");
        else if (roll < 0.90f) { triggerAnim("actions", "eat"); this.playSound(ModSounds.CINDERVANE_EAT, 1.0f, 1.0f); }
        else triggerAnim("actions", "grumble3");
        if (!isFlying() && getRandom().nextInt(4) == 0) startRoar();
    }

    public boolean isSleepLocked() { return sleepRoutine != SleepRoutine.NONE; }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        String id = source.getType().msgId();
        if (id.equals("inFire") || id.equals("onFire") || id.equals("lava") || id.equals("hotFloor")) return false;
        if (id.equals("fall")) return false;
        if (sleepRoutine == SleepRoutine.FALL_ASLEEP_ANIM || sleepRoutine == SleepRoutine.SLEEPING_LOOP)
            interruptSleepRoutineToWake(200);
        boolean result = super.damage(world, source, amount);
        if (result) {
            hurtTimer = 12;
            if (hurtSoundCooldown <= 0) { triggerAnim("instant", "hurt"); hurtSoundCooldown = 10; }
            if (source.getAttacker() instanceof LivingEntity attacker && attacker != this) {
                setTarget(attacker);
                if (!isSleepLocked()) {
                    tryBite();
                    if (volleyCooldown <= 0 && phase == Phase.NONE && getRandom().nextInt(3) == 0)
                        startVolley();
                }
            }
        }
        return result;
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        dieTimer = 50;
        setFlying(false); setTakeoff(false); setLanding(false);
        flightPhase = FlightPhase.NONE;
        flightSpeed = 0.0;
        sleepRoutine = SleepRoutine.NONE; sleepRoutineTicks = 0;
        syncSleepStage((byte) 0);
        setNoGravity(false);
        setFireBodyActive(false);
        triggerAnim("instant", "die");
    }

    @Override
    public boolean tryAttack(ServerWorld world, Entity target) {
        int roll = getRandom().nextInt(3);
        if      (roll == 0 && slashCooldown  <= 0) startSlash();
        else if (roll == 1 && volleyCooldown <= 0 && !isFlying()) startVolley();
        else tryBite();
        return true;
    }

    @Override
    public void writeCustomData(WriteView nbt) {
        super.writeCustomData(nbt);
        nbt.putBoolean("IsFlying", isFlying());
        nbt.putBoolean("IsTakeoff", isTakeoff());
        nbt.putBoolean("IsLanding", isLanding());
        nbt.putByte("SleepRoutine", (byte) sleepRoutine.ordinal());
        nbt.putInt("SleepRoutineTicks", sleepRoutineTicks);
        nbt.putDouble("FlightSpeed", flightSpeed);
    }

    @Override
    public void readCustomData(ReadView nbt) {
        super.readCustomData(nbt);
        setFlying(nbt.getBoolean("IsFlying",  false));
        setTakeoff(nbt.getBoolean("IsTakeoff", false));
        setLanding(nbt.getBoolean("IsLanding", false));
        SleepRoutine[] values = SleepRoutine.values();
        int ord = nbt.getByte("SleepRoutine", (byte) 0);
        sleepRoutine = (ord >= 0 && ord < values.length) ? values[ord] : SleepRoutine.NONE;
        sleepRoutineTicks = Math.max(0, nbt.getInt("SleepRoutineTicks", 0));
        flightSpeed = nbt.getDouble("FlightSpeed", 0.0);
        syncSleepStage(sleepRoutineToStageByte());
        groundTimer = GROUND_MIN + getRandom().nextInt(GROUND_MAX - GROUND_MIN);
        flightPhase = switch (getSyncedFlightPose()) {
            case FPOSE_TAKEOFF -> FlightPhase.VERTICAL;
            case FPOSE_CLIMB -> FlightPhase.CLIMB;
            case FPOSE_CRUISE -> FlightPhase.CRUISE;
            case FPOSE_LAND_CLOSE -> FlightPhase.LAND_CLOSE;
            case FPOSE_LAND_FAR -> FlightPhase.LAND_FAR;
            default -> FlightPhase.NONE;
        };
    }

    private byte sleepRoutineToStageByte() {
        return switch (sleepRoutine) {
            case NONE -> 0;
            case FALL_ASLEEP_ANIM -> 1;
            case SLEEPING_LOOP -> 2;
            case WAKE_ANIM -> 3;
        };
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

        controllers.add(new AnimationController<Cindervane>("movement", 12, state -> {
            if (dieTimer > 0) return PlayState.STOP;

            byte sleepStage = getSyncedSleepStage();
            if (sleepStage == 1 || sleepStage == 3) return PlayState.STOP;
            if (sleepStage == 2) return state.setAndContinue(SLEEP);

            if (isTakeoff()) return PlayState.STOP;

            if (isTouchingWater()) return state.setAndContinue(SWIM);

            if (isFlying()) {
                int pose = getSyncedFlightPose();
                if (pose == FPOSE_CLIMB) return state.setAndContinue(SPRINT_FLAP);
                return state.setAndContinue(FLAP);
            }

            if (isLanding()) {
                int pose = getSyncedFlightPose();
                if (pose == FPOSE_LAND_FAR) return state.setAndContinue(FALLING);
                return PlayState.STOP;
            }

            if (!getNavigation().isIdle() || getVelocity().horizontalLengthSquared() > 1.0E-6)
                return isSprinting() ? state.setAndContinue(RUN) : state.setAndContinue(WALK);

            return state.setAndContinue(IDLE);
        }));

        AnimationController<Cindervane> actions =
                new AnimationController<Cindervane>("actions", 5, state -> PlayState.STOP);
        actions.triggerableAnim("bite", BITE_A);
        actions.triggerableAnim("bite_air", BITE_AIR_A);
        actions.triggerableAnim("roar", ROAR_A);
        actions.triggerableAnim("roar_air", ROAR_AIR_A);
        actions.triggerableAnim("magma_blast", MAGMA_A);
        actions.triggerableAnim("slash", SLASH_A);
        actions.triggerableAnim("landed", LANDED_A);
        actions.triggerableAnim("down", SIT_DOWN_A);
        actions.triggerableAnim("up", SIT_UP_A);
        actions.triggerableAnim("fall_asleep", FALL_SLEEP);
        actions.triggerableAnim("sleep", SLEEP);
        actions.triggerableAnim("wake_up", WAKE_UP_A);
        actions.triggerableAnim("grumble1", GRUMBLE1);
        actions.triggerableAnim("grumble2",  GRUMBLE2);
        actions.triggerableAnim("grumble3",   GRUMBLE3);
        actions.triggerableAnim("eat", EAT_A);
        controllers.add(actions);

        AnimationController<Cindervane> instant =
                new AnimationController<Cindervane>("instant", 1, state -> PlayState.STOP);
        instant.triggerableAnim("takeoff", TAKEOFF_A);
        instant.triggerableAnim("hurt", HURT_A);
        instant.triggerableAnim("die", DIE_A);
        controllers.add(instant);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }
}