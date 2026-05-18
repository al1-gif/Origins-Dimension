package meh.shuuphe.originsdimension.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.block.MushroomBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

public class MushroomTreeFeature extends Feature<DefaultFeatureConfig> {

    public MushroomTreeFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> ctx) {
        StructureWorldAccess world = ctx.getWorld();
        BlockPos origin = ctx.getOrigin();
        Random random = new Random(ctx.getRandom().nextLong());
        if (!world.getFluidState(origin).isEmpty()) return false;
        if (!world.getFluidState(origin.down()).isEmpty()) return false;

        int stemHeight = 14 + random.nextInt(6);

        for (int y = 0; y < stemHeight; y++) {
            for (int dx = 0; dx <= 1; dx++) {
                for (int dz = 0; dz <= 1; dz++) {
                    world.setBlockState(origin.add(dx, y, dz),
                            Blocks.MUSHROOM_STEM.getDefaultState()
                                    .with(MushroomBlock.NORTH, true)
                                    .with(MushroomBlock.SOUTH, true)
                                    .with(MushroomBlock.EAST,  true)
                                    .with(MushroomBlock.WEST,  true)
                                    .with(MushroomBlock.UP,    false)
                                    .with(MushroomBlock.DOWN,  false),
                            3);
                }
            }
        }

        int numRings = 2 + random.nextInt(3);
        int lastRing = 3;
        for (int i = 0; i < numRings; i++) {
            int ringY = lastRing + 3 + random.nextInt(3);
            if (ringY >= stemHeight - 3) break;
            lastRing = ringY;
            int ringRadius = 2 + random.nextInt(2);
            placeFlatRing(world, origin.add(0, ringY, 0), ringRadius);
        }

        BlockPos capBase = origin.add(0, stemHeight, 0);
        int topRadius = 4 + random.nextInt(2);
        int numTiers  = 3 + random.nextInt(2);

        for (int tier = 0; tier < numTiers; tier++) {
            int tierRadius = topRadius + tier;
            int tierY = -tier;
            placeFlatDisk(world, capBase.add(0, tierY, 0), tierRadius);
        }

        return true;
    }

    private void placeFlatDisk(StructureWorldAccess world, BlockPos center, int radius) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                double dist = Math.sqrt(dx * dx + dz * dz);
                if (dist > radius) continue;
                boolean isEdge = dist > radius - 1.5;
                world.setBlockState(center.add(dx, 0, dz),
                        Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState()
                                .with(MushroomBlock.UP,    true)
                                .with(MushroomBlock.DOWN,  isEdge)
                                .with(MushroomBlock.NORTH, isEdge && dz < 0)
                                .with(MushroomBlock.SOUTH, isEdge && dz > 0)
                                .with(MushroomBlock.EAST,  isEdge && dx > 0)
                                .with(MushroomBlock.WEST,  isEdge && dx < 0),
                        3);
            }
        }
    }

    private void placeFlatRing(StructureWorldAccess world, BlockPos center, int radius) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                double dist = Math.sqrt(dx * dx + dz * dz);
                if (dist > radius || dist < radius - 2) continue;
                boolean isOuter = dist > radius - 1;
                world.setBlockState(center.add(dx, 0, dz),
                        Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState()
                                .with(MushroomBlock.UP,    true)
                                .with(MushroomBlock.DOWN,  true)
                                .with(MushroomBlock.NORTH, isOuter && dz < 0)
                                .with(MushroomBlock.SOUTH, isOuter && dz > 0)
                                .with(MushroomBlock.EAST,  isOuter && dx > 0)
                                .with(MushroomBlock.WEST,  isOuter && dx < 0),
                        3);
            }
        }
    }
}