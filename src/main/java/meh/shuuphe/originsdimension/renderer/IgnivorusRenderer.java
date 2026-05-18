package meh.shuuphe.originsdimension.renderer;

import meh.shuuphe.originsdimension.entity.ability.ignivorus.Ignivorus;
import meh.shuuphe.originsdimension.models.IgnivorusModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class IgnivorusRenderer extends GeoEntityRenderer<Ignivorus, RenderState> {

    public IgnivorusRenderer(EntityRendererFactory.Context context) {
        super(context, new IgnivorusModel());
    }
}