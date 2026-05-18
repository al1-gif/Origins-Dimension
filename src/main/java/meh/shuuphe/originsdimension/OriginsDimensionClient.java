package meh.shuuphe.originsdimension;

import meh.shuuphe.originsdimension.renderer.CindervaneRenderer;
import meh.shuuphe.originsdimension.renderer.IgnivorusRenderer;
import meh.shuuphe.originsdimension.renderer.IvymerchantRenderer;
import meh.shuuphe.originsdimension.renderer.NulljawRenderer;
import meh.shuuphe.originsdimension.renderer.StegonautRenderer;
import meh.shuuphe.originsdimension.renderer.VarasuchusRenderer;
import meh.shuuphe.originsdimension.renderer.VolitansRenderer;
import meh.shuuphe.originsdimension.registry.ModBlocks;
import meh.shuuphe.originsdimension.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class OriginsDimensionClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.putBlocks(BlockRenderLayer.CUTOUT,
                ModBlocks.JACARANDA_DOOR,
                ModBlocks.JACARANDA_TRAPDOOR,
                ModBlocks.BLUE_FIRE,
                ModBlocks.FIR_SAPLING,
                ModBlocks.JACARANDA_SAPLING,
                ModBlocks.CLOVER,
                ModBlocks.SPROUT,
                ModBlocks.LAVENDER,
                ModBlocks.PINK_DAFFODIL,
                ModBlocks.ORANGE_COSMOS,
                ModBlocks.BURNING_BLOSSOM,
                ModBlocks.BLUE_HYDRANGEA,
                ModBlocks.MARIGOLD,
                ModBlocks.VIOLET,
                ModBlocks.SNOWBLOSSOM_LEAVES
        );

        EntityRendererRegistry.register(ModEntities.VARASUCHUS, VarasuchusRenderer::new);
        EntityRendererRegistry.register(ModEntities.CINDERVANE, CindervaneRenderer::new);
        EntityRendererRegistry.register(ModEntities.IVYMERCHANT, IvymerchantRenderer::new);
        EntityRendererRegistry.register(ModEntities.NULLJAW, NulljawRenderer::new);
        EntityRendererRegistry.register(ModEntities.STEGONAUT, StegonautRenderer::new);
        EntityRendererRegistry.register(ModEntities.IGNIVORUS, IgnivorusRenderer::new);
        EntityRendererRegistry.register(ModEntities.VOLITANS, VolitansRenderer::new);
        EntityRendererRegistry.register(ModEntities.CINDERVANE_MAGMA_BLOCK, ctx -> new FlyingItemEntityRenderer(ctx));
    }
}
