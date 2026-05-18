package meh.shuuphe.originsdimension;

import meh.shuuphe.originsdimension.entity.ability.cindervane.Cindervane;
import meh.shuuphe.originsdimension.entity.ability.ignivorus.Ignivorus;
import meh.shuuphe.originsdimension.entity.ability.ivymerchant.Ivymerchant;
import meh.shuuphe.originsdimension.entity.ability.nulljaw.Nulljaw;
import meh.shuuphe.originsdimension.entity.ability.stegonaut.Stegonaut;
import meh.shuuphe.originsdimension.entity.ability.varasuchus.Varasuchus;
import meh.shuuphe.originsdimension.entity.ability.volitans.Volitans;
import meh.shuuphe.originsdimension.registry.*;
import meh.shuuphe.originsdimension.worldgen.feature.MushroomTreeFeature;
import meh.shuuphe.originsdimension.worldgen.feature.NbtFossilFeature;
import net.fabricmc.api.ModInitializer;

import meh.shuuphe.customportalapi.api.CustomPortalBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OriginsDimension implements ModInitializer {
	public static final String MOD_ID = "originsdimension";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Feature<DefaultFeatureConfig> MUSHROOM_TREE =
			Registry.register(Registries.FEATURE,
					Identifier.of(MOD_ID, "mushroom_tree"),
					new MushroomTreeFeature(DefaultFeatureConfig.CODEC));
	public static final Feature<DefaultFeatureConfig> NBT_FOSSIL =
			Registry.register(Registries.FEATURE,
					Identifier.of(MOD_ID, "nbt_fossil"),
					new NbtFossilFeature(DefaultFeatureConfig.CODEC));

	@Override
	public void onInitialize() {
		LOGGER.info("Origins Dimension starting!!!...");
		ModDimensions.register();
		ModBiomes.register();
		ModBlocks.registerModBlocks();
		ModItems.registerModItems();
		ModEntities.register();
		ModCreativeTab.registerCreativeTab();
		try {
			java.lang.reflect.Field f = net.minecraft.block.entity.BlockEntityType.class.getDeclaredField("blocks");
			f.setAccessible(true);

			@SuppressWarnings("unchecked")
			java.util.Set<net.minecraft.block.Block> shelfBlocks =
					(java.util.Set<net.minecraft.block.Block>) f.get(net.minecraft.block.entity.BlockEntityType.SHELF);
			shelfBlocks.add(ModBlocks.FIR_SHELF);
			shelfBlocks.add(ModBlocks.JACARANDA_SHELF);

			@SuppressWarnings("unchecked")
			java.util.Set<net.minecraft.block.Block> signBlocks =
					(java.util.Set<net.minecraft.block.Block>) f.get(net.minecraft.block.entity.BlockEntityType.SIGN);
			signBlocks.add(ModBlocks.FIR_SIGN);
			signBlocks.add(ModBlocks.FIR_WALL_SIGN);
			signBlocks.add(ModBlocks.JACARANDA_SIGN);
			signBlocks.add(ModBlocks.JACARANDA_WALL_SIGN);

			@SuppressWarnings("unchecked")
			java.util.Set<net.minecraft.block.Block> hangingSignBlocks =
					(java.util.Set<net.minecraft.block.Block>) f.get(net.minecraft.block.entity.BlockEntityType.HANGING_SIGN);
			hangingSignBlocks.add(ModBlocks.FIR_HANGING_SIGN);
			hangingSignBlocks.add(ModBlocks.FIR_WALL_HANGING_SIGN);
			hangingSignBlocks.add(ModBlocks.JACARANDA_HANGING_SIGN);
			hangingSignBlocks.add(ModBlocks.JACARANDA_WALL_HANGING_SIGN);

		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Failed to patch block entity types", e);
		}
		ModSounds.registerModEvents();

		FabricDefaultAttributeRegistry.register(ModEntities.VARASUCHUS, Varasuchus.createAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.CINDERVANE, Cindervane.createAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.IVYMERCHANT, Ivymerchant.createAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.NULLJAW, Nulljaw.createAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.STEGONAUT, Stegonaut.createAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.IGNIVORUS, Ignivorus.createAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.VOLITANS, Volitans.createAttributes());
		StrippableBlockRegistry.register(ModBlocks.FIR_LOG, ModBlocks.STRIPPED_FIR_LOG);
		StrippableBlockRegistry.register(ModBlocks.FIR_WOOD, ModBlocks.STRIPPED_FIR_WOOD);
		StrippableBlockRegistry.register(ModBlocks.JACARANDA_LOG, ModBlocks.STRIPPED_JACARANDA_LOG);
		StrippableBlockRegistry.register(ModBlocks.JACARANDA_WOOD, ModBlocks.STRIPPED_JACARANDA_WOOD);

		CustomPortalBuilder.beginPortal()
				.frameBlock(Blocks.COPPER_BLOCK)
				.lightWithItem(ModItems.SYNCOPE)
				.destDimID(Identifier.of("originsdimension", "origin"))
				.tintColor(0, 153, 154)
				.registerPortal();
	}
}