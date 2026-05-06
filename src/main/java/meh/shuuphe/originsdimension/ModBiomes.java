package meh.shuuphe.originsdimension;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class ModBiomes {

    public static final RegistryKey<Biome> ORIGIN_FOREST =
            RegistryKey.of(RegistryKeys.BIOME,
                    Identifier.of(OriginsDimension.MOD_ID, "origin_forest"));

    public static final RegistryKey<Biome> SPOOKY_LAND =
            RegistryKey.of(RegistryKeys.BIOME,
                    Identifier.of(OriginsDimension.MOD_ID, "spooky_land"));

    public static final RegistryKey<Biome> ORIGIN_OCEAN =
            RegistryKey.of(RegistryKeys.BIOME,
                    Identifier.of(OriginsDimension.MOD_ID, "origin_ocean"));

    public static final RegistryKey<Biome> ORIGIN_FLOWER_FOREST =
            RegistryKey.of(RegistryKeys.BIOME,
                    Identifier.of(OriginsDimension.MOD_ID, "origin_flower_forest"));

    public static void register() {
        OriginsDimension.LOGGER.info("Origin Biome keys registered.");
    }
}