package meh.shuuphe.originsdimension;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class ModDimensions {

    public static final RegistryKey<World> ORIGIN_WORLD_KEY =
            RegistryKey.of(RegistryKeys.WORLD,
                    Identifier.of(OriginsDimension.MOD_ID, "origin"));

    public static final RegistryKey<DimensionType> ORIGIN_DIM_TYPE_KEY =
            RegistryKey.of(RegistryKeys.DIMENSION_TYPE,
                    Identifier.of(OriginsDimension.MOD_ID, "origin"));

    public static void register() {
        OriginsDimension.LOGGER.info("Origin Dimension keys registered.");
    }
}