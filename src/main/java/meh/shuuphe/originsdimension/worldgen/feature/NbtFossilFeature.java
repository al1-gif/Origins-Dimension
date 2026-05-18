package meh.shuuphe.originsdimension.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.io.InputStream;
import java.util.Random;

public class NbtFossilFeature extends Feature<DefaultFeatureConfig> {

    private static final String[] FOSSIL_NAMES = {
            "fossil_1", "fossil_2", "fossil_3", "fossil_4", "fossil_5"
    };

    public NbtFossilFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> ctx) {
        StructureWorldAccess world = ctx.getWorld();
        BlockPos origin = ctx.getOrigin();
        Random random = new Random(ctx.getRandom().nextLong());

        BlockPos ground = origin;
        for (int i = 0; i < 16; i++) {
            if (world.getBlockState(ground.down()).isSolidBlock(world, ground.down())) break;
            ground = ground.down();
            if (i == 15) return false;
        }

        if (!world.getFluidState(origin).isEmpty()) return false;
        if (!world.getFluidState(origin.down()).isEmpty()) return false;
        if (!world.getBlockState(ground).isAir()) return false;

        String name = FOSSIL_NAMES[random.nextInt(FOSSIL_NAMES.length)];
        String path = "/data/originsdimension/structures/" + name + ".nbt";

        try (InputStream stream = NbtFossilFeature.class.getResourceAsStream(path)) {
            if (stream == null) {
                System.out.println("FOSSIL: stream null for " + path);
                return false;
            }

            NbtCompound nbt = NbtIo.readCompressed(stream, net.minecraft.nbt.NbtSizeTracker.ofUnlimitedBytes());
            StructureTemplate template = new StructureTemplate();
            template.readNbt(world.toServerWorld().createCommandRegistryWrapper(net.minecraft.registry.RegistryKeys.BLOCK), nbt);

            BlockRotation rotation = BlockRotation.values()[random.nextInt(4)];
            StructurePlacementData data = new StructurePlacementData()
                    .setRotation(rotation)
                    .setIgnoreEntities(true);

            net.minecraft.util.math.Vec3i size = template.getRotatedSize(rotation);
            BlockPos offset = ground.add(-size.getX() / 2, 0, -size.getZ() / 2);

            template.place(world, offset, offset, data, world.getRandom(), 3);
            System.out.println("FOSSIL: placed " + name + " at " + offset);
            return true;

        } catch (Exception e) {
            System.out.println("FOSSIL ERROR: " + e.getMessage());
            return false;
        }
    }
}