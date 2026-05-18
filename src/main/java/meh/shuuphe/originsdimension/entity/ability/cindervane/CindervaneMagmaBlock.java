package meh.shuuphe.originsdimension.entity.ability.cindervane;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import meh.shuuphe.originsdimension.registry.ModEntities;

import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import java.util.List;

public class CindervaneMagmaBlock extends ProjectileEntity implements FlyingItemEntity {

    private final float impactDamage;
    private final double impactRadius;
    private int lifeTicks;

    public CindervaneMagmaBlock(World world, LivingEntity owner, float impactDamage, double impactRadius, int lifeTicks) {
        super(ModEntities.CINDERVANE_MAGMA_BLOCK, world);
        this.setOwner(owner);
        this.impactDamage = impactDamage;
        this.impactRadius = impactRadius;
        this.lifeTicks = lifeTicks;
        this.setPosition(owner.getX(), owner.getY() + owner.getHeight() * 0.5, owner.getZ());
    }

    public CindervaneMagmaBlock(EntityType<? extends CindervaneMagmaBlock> type, World world) {
        super(type, world);
        this.impactDamage = 20.0f;
        this.impactRadius = 7.0;
        this.lifeTicks = 200;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getEntityWorld().isClient()) {
            if (--lifeTicks <= 0) {
                this.discard();
                return;
            }
            // Spawn particles
            if (this.getEntityWorld() instanceof ServerWorld sw) {
                sw.spawnParticles(ParticleTypes.LAVA, this.getX(), this.getY(), this.getZ(), 2, 0.2, 0.2, 0.2, 0.0);
                sw.spawnParticles(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 3, 0.3, 0.3, 0.3, 0.05);
            }
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult hitResult) {
        explode();
    }

    @Override
    protected void onEntityHit(EntityHitResult hitResult) {
        explode();
    }

    private void explode() {
        if (this.getEntityWorld().isClient() || this.isRemoved()) return;
        ServerWorld sw = (ServerWorld) this.getEntityWorld();

        sw.spawnParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 10, 1.0, 0.5, 1.0, 0.1);
        sw.spawnParticles(ParticleTypes.LAVA, this.getX(), this.getY(), this.getZ(), 20, 2.0, 1.0, 2.0, 0.15);
        sw.spawnParticles(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 30, 2.0, 1.0, 2.0, 0.1);
        Box area = Box.of(this.getEntityPos(), impactRadius * 2, impactRadius, impactRadius * 2);
        List<LivingEntity> targets = sw.getEntitiesByClass(LivingEntity.class, area, e -> {
            if (e == this.getOwner()) return false;
            if (!e.isAlive()) return false;
            return this.squaredDistanceTo(e) <= impactRadius * impactRadius;
        });

        for (LivingEntity target : targets) {
            target.damage(sw, sw.getDamageSources().mobProjectile(this, (LivingEntity)this.getOwner()), impactDamage);
            target.setOnFireFor(4);
        }

        this.discard();
    }

    @Override
    protected void initDataTracker(net.minecraft.entity.data.DataTracker.Builder builder) {}

    @Override
    public ItemStack getStack() {
        return new ItemStack(Items.MAGMA_BLOCK);
    }
}