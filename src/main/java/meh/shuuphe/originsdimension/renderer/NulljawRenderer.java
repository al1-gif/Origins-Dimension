package meh.shuuphe.originsdimension.renderer;

import meh.shuuphe.originsdimension.entity.ability.nulljaw.Nulljaw;
import meh.shuuphe.originsdimension.models.NulljawModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NulljawRenderer extends GeoEntityRenderer<Nulljaw, RenderState> {

    public NulljawRenderer(EntityRendererFactory.Context context) {
        super(context, new NulljawModel());
    }
}