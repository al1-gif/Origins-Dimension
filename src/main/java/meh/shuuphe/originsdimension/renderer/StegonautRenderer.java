package meh.shuuphe.originsdimension.renderer;

import meh.shuuphe.originsdimension.entity.ability.stegonaut.Stegonaut;
import meh.shuuphe.originsdimension.models.StegonautModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class StegonautRenderer extends GeoEntityRenderer<Stegonaut, RenderState> {

    public StegonautRenderer(EntityRendererFactory.Context context) {
        super(context, new StegonautModel());
    }
}