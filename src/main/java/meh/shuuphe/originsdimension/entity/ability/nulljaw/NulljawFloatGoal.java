package meh.shuuphe.originsdimension.entity.ability.nulljaw;

import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class NulljawFloatGoal extends Goal {

    private static final double ARRIVAL_DIST_SQ = 4.0;
    private static final int    MIN_TRAVEL      = 50;
    private static final int    MAX_TRAVEL      = 100;

    private final Nulljaw nulljaw;
    private Vec3d target;
    private int travelTicks;
    private int cooldown;

    public NulljawFloatGoal(Nulljaw nulljaw) {
        this.nulljaw = nulljaw;
        setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (nulljaw.isTamed() && nulljaw.isInSittingPose()) return false;
        if (nulljaw.getOwner() != null
                && nulljaw.squaredDistanceTo(nulljaw.getOwner()) > 64.0
                && nulljaw.isInSittingPose()) return false;
        if (cooldown > 0) { cooldown--; return false; }
        return findTarget() != null;
    }

    @Override
    public void start() {
        target      = findTarget();
        travelTicks = MIN_TRAVEL + nulljaw.getRandom().nextInt(MAX_TRAVEL - MIN_TRAVEL + 1);
        if (target == null) { cooldown = 20; return; }
        nulljaw.getMoveControl().moveTo(target.x, target.y, target.z, 1.0);
    }

    @Override
    public boolean shouldContinue() {
        return target != null
                && travelTicks > 0
                && nulljaw.squaredDistanceTo(target) > ARRIVAL_DIST_SQ;
    }

    @Override
    public void tick() {
        if (target == null) return;
        travelTicks--;
    }

    @Override
    public void stop() {
        nulljaw.getMoveControl().moveTo(nulljaw.getX(), nulljaw.getY(), nulljaw.getZ(), 0);
        nulljaw.setVelocity(nulljaw.getVelocity().multiply(0.3));
        nulljaw.velocityDirty = true;
        target   = null;
        cooldown = 25 + nulljaw.getRandom().nextInt(35);
    }

    private Vec3d findTarget() {
        for (int i = 0; i < 12; i++) {
            double x = nulljaw.getX() + (nulljaw.getRandom().nextDouble() - 0.5) * 24.0;
            double y = nulljaw.getY() + (nulljaw.getRandom().nextDouble() - 0.5) * 10.0;
            double z = nulljaw.getZ() + (nulljaw.getRandom().nextDouble() - 0.5) * 24.0;

            BlockPos pos = BlockPos.ofFloored(x, y, z);
            if (!nulljaw.getEntityWorld().isChunkLoaded(pos)) continue;

            BlockState block = nulljaw.getEntityWorld().getBlockState(pos);
            if (!block.getCollisionShape(nulljaw.getEntityWorld(), pos).isEmpty()) continue;

            FluidState fluid = nulljaw.getEntityWorld().getFluidState(pos);
            if (!fluid.isEmpty()) continue;

            return new Vec3d(x, y, z);
        }
        return null;
    }
}