package meh.shuuphe.originsdimension.renderer;

import meh.shuuphe.originsdimension.entity.ability.cindervane.Cindervane;
import meh.shuuphe.originsdimension.models.CindervaneModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CindervaneRenderer extends GeoEntityRenderer<Cindervane, RenderState> {

    public CindervaneRenderer(EntityRendererFactory.Context context) {
        super(context, new CindervaneModel());
    }
}