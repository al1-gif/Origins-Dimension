package meh.shuuphe.originsdimension.registry;

import meh.shuuphe.originsdimension.OriginsDimension;
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

    public static final RegistryKey<Biome> GRASSLAND =
            RegistryKey.of(RegistryKeys.BIOME,
                    Identifier.of(OriginsDimension.MOD_ID, "grassland"));

    public static final RegistryKey<Biome> ORIGIN_DESERT =
            RegistryKey.of(RegistryKeys.BIOME,
                    Identifier.of(OriginsDimension.MOD_ID, "origin_desert"));

    public static final RegistryKey<Biome> ORIGIN_BADLANDS =
            RegistryKey.of(RegistryKeys.BIOME,
                    Identifier.of(OriginsDimension.MOD_ID, "origin_badlands"));

    public static final RegistryKey<Biome> AZURE_FOREST =
            RegistryKey.of(RegistryKeys.BIOME,
                    Identifier.of(OriginsDimension.MOD_ID, "azure_forest"));

    public static final RegistryKey<Biome> CONIFEROUS_FOREST =
            RegistryKey.of(RegistryKeys.BIOME,
                    Identifier.of(OriginsDimension.MOD_ID, "coniferous_forest"));

    public static final RegistryKey<Biome> SNOWY_CONIFEROUS_FOREST =
            RegistryKey.of(RegistryKeys.BIOME,
                    Identifier.of(OriginsDimension.MOD_ID, "snowy_coniferous_forest"));

    public static final RegistryKey<Biome> CONIFEROUS_FOREST_CLEARING =
            RegistryKey.of(RegistryKeys.BIOME,
                    Identifier.of(OriginsDimension.MOD_ID, "coniferous_forest_clearing"));

    public static final RegistryKey<Biome> SNOWY_CONIFEROUS_FOREST_CLEARING =
            RegistryKey.of(RegistryKeys.BIOME,
                    Identifier.of(OriginsDimension.MOD_ID, "snowy_coniferous_forest_clearing"));

    public static final RegistryKey<Biome> ORIGIN_FOREST_CLEARING =
            RegistryKey.of(RegistryKeys.BIOME,
                    Identifier.of(OriginsDimension.MOD_ID, "origin_forest_clearing"));

    public static final RegistryKey<Biome> ORIGIN_MUSHROOM_FIELDS =
            RegistryKey.of(RegistryKeys.BIOME,
                    Identifier.of(OriginsDimension.MOD_ID, "origin_mushroom_fields"));

    public static final RegistryKey<Biome> ORIGIN_BEACH =
            RegistryKey.of(RegistryKeys.BIOME,
                    Identifier.of(OriginsDimension.MOD_ID, "origin_beach"));

    public static final RegistryKey<Biome> AZURE_FOREST_CLEARING =
            RegistryKey.of(RegistryKeys.BIOME,
                    Identifier.of(OriginsDimension.MOD_ID, "azure_forest_clearing"));

    public static final RegistryKey<Biome> SNOWY_ORIGIN_PLAINS =
            RegistryKey.of(RegistryKeys.BIOME,
                    Identifier.of(OriginsDimension.MOD_ID, "snowy_origin_plains"));

    public static final RegistryKey<Biome> SNOWY_ORIGIN_BEACH =
            RegistryKey.of(RegistryKeys.BIOME,
                    Identifier.of(OriginsDimension.MOD_ID, "snowy_origin_beach"));

    public static final RegistryKey<Biome> FROZEN_ORIGIN_OCEAN =
            RegistryKey.of(RegistryKeys.BIOME,
                    Identifier.of(OriginsDimension.MOD_ID, "frozen_origin_ocean"));

    public static void register() {
        OriginsDimension.LOGGER.info("Origin Biome keys registered.");
    }
}