package meh.shuuphe.originsdimension.entity.ability.cindervane;

import meh.shuuphe.originsdimension.registry.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class CindervaneGroundCombatGoal extends Goal {

    private static final double ATTACK_RANGE    = 4.5;
    private static final double FIRE_BODY_RANGE = 8.0;
    private static final double CHASE_SPEED     = 1.0;

    private static final double LEAP_MIN_DIST      = 10.0;
    private static final double LEAP_MAX_DIST      = 32.0;
    private static final int    LEAP_COOLDOWN_TICKS = 90;
    private static final int    LEAP_MAX_AIR_TICKS  = 55;

    private final Cindervane dragon;
    private int fireBodyCooldown = 0;
    private int pathRecalcTimer  = 0;
    private int attackCooldown   = 0;
    private double lastTargetX, lastTargetY, lastTargetZ;
    private boolean isLeaping    = false;
    private int     leapCooldown = 0;
    private int     leapAirTicks = 0;

    public CindervaneGroundCombatGoal(Cindervane dragon) {
        this.dragon = dragon;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (dragon.isSleepLocked()) return false;
        LivingEntity target = dragon.getTarget();
        if (target == null || !target.isAlive()) return false;
        double followRange = dragon.getAttributeValue(
                net.minecraft.entity.attribute.EntityAttributes.FOLLOW_RANGE);
        if (followRange <= 0.0) followRange = 16.0;
        if (dragon.squaredDistanceTo(target) > followRange * followRange) return false;
        return !dragon.isFlying() && !dragon.isHovering()
                && !dragon.isTakeoff() && !dragon.isLanding();
    }

    @Override
    public boolean shouldContinue() {
        if (dragon.isSleepLocked()) return false;
        LivingEntity target = dragon.getTarget();
        if (target == null || !target.isAlive()) return false;
        if (dragon.isFlying() || dragon.isHovering()
                || dragon.isTakeoff() || dragon.isLanding()) return false;
        double followRange = dragon.getAttributeValue(
                net.minecraft.entity.attribute.EntityAttributes.FOLLOW_RANGE);
        return dragon.squaredDistanceTo(target) <= followRange * followRange;
    }

    @Override
    public boolean shouldRunEveryTick() { return true; }

    @Override
    public void start() {
        dragon.setSprinting(true);
        pathRecalcTimer = 0;
        isLeaping       = false;
        leapAirTicks    = 0;
        LivingEntity target = dragon.getTarget();
        if (target != null) {
            rememberPos(target);
            dragon.getNavigation().startMovingTo(target, CHASE_SPEED);
        }
    }

    @Override
    public void stop() {
        dragon.getNavigation().stop();
        dragon.setFireBodyActive(false);
        dragon.setSprinting(false);
        pathRecalcTimer = 0;
        attackCooldown  = 0;
        isLeaping       = false;
        leapAirTicks    = 0;
    }

    @Override
    public void tick() {
        if (fireBodyCooldown > 0) fireBodyCooldown--;
        if (attackCooldown   > 0) attackCooldown--;
        if (leapCooldown     > 0) leapCooldown--;

        LivingEntity target = dragon.getTarget();
        if (target == null) return;

        if (dragon.isTakeoff()) {
            dragon.getNavigation().stop();
            return;
        }

        if (isLeaping) {
            dragon.getNavigation().stop();
            dragon.getLookControl().lookAt(target, 30f, 30f);
            leapAirTicks++;
            if (leapAirTicks > 6 && dragon.isOnGround()) {
                finishLeap();
                return;
            }
            if (leapAirTicks >= LEAP_MAX_AIR_TICKS) {
                isLeaping    = false;
                leapAirTicks = 0;
                leapCooldown = LEAP_COOLDOWN_TICKS;
            }
            return;
        }

        double dist    = dragon.distanceTo(target);
        double distSq  = dist * dist;
        double reachSq = getAttackReachSq(target);
        boolean hasSight = dragon.canSee(target);

        dragon.getLookControl().lookAt(target, 30f, 30f);

        if (distSq <= reachSq && hasSight) {
            dragon.getNavigation().stop();
            dragon.setSprinting(false);
            pathRecalcTimer = 0;
            if (attackCooldown <= 0) performAttack(dist);

        } else {
            dragon.setSprinting(true);
            boolean attackPlaying = dragon.isAbilityInProgress();

            if (!attackPlaying) {
                if (++pathRecalcTimer >= MathHelper.clamp((int)(dist * 0.6), 5, 20)
                        || movedSignificantly(target)) {
                    pathRecalcTimer = 0;
                    rememberPos(target);
                    dragon.getNavigation().startMovingTo(target, CHASE_SPEED);
                }
                if (leapCooldown <= 0
                        && dist >= LEAP_MIN_DIST && dist <= LEAP_MAX_DIST
                        && dragon.isOnGround()) {
                    performLeap(target);
                }
            }
        }

        if (fireBodyCooldown <= 0) {
            if (!dragon.isFireBodyActive() && dist < FIRE_BODY_RANGE) {
                dragon.setFireBodyActive(true);
                fireBodyCooldown = 40;
            } else if (dragon.isFireBodyActive() && dist > FIRE_BODY_RANGE * 1.5) {
                dragon.setFireBodyActive(false);
                fireBodyCooldown = 40;
            }
        }
    }


    private void performAttack(double dist) {
        if (dragon.isAbilityInProgress()) return;

        int roll = dragon.getRandom().nextInt(3);
        if (roll == 0 && dist <= 7.0) {
            dragon.startSlashPublic();
            attackCooldown = 45;
        } else if (roll == 1) {
            dragon.startVolleyPublic();
            attackCooldown = 40;
        } else {
            dragon.tryBite();
            attackCooldown = 28;
        }
    }

    private void performLeap(LivingEntity target) {
        Vec3d toTarget = target.getEntityPos().subtract(dragon.getEntityPos());
        double horizDist = Math.sqrt(toTarget.x * toTarget.x + toTarget.z * toTarget.z);
        if (horizDist < 0.1) return;

        Vec3d dir = new Vec3d(toTarget.x / horizDist, 0, toTarget.z / horizDist);

        double hSpeed = MathHelper.clamp(horizDist / 18.0, 0.55, 1.15);
        double vSpeed = MathHelper.clamp(0.45 + horizDist * 0.018, 0.45, 0.72);
        float yawJitter = (dragon.getRandom().nextFloat() - 0.5f) * 12f;
        double rad = Math.toRadians(yawJitter);
        double jx = dir.x * Math.cos(rad) - dir.z * Math.sin(rad);
        double jz = dir.x * Math.sin(rad) + dir.z * Math.cos(rad);

        dragon.setVelocity(jx * hSpeed, vSpeed, jz * hSpeed);
        dragon.velocityModified = true;

        isLeaping    = true;
        leapAirTicks = 0;

        dragon.getNavigation().stop();
        dragon.triggerAnim("instant", "takeoff");
        dragon.getEntityWorld().playSound(
                null, dragon.getBlockPos(),
                ModSounds.CINDERVANE_TAKEOFF,
                SoundCategory.HOSTILE, 1.6f, 1.05f);
    }

    private void finishLeap() {
        isLeaping    = false;
        leapAirTicks = 0;
        leapCooldown = LEAP_COOLDOWN_TICKS;

        dragon.triggerAnim("actions", "landed");
        dragon.getEntityWorld().playSound(
                null, dragon.getBlockPos(),
                ModSounds.CINDERVANE_LANDED,
                SoundCategory.HOSTILE, 2.0f, 1.0f);

        LivingEntity target = dragon.getTarget();
        if (target != null && dragon.distanceTo(target) <= ATTACK_RANGE + 2.5
                && attackCooldown <= 0) {
            attackCooldown = 8;
        }
    }

    private double getAttackReachSq(LivingEntity target) {
        double combined = dragon.getWidth() * 0.5 + target.getWidth() * 0.5;
        double reach    = ATTACK_RANGE + combined;
        return reach * reach;
    }

    private void rememberPos(LivingEntity t) {
        lastTargetX = t.getX(); lastTargetY = t.getY(); lastTargetZ = t.getZ();
    }

    private boolean movedSignificantly(LivingEntity t) {
        double dx = t.getX() - lastTargetX;
        double dy = t.getY() - lastTargetY;
        double dz = t.getZ() - lastTargetZ;
        return dx*dx + dy*dy + dz*dz > 4.0;
    }
}