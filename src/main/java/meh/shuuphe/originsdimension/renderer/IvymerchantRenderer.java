package meh.shuuphe.originsdimension.renderer;

import meh.shuuphe.originsdimension.entity.ability.ivymerchant.Ivymerchant;
import meh.shuuphe.originsdimension.models.IvymerchantModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class IvymerchantRenderer extends GeoEntityRenderer<Ivymerchant, RenderState> {

    public IvymerchantRenderer(EntityRendererFactory.Context context) {
        super(context, new IvymerchantModel());
    }
}