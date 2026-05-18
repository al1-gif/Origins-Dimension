package meh.shuuphe.originsdimension.registry;

import meh.shuuphe.originsdimension.OriginsDimension;
import meh.shuuphe.originsdimension.items.SyncopeItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import static meh.shuuphe.originsdimension.registry.ModBlocks.*;

public class ModItems {
    private static RegistryKey<Item> itemkey(String path) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(OriginsDimension.MOD_ID, path));
    }

    public static final Item SILVER_INGOT = new Item(
            new Item.Settings().registryKey(itemkey("silver_ingot")));
    public static final Item PRIMEVELITE_INGOT = new Item(
            new Item.Settings().registryKey(itemkey("primevelite_ingot")));
    public static final Item RAW_SILVER = new Item(
            new Item.Settings().registryKey(itemkey("raw_silver")));
    public static final Item SYNCOPE = new SyncopeItem(
            new Item.Settings().registryKey(itemkey("syncope")));
    public static final Item VARASUCHUS_SPAWN_EGG = new SpawnEggItem(
            new Item.Settings().registryKey(itemkey("varasuchus_spawn_egg")).spawnEgg(ModEntities.VARASUCHUS));

    public static final Item CINDERVANE_SPAWN_EGG = new SpawnEggItem(
            new Item.Settings().registryKey(itemkey("cindervane_spawn_egg")).spawnEgg(ModEntities.CINDERVANE));
    public static final Item NULLJAW_SPAWN_EGG = new SpawnEggItem(
            new Item.Settings().registryKey(itemkey("nulljaw_spawn_egg")).spawnEgg(ModEntities.NULLJAW));
    public static final Item STEGONAUT_SPAWN_EGG = new SpawnEggItem(
            new Item.Settings().registryKey(itemkey("stegonaut_spawn_egg")).spawnEgg(ModEntities.STEGONAUT));
    public static final Item IGNIVORUS_SPAWN_EGG = new SpawnEggItem(
            new Item.Settings().registryKey(itemkey("ignivorus_spawn_egg")).spawnEgg(ModEntities.IGNIVORUS));
    public static final Item VOLITANS_SPAWN_EGG = new SpawnEggItem(
            new Item.Settings().registryKey(itemkey("volitans_spawn_egg")).spawnEgg(ModEntities.VOLITANS));
    public static final Item IVYMERCHANT_SPAWN_EGG = new SpawnEggItem(
            new Item.Settings().registryKey(itemkey("ivymerchant_spawn_egg")).spawnEgg(ModEntities.IVYMERCHANT));

    public static void registerModItems() {
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "silver_ingot"), SILVER_INGOT);
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "primevelite_ingot"), PRIMEVELITE_INGOT);
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "raw_silver"), RAW_SILVER);
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "syncope"), SYNCOPE);

        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "varasuchus_spawn_egg"), VARASUCHUS_SPAWN_EGG);
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "cindervane_spawn_egg"), CINDERVANE_SPAWN_EGG);
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "nulljaw_spawn_egg"), NULLJAW_SPAWN_EGG);
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "stegonaut_spawn_egg"), STEGONAUT_SPAWN_EGG);
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "ignivorus_spawn_egg"), IGNIVORUS_SPAWN_EGG);
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "volitans_spawn_egg"), VOLITANS_SPAWN_EGG);
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "ivymerchant_spawn_egg"), IVYMERCHANT_SPAWN_EGG);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(SILVER_INGOT);
            entries.add(PRIMEVELITE_INGOT);
            entries.add(RAW_SILVER);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(SILVER_BLOCK);
            entries.add(SILVER_ORE);
            entries.add(PRIMEVELITE_BLOCK);
            entries.add(PRIMEVELITE_ORE);
            entries.add(DEEPSLATE_PRIMEVELITE_ORE);
            entries.add(DEEPSLATE_SILVER_ORE);
            entries.add(FIR_LOG);
            entries.add(STRIPPED_FIR_WOOD);
            entries.add(STRIPPED_FIR_LOG);
            entries.add(FIR_WOOD);
            entries.add(FIR_PLANKS);
            entries.add(FIR_LEAVES);
            entries.add(FIR_SAPLING);
            entries.add(FIR_SLAB);
            entries.add(FIR_STAIRS);
            entries.add(FIR_FENCE);
            entries.add(FIR_FENCE_GATE);
            entries.add(FIR_DOOR);
            entries.add(FIR_TRAPDOOR);
            entries.add(FIR_BUTTON);
            entries.add(FIR_PRESSURE_PLATE);
            entries.add(FIR_SIGN);
            entries.add(FIR_HANGING_SIGN);
            entries.add(FIR_SHELF);
            entries.add(JACARANDA_LOG);
            entries.add(STRIPPED_JACARANDA_WOOD);
            entries.add(STRIPPED_JACARANDA_LOG);
            entries.add(JACARANDA_WOOD);
            entries.add(JACARANDA_PLANKS);
            entries.add(JACARANDA_LEAVES);
            entries.add(JACARANDA_SAPLING);
            entries.add(JACARANDA_SLAB);
            entries.add(JACARANDA_STAIRS);
            entries.add(JACARANDA_FENCE);
            entries.add(JACARANDA_FENCE_GATE);
            entries.add(JACARANDA_DOOR);
            entries.add(JACARANDA_TRAPDOOR);
            entries.add(JACARANDA_BUTTON);
            entries.add(JACARANDA_PRESSURE_PLATE);
            entries.add(JACARANDA_SIGN);
            entries.add(JACARANDA_HANGING_SIGN);
            entries.add(JACARANDA_SHELF);

        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(SYNCOPE);
            entries.add(ModBlocks.PINK_DAFFODIL);
            entries.add(ModBlocks.ORANGE_COSMOS);
            entries.add(ModBlocks.BURNING_BLOSSOM);
            entries.add(ModBlocks.BLUE_HYDRANGEA);
            entries.add(ModBlocks.MARIGOLD);
            entries.add(ModBlocks.VIOLET);
            entries.add(ModBlocks.SNOWBLOSSOM_LEAVES);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.add(LAVENDER);
            entries.add(SPROUT);
            entries.add(CLOVER);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> {
            entries.add(VARASUCHUS_SPAWN_EGG);
            entries.add(CINDERVANE_SPAWN_EGG);
            entries.add(NULLJAW_SPAWN_EGG);
            entries.add(STEGONAUT_SPAWN_EGG);
            entries.add(IGNIVORUS_SPAWN_EGG);
            entries.add(VOLITANS_SPAWN_EGG);
            entries.add(IVYMERCHANT_SPAWN_EGG);
        });
    }
}
