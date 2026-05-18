package meh.shuuphe.originsdimension.renderer;

import meh.shuuphe.originsdimension.entity.ability.volitans.Volitans;
import meh.shuuphe.originsdimension.models.VolitansModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class VolitansRenderer extends GeoEntityRenderer<Volitans, RenderState> {

    public VolitansRenderer(EntityRendererFactory.Context context) {
        super(context, new VolitansModel());
    }
}