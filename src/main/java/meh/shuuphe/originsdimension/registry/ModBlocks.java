package meh.shuuphe.originsdimension.registry;

import meh.shuuphe.originsdimension.OriginsDimension;
import meh.shuuphe.originsdimension.block.BlueFireBlock;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.HangingSignItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModBlocks {

    private static RegistryKey<Block> blockkey(String path) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(OriginsDimension.MOD_ID, path));
    }
    private static RegistryKey<Item> itemkey(String path) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(OriginsDimension.MOD_ID, path));
    }
    public static final WoodType FIR_WOOD_TYPE;
    static {
        try {
            java.lang.reflect.Method m = WoodType.class.getDeclaredMethod("register", WoodType.class);
            m.setAccessible(true);
            FIR_WOOD_TYPE = (WoodType) m.invoke(null, new WoodType("originsdimension:fir", BlockSetType.OAK));
        } catch (Exception e) {
            throw new RuntimeException("Failed to register fir WoodType", e);
        }
    }

    public static final WoodType JACARANDA_WOOD_TYPE;
    static {
        try {
            java.lang.reflect.Method m = WoodType.class.getDeclaredMethod("register", WoodType.class);
            m.setAccessible(true);
        JACARANDA_WOOD_TYPE = (WoodType) m.invoke(null, new WoodType("originsdimension:jacaranda", BlockSetType.OAK));
        } catch (Exception e) {
            throw new RuntimeException("Failed to register jacaranda WoodType", e);
        }
    }
    public static final SaplingGenerator FIR_SAPLING_GENERATOR = new SaplingGenerator(
            "originsdimension:fir",
            0.5F,
            Optional.of(RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE,
                    Identifier.of(OriginsDimension.MOD_ID, "fir_mega_tree"))),
            Optional.empty(),
            Optional.of(RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE,
                    Identifier.of(OriginsDimension.MOD_ID, "trees_coniferous_forest"))),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
    );
    public static final SaplingGenerator JACARANDA_SAPLING_GENERATOR = new SaplingGenerator(
            "originsdimension:jacaranda",
            0.5F,
            Optional.empty(),
            Optional.empty(),
            Optional.of(RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE,
                    Identifier.of(OriginsDimension.MOD_ID, "trees_jacaranda_forest"))),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
    );

    public static final Block PRIMEVELITE_BLOCK = new Block(
            AbstractBlock.Settings.create()
                    .strength(5.0f, 6.0f)
                    .mapColor(MapColor.PALE_PURPLE)
                    .sounds(BlockSoundGroup.METAL)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .registryKey(blockkey("primevelite_block"))
                    .requiresTool());

    public static final Block SILVER_BLOCK = new Block(
            AbstractBlock.Settings.create()
                    .strength(5.0f, 6.0f)
                    .mapColor(MapColor.IRON_GRAY)
                    .sounds(BlockSoundGroup.METAL)
                    .requiresTool()
                    .registryKey(blockkey("silver_block"))
                    .instrument(NoteBlockInstrument.XYLOPHONE));

    public static final Block PRIMEVELITE_ORE = new Block(
            AbstractBlock.Settings.create()
                    .strength(5.0f, 6.0f)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(blockkey("primevelite_ore"))
                    .requiresTool());

    public static final Block SILVER_ORE = new Block(
            AbstractBlock.Settings.create()
                    .strength(5.0f, 6.0f)
                    .sounds(BlockSoundGroup.IRON)
                    .registryKey(blockkey("silver_ore"))
                    .requiresTool());

    public static final Block DEEPSLATE_PRIMEVELITE_ORE = new Block(
            AbstractBlock.Settings.create()
                    .strength(5.0f, 6.0f)
                    .sounds(BlockSoundGroup.IRON)
                    .registryKey(blockkey("deepslate_primevelite_ore"))
                    .requiresTool());

    public static final Block DEEPSLATE_SILVER_ORE = new Block(
            AbstractBlock.Settings.create()
                    .strength(5.0f, 6.0f)
                    .sounds(BlockSoundGroup.IRON)
                    .registryKey(blockkey("deepslate_silver_ore"))
                    .requiresTool());

    public static final Block BLUE_FIRE = new BlueFireBlock(
            AbstractBlock.Settings.copy(Blocks.SOUL_FIRE)
                    .registryKey(blockkey("blue_fire")));

    public static final Block FIR_LOG = new PillarBlock(
            Blocks.createLogSettings(MapColor.SPRUCE_BROWN, MapColor.BROWN, BlockSoundGroup.WOOD)
                    .registryKey(blockkey("fir_log")));

    public static final Block FIR_WOOD = new PillarBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
                    .registryKey(blockkey("fir_wood")));

    public static final Block FIR_PLANKS = new Block(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0f, 3.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
                    .registryKey(blockkey("fir_planks")));

    public static final Block FIR_LEAVES = new TintedParticleLeavesBlock(0.01f,
            Blocks.createLeavesSettings(BlockSoundGroup.GRASS)
                    .registryKey(blockkey("fir_leaves")));

    public static final Block FIR_SAPLING = new SaplingBlock(
            FIR_SAPLING_GENERATOR,
            AbstractBlock.Settings.copy(Blocks.SPRUCE_SAPLING)
                    .registryKey(blockkey("fir_sapling")));

    public static final Block FIR_SLAB = new SlabBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0f, 3.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
                    .registryKey(blockkey("fir_slab")));

    public static final Block FIR_STAIRS = new StairsBlock(FIR_PLANKS.getDefaultState(),
            AbstractBlock.Settings.copy(FIR_PLANKS)
                    .registryKey(blockkey("fir_stairs")));

    public static final Block FIR_FENCE = new FenceBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0f, 3.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
                    .registryKey(blockkey("fir_fence")));

    public static final Block FIR_FENCE_GATE = new FenceGateBlock(WoodType.SPRUCE,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0f, 3.0f)
                    .burnable()
                    .registryKey(blockkey("fir_fence_gate")));

    public static final Block FIR_DOOR = new DoorBlock(BlockSetType.SPRUCE,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(3.0f)
                    .nonOpaque()
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(blockkey("fir_door")));

    public static final Block FIR_TRAPDOOR = new TrapdoorBlock(BlockSetType.SPRUCE,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(3.0f)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .burnable()
                    .registryKey(blockkey("fir_trapdoor")));

    public static final Block FIR_BUTTON = new ButtonBlock(BlockSetType.SPRUCE, 30,
            AbstractBlock.Settings.create()
                    .noCollision()
                    .strength(0.5f)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(blockkey("fir_button")));

    public static final Block FIR_PRESSURE_PLATE = new PressurePlateBlock(BlockSetType.SPRUCE,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(0.5f)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(blockkey("fir_pressure_plate")));

    public static final Block FIR_SIGN = new SignBlock(FIR_WOOD_TYPE,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
                    .registryKey(blockkey("fir_sign")));

    public static final Block FIR_WALL_SIGN = new WallSignBlock(FIR_WOOD_TYPE,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
                    .lootTable(FIR_SIGN.getLootTableKey())
                    .registryKey(blockkey("fir_wall_sign")));

    public static final Block FIR_HANGING_SIGN = new HangingSignBlock(FIR_WOOD_TYPE,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
                    .registryKey(blockkey("fir_hanging_sign")));

    public static final Block FIR_WALL_HANGING_SIGN = new WallHangingSignBlock(FIR_WOOD_TYPE,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
                    .lootTable(FIR_HANGING_SIGN.getLootTableKey())
                    .registryKey(blockkey("fir_wall_hanging_sign")));

    public static final Block FIR_SHELF = new ShelfBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .sounds(BlockSoundGroup.SHELF)
                    .burnable()
                    .strength(2.0f, 3.0f)
                    .registryKey(blockkey("fir_shelf")));

    public static final Block STRIPPED_FIR_LOG = new PillarBlock(
            Blocks.createLogSettings(MapColor.SPRUCE_BROWN, MapColor.SPRUCE_BROWN, BlockSoundGroup.WOOD)
                    .registryKey(blockkey("stripped_fir_log")));

    public static final Block STRIPPED_FIR_WOOD = new PillarBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
                    .registryKey(blockkey("stripped_fir_wood")));

    public static final Block JACARANDA_LOG = new PillarBlock(
            Blocks.createLogSettings(MapColor.SPRUCE_BROWN, MapColor.BROWN, BlockSoundGroup.WOOD)
                    .registryKey(blockkey("jacaranda_log")));

    public static final Block JACARANDA_WOOD = new PillarBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
                    .registryKey(blockkey("jacaranda_wood")));

    public static final Block JACARANDA_PLANKS = new Block(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0f, 3.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
                    .registryKey(blockkey("jacaranda_planks")));

    public static final Block JACARANDA_LEAVES = new TintedParticleLeavesBlock(0.01f,
            Blocks.createLeavesSettings(BlockSoundGroup.GRASS)
                    .registryKey(blockkey("jacaranda_leaves")));

    public static final Block JACARANDA_SAPLING = new SaplingBlock(
            JACARANDA_SAPLING_GENERATOR,
            AbstractBlock.Settings.copy(Blocks.SPRUCE_SAPLING)
                    .registryKey(blockkey("jacaranda_sapling")));

    public static final Block JACARANDA_SLAB = new SlabBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0f, 3.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
                    .registryKey(blockkey("jacaranda_slab")));

    public static final Block JACARANDA_STAIRS = new StairsBlock(JACARANDA_PLANKS.getDefaultState(),
            AbstractBlock.Settings.copy(JACARANDA_PLANKS)
                    .registryKey(blockkey("jacaranda_stairs")));

    public static final Block JACARANDA_FENCE = new FenceBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0f, 3.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
                    .registryKey(blockkey("jacaranda_fence")));

    public static final Block JACARANDA_FENCE_GATE = new FenceGateBlock(WoodType.SPRUCE,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0f, 3.0f)
                    .burnable()
                    .registryKey(blockkey("jacaranda_fence_gate")));

    public static final Block JACARANDA_DOOR = new DoorBlock(BlockSetType.SPRUCE,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(3.0f)
                    .nonOpaque()
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(blockkey("jacaranda_door")));

    public static final Block JACARANDA_TRAPDOOR = new TrapdoorBlock(BlockSetType.SPRUCE,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(3.0f)
                    .nonOpaque()
                    .allowsSpawning(Blocks::never)
                    .burnable()
                    .registryKey(blockkey("jacaranda_trapdoor")));

    public static final Block JACARANDA_BUTTON = new ButtonBlock(BlockSetType.SPRUCE, 30,
            AbstractBlock.Settings.create()
                    .noCollision()
                    .strength(0.5f)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(blockkey("jacaranda_button")));

    public static final Block JACARANDA_PRESSURE_PLATE = new PressurePlateBlock(BlockSetType.SPRUCE,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(0.5f)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(blockkey("jacaranda_pressure_plate")));

    public static final Block JACARANDA_SIGN = new SignBlock(JACARANDA_WOOD_TYPE,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
                    .registryKey(blockkey("jacaranda_sign")));

    public static final Block JACARANDA_WALL_SIGN = new WallSignBlock(JACARANDA_WOOD_TYPE,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
                    .lootTable(JACARANDA_SIGN.getLootTableKey())
                    .registryKey(blockkey("jacaranda_wall_sign")));

    public static final Block JACARANDA_HANGING_SIGN = new HangingSignBlock(JACARANDA_WOOD_TYPE,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
                    .registryKey(blockkey("jacaranda_hanging_sign")));

    public static final Block JACARANDA_WALL_HANGING_SIGN = new WallHangingSignBlock(JACARANDA_WOOD_TYPE,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
                    .lootTable(JACARANDA_HANGING_SIGN.getLootTableKey())
                    .registryKey(blockkey("jacaranda_wall_hanging_sign")));

    public static final Block JACARANDA_SHELF = new ShelfBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .sounds(BlockSoundGroup.SHELF)
                    .burnable()
                    .strength(2.0f, 3.0f)
                    .registryKey(blockkey("jacaranda_shelf")));

    public static final Block STRIPPED_JACARANDA_LOG = new PillarBlock(
            Blocks.createLogSettings(MapColor.SPRUCE_BROWN, MapColor.SPRUCE_BROWN, BlockSoundGroup.WOOD)
                    .registryKey(blockkey("stripped_jacaranda_log")));

    public static final Block STRIPPED_JACARANDA_WOOD = new PillarBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
                    .registryKey(blockkey("stripped_jacaranda_wood")));

    public static final Block LAVENDER = new FlowerBlock(StatusEffects.SLOW_FALLING, 8,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PURPLE)
                    .noCollision().breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(blockkey("lavender")));

    public static final Block SPROUT = new ShortPlantBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision().breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(blockkey("sprout")));

    public static final Block CLOVER = new FlowerbedBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision().breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(blockkey("clover")));

    public static final Block PINK_DAFFODIL = new FlowerBlock(StatusEffects.INVISIBILITY, 8,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PINK)
                    .noCollision().breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(blockkey("pink_daffodil")));

    public static final Block ORANGE_COSMOS = new FlowerBlock(StatusEffects.ABSORPTION, 8,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .noCollision().breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(blockkey("orange_cosmos")));

    public static final Block BURNING_BLOSSOM = new FlowerBlock(StatusEffects.FIRE_RESISTANCE, 8,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .noCollision().breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .luminance(state -> 7)
                    .registryKey(blockkey("burning_blossom")));

    public static final Block BLUE_HYDRANGEA = new TallPlantBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_PURPLE)
                    .noCollision().breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(blockkey("blue_hydrangea")));

    public static final Block MARIGOLD = new FlowerBlock(StatusEffects.SPEED, 8,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.YELLOW)
                    .noCollision().breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(blockkey("marigold")));

    public static final Block VIOLET = new FlowerBlock(StatusEffects.NAUSEA, 8,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PURPLE)
                    .noCollision().breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(blockkey("violet")));

    public static final Block SNOWBLOSSOM_LEAVES = new TintedParticleLeavesBlock(0.01F,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WHITE)
                    .strength(0.2f)
                    .ticksRandomly()
                    .sounds(BlockSoundGroup.CHERRY_LEAVES)
                    .allowsSpawning((state, world, pos, type) -> false)
                    .suffocates((state, world, pos) -> false)
                    .blockVision((state, world, pos) -> false)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(blockkey("snowblossom_leaves")));


    public static void registerModBlocks() {
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "primevelite_block"), PRIMEVELITE_BLOCK);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "silver_block"), SILVER_BLOCK);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "primevelite_ore"), PRIMEVELITE_ORE);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "silver_ore"), SILVER_ORE);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "blue_fire"), BLUE_FIRE);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "deepslate_primevelite_ore"), DEEPSLATE_PRIMEVELITE_ORE);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "deepslate_silver_ore"), DEEPSLATE_SILVER_ORE);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "fir_log"), FIR_LOG);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "fir_wood"), FIR_WOOD);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "fir_planks"), FIR_PLANKS);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "fir_leaves"), FIR_LEAVES);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "fir_sapling"), FIR_SAPLING);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "fir_slab"), FIR_SLAB);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "fir_stairs"), FIR_STAIRS);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "fir_fence"), FIR_FENCE);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "fir_fence_gate"), FIR_FENCE_GATE);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "fir_door"), FIR_DOOR);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "fir_trapdoor"), FIR_TRAPDOOR);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "fir_button"), FIR_BUTTON);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "fir_pressure_plate"), FIR_PRESSURE_PLATE);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "fir_sign"), FIR_SIGN);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "fir_wall_sign"), FIR_WALL_SIGN);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "fir_hanging_sign"), FIR_HANGING_SIGN);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "fir_wall_hanging_sign"), FIR_WALL_HANGING_SIGN);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "fir_shelf"), FIR_SHELF);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "stripped_fir_log"), STRIPPED_FIR_LOG);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "stripped_fir_wood"), STRIPPED_FIR_WOOD);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_log"), JACARANDA_LOG);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_wood"), JACARANDA_WOOD);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_planks"), JACARANDA_PLANKS);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_leaves"), JACARANDA_LEAVES);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_sapling"), JACARANDA_SAPLING);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_slab"), JACARANDA_SLAB);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_stairs"), JACARANDA_STAIRS);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_fence"), JACARANDA_FENCE);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_fence_gate"), JACARANDA_FENCE_GATE);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_door"), JACARANDA_DOOR);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_trapdoor"), JACARANDA_TRAPDOOR);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_button"), JACARANDA_BUTTON);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_pressure_plate"), JACARANDA_PRESSURE_PLATE);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_sign"), JACARANDA_SIGN);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_wall_sign"), JACARANDA_WALL_SIGN);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_hanging_sign"), JACARANDA_HANGING_SIGN);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_wall_hanging_sign"), JACARANDA_WALL_HANGING_SIGN);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_shelf"), JACARANDA_SHELF);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "stripped_jacaranda_log"), STRIPPED_JACARANDA_LOG);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "stripped_jacaranda_wood"), STRIPPED_JACARANDA_WOOD);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "lavender"), LAVENDER);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "sprout"), SPROUT);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "clover"), CLOVER);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "pink_daffodil"), PINK_DAFFODIL);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "orange_cosmos"), ORANGE_COSMOS);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "burning_blossom"), BURNING_BLOSSOM);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "blue_hydrangea"), BLUE_HYDRANGEA);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "marigold"), MARIGOLD);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "violet"), VIOLET);
        Registry.register(Registries.BLOCK, Identifier.of(OriginsDimension.MOD_ID, "snowblossom_leaves"), SNOWBLOSSOM_LEAVES);


        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "primevelite_block"),
                new BlockItem(PRIMEVELITE_BLOCK, new Item.Settings().registryKey(itemkey("primevelite_block"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "silver_block"),
                new BlockItem(SILVER_BLOCK, new Item.Settings().registryKey(itemkey("silver_block"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "primevelite_ore"),
                new BlockItem(PRIMEVELITE_ORE, new Item.Settings().registryKey(itemkey("primevelite_ore"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "silver_ore"),
                new BlockItem(SILVER_ORE, new Item.Settings().registryKey(itemkey("silver_ore"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "deepslate_primevelite_ore"),
                new BlockItem(DEEPSLATE_PRIMEVELITE_ORE, new Item.Settings().registryKey(itemkey("deepslate_primevelite_ore"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "deepslate_silver_ore"),
                new BlockItem(DEEPSLATE_SILVER_ORE, new Item.Settings().registryKey(itemkey("deepslate_silver_ore"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "fir_log"),
                new BlockItem(FIR_LOG, new Item.Settings().registryKey(itemkey("fir_log"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "fir_wood"),
                new BlockItem(FIR_WOOD, new Item.Settings().registryKey(itemkey("fir_wood"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "fir_planks"),
                new BlockItem(FIR_PLANKS, new Item.Settings().registryKey(itemkey("fir_planks"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "fir_leaves"),
                new BlockItem(FIR_LEAVES, new Item.Settings().registryKey(itemkey("fir_leaves"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "fir_sapling"),
                new BlockItem(FIR_SAPLING, new Item.Settings().registryKey(itemkey("fir_sapling"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "fir_slab"),
                new BlockItem(FIR_SLAB, new Item.Settings().registryKey(itemkey("fir_slab"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "fir_stairs"),
                new BlockItem(FIR_STAIRS, new Item.Settings().registryKey(itemkey("fir_stairs"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "fir_fence"),
                new BlockItem(FIR_FENCE, new Item.Settings().registryKey(itemkey("fir_fence"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "fir_fence_gate"),
                new BlockItem(FIR_FENCE_GATE, new Item.Settings().registryKey(itemkey("fir_fence_gate"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "fir_door"),
                new BlockItem(FIR_DOOR, new Item.Settings().registryKey(itemkey("fir_door"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "fir_trapdoor"),
                new BlockItem(FIR_TRAPDOOR, new Item.Settings().registryKey(itemkey("fir_trapdoor"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "fir_button"),
                new BlockItem(FIR_BUTTON, new Item.Settings().registryKey(itemkey("fir_button"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "fir_pressure_plate"),
                new BlockItem(FIR_PRESSURE_PLATE, new Item.Settings().registryKey(itemkey("fir_pressure_plate"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "fir_sign"),
                new BlockItem(FIR_SIGN, new Item.Settings().registryKey(itemkey("fir_sign"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "fir_hanging_sign"),
                new HangingSignItem(FIR_HANGING_SIGN, FIR_WALL_HANGING_SIGN,
                        new Item.Settings().registryKey(itemkey("fir_hanging_sign"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "fir_shelf"),
                new BlockItem(FIR_SHELF, new Item.Settings().registryKey(itemkey("fir_shelf"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "stripped_fir_log"),
                new BlockItem(STRIPPED_FIR_LOG, new Item.Settings().registryKey(itemkey("stripped_fir_log"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "stripped_fir_wood"),
                new BlockItem(STRIPPED_FIR_WOOD, new Item.Settings().registryKey(itemkey("stripped_fir_wood"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_log"),
                new BlockItem(JACARANDA_LOG, new Item.Settings().registryKey(itemkey("jacaranda_log"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_wood"),
                new BlockItem(JACARANDA_WOOD, new Item.Settings().registryKey(itemkey("jacaranda_wood"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_planks"),
                new BlockItem(JACARANDA_PLANKS, new Item.Settings().registryKey(itemkey("jacaranda_planks"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_leaves"),
                new BlockItem(JACARANDA_LEAVES, new Item.Settings().registryKey(itemkey("jacaranda_leaves"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_sapling"),
                new BlockItem(JACARANDA_SAPLING, new Item.Settings().registryKey(itemkey("jacaranda_sapling"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_slab"),
                new BlockItem(JACARANDA_SLAB, new Item.Settings().registryKey(itemkey("jacaranda_slab"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_stairs"),
                new BlockItem(JACARANDA_STAIRS, new Item.Settings().registryKey(itemkey("jacaranda_stairs"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_fence"),
                new BlockItem(JACARANDA_FENCE, new Item.Settings().registryKey(itemkey("jacaranda_fence"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_fence_gate"),
                new BlockItem(JACARANDA_FENCE_GATE, new Item.Settings().registryKey(itemkey("jacaranda_fence_gate"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_door"),
                new BlockItem(JACARANDA_DOOR, new Item.Settings().registryKey(itemkey("jacaranda_door"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_trapdoor"),
                new BlockItem(JACARANDA_TRAPDOOR, new Item.Settings().registryKey(itemkey("jacaranda_trapdoor"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_button"),
                new BlockItem(JACARANDA_BUTTON, new Item.Settings().registryKey(itemkey("jacaranda_button"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_pressure_plate"),
                new BlockItem(JACARANDA_PRESSURE_PLATE, new Item.Settings().registryKey(itemkey("jacaranda_pressure_plate"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_sign"),
                new BlockItem(JACARANDA_SIGN, new Item.Settings().registryKey(itemkey("jacaranda_sign"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_hanging_sign"),
                new HangingSignItem(JACARANDA_HANGING_SIGN, JACARANDA_WALL_HANGING_SIGN,
                        new Item.Settings().registryKey(itemkey("jacaranda_hanging_sign"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "jacaranda_shelf"),
                new BlockItem(JACARANDA_SHELF, new Item.Settings().registryKey(itemkey("jacaranda_shelf"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "stripped_jacaranda_log"),
                new BlockItem(STRIPPED_JACARANDA_LOG, new Item.Settings().registryKey(itemkey("stripped_jacaranda_log"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "stripped_jacaranda_wood"),
                new BlockItem(STRIPPED_JACARANDA_WOOD, new Item.Settings().registryKey(itemkey("stripped_jacaranda_wood"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "lavender"),
                new BlockItem(LAVENDER, new Item.Settings().registryKey(itemkey("lavender"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "sprout"),
                new BlockItem(SPROUT, new Item.Settings().registryKey(itemkey("sprout"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "clover"),
                new BlockItem(CLOVER, new Item.Settings().registryKey(itemkey("clover"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "pink_daffodil"),
                new BlockItem(PINK_DAFFODIL, new Item.Settings().registryKey(itemkey("pink_daffodil"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "orange_cosmos"),
                new BlockItem(ORANGE_COSMOS, new Item.Settings().registryKey(itemkey("orange_cosmos"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "burning_blossom"),
                new BlockItem(BURNING_BLOSSOM, new Item.Settings().registryKey(itemkey("burning_blossom"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "blue_hydrangea"),
                new BlockItem(BLUE_HYDRANGEA, new Item.Settings().registryKey(itemkey("blue_hydrangea"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "marigold"),
                new BlockItem(MARIGOLD, new Item.Settings().registryKey(itemkey("marigold"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "violet"),
                new BlockItem(VIOLET, new Item.Settings().registryKey(itemkey("violet"))));
        Registry.register(Registries.ITEM, Identifier.of(OriginsDimension.MOD_ID, "snowblossom_leaves"),
                new BlockItem(SNOWBLOSSOM_LEAVES, new Item.Settings().registryKey(itemkey("snowblossom_leaves"))));

    }
}
