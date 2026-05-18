package meh.shuuphe.originsdimension.renderer;

import meh.shuuphe.originsdimension.entity.ability.varasuchus.Varasuchus;
import meh.shuuphe.originsdimension.models.VarasuchusModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class VarasuchusRenderer extends GeoEntityRenderer<Varasuchus, RenderState> {

    public VarasuchusRenderer(EntityRendererFactory.Context context) {
        super(context, new VarasuchusModel());
    }
}