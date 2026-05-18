package meh.shuuphe.originsdimension.models;

import meh.shuuphe.originsdimension.entity.ability.volitans.Volitans;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class VolitansModel extends GeoModel<Volitans> {

    private static final Identifier TEXTURE   = Identifier.of("originsdimension", "textures/entity/volitans/volitans.png");
    private static final Identifier MODEL     = Identifier.of("originsdimension", "entity/volitans");
    private static final Identifier ANIMATION = Identifier.of("originsdimension", "entity/volitans");

    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return MODEL;
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return TEXTURE;
    }

    @Override
    public Identifier getAnimationResource(Volitans animatable) {
        return ANIMATION;
    }
}