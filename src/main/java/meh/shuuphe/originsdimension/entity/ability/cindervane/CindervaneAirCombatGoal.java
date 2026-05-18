package meh.shuuphe.originsdimension.entity.ability.cindervane;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;

import java.util.EnumSet;

public class CindervaneAirCombatGoal extends Goal {

    private static final double BITE_RANGE      = 6.0;
    private static final double APPROACH_DIST   = 3.5;
    private static final double AIR_CHASE_SPEED = 0.40;
    private static final int    LOST_SIGHT_TICKS = 30;

    private final Cindervane dragon;
    private int lostSightTicks   = 0;
    private int fireBodyCooldown = 0;

    public CindervaneAirCombatGoal(Cindervane dragon) {
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
        if (followRange <= 0) followRange = 16.0;
        if (dragon.squaredDistanceTo(target) > followRange * followRange) return false;
        return dragon.isFlying() || dragon.isTakeoff() || dragon.isLanding();
    }

    @Override
    public boolean shouldContinue() {
        if (dragon.isSleepLocked()) return false;
        LivingEntity target = dragon.getTarget();
        if (target == null || !target.isAlive()) return false;
        double followRange = dragon.getAttributeValue(
                net.minecraft.entity.attribute.EntityAttributes.FOLLOW_RANGE);
        if (followRange <= 0) followRange = 16.0;
        if (dragon.squaredDistanceTo(target) > followRange * followRange) return false;
        if (dragon.isLanding()) return !dragon.isOnGround();
        return dragon.isFlying() || dragon.isTakeoff();
    }

    @Override
    public void start() {
        lostSightTicks = 0;
    }

    @Override
    public void stop() {
        lostSightTicks = 0;
        dragon.setFireBodyActive(false);
    }

    @Override
    public boolean shouldRunEveryTick() { return true; }

    @Override
    public void tick() {
        if (fireBodyCooldown > 0) fireBodyCooldown--;

        LivingEntity target = dragon.getTarget();
        if (target == null || !target.isAlive()) return;

        dragon.getLookControl().lookAt(target, 30f, 30f);

        if (!isAirborne(target) && (dragon.isFlying() || dragon.isTakeoff())) {
            dragon.beginLanding();
            return;
        }

        boolean hasSight = dragon.canSee(target);
        if (!hasSight) {
            lostSightTicks++;
            if (lostSightTicks >= LOST_SIGHT_TICKS && dragon.isFlying()) {
                dragon.beginLanding();
                return;
            }
        } else {
            lostSightTicks = 0;
        }

        double dist = dragon.distanceTo(target);

        if (dist <= BITE_RANGE && hasSight) {
            maintainPosition(target);
            dragon.tryBite();
        } else {
            chaseTarget(target);
        }

        if (fireBodyCooldown <= 0) {
            if (!dragon.isFireBodyActive() && dist < 8.0) {
                dragon.setFireBodyActive(true);
                fireBodyCooldown = 40;
            } else if (dragon.isFireBodyActive() && dist > 12.0) {
                dragon.setFireBodyActive(false);
                fireBodyCooldown = 40;
            }
        }
    }

    private void maintainPosition(LivingEntity target) {
        Vec3d targetMid = target.getEntityPos().add(0, target.getHeight() * 0.5, 0);
        Vec3d toTarget  = targetMid.subtract(dragon.getEntityPos());
        double dist = toTarget.length();
        if (dist < 1e-4) return;
        Vec3d dir     = toTarget.normalize();
        Vec3d desired = targetMid.subtract(dir.multiply(APPROACH_DIST));
        double speed  = dist > APPROACH_DIST ? 0.30 : 0.16;
        dragon.setVelocity(dragon.getVelocity().lerp(
                desired.subtract(dragon.getEntityPos()).multiply(speed), 0.2));
    }

    private void chaseTarget(LivingEntity target) {
        double bob = Math.sin(dragon.age * 0.15) * 0.35;
        Vec3d predicted = target.getEntityPos()
                .add(target.getVelocity().multiply(5.0))
                .add(0, target.getHeight() + 0.5 + bob, 0);
        Vec3d toTarget = predicted.subtract(dragon.getEntityPos());
        double dist = toTarget.length();
        if (dist < 1e-4) return;
        Vec3d dir     = toTarget.normalize();
        Vec3d desired = dir.multiply(AIR_CHASE_SPEED);
        double yError = predicted.y - dragon.getY();
        double yVel   = Math.max(-0.22, Math.min(0.20, yError * 0.06));
        desired = new Vec3d(desired.x, yVel, desired.z);
        dragon.setVelocity(dragon.getVelocity().lerp(desired, 0.15));
    }

    private boolean isAirborne(LivingEntity entity) {
        if (entity.isOnGround()) return false;
        int groundY = entity.getEntityWorld().getTopY(
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                entity.getBlockX(), entity.getBlockZ());
        return entity.getY() - groundY > 2.0;
    }
}