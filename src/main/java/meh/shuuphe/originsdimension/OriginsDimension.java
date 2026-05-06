package meh.shuuphe.originsdimension;

import net.fabricmc.api.ModInitializer;

import meh.shuuphe.customportalapi.api.CustomPortalBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OriginsDimension implements ModInitializer {
	public static final String MOD_ID = "originsdimension";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Origins Dimension starting!!!...");
		ModDimensions.register();
		ModBiomes.register();

		CustomPortalBuilder.beginPortal()
				.frameBlock(Blocks.CRYING_OBSIDIAN)
				.lightWithWater()
				.destDimID(Identifier.of("originsdimension", "origin"))
				.tintColor(255, 200, 0)
				.registerPortal();
	}
}