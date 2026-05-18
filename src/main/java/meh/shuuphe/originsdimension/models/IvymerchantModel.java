package meh.shuuphe.originsdimension.models;

import meh.shuuphe.originsdimension.entity.ability.ivymerchant.Ivymerchant;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class IvymerchantModel extends GeoModel<Ivymerchant> {

    private static final Identifier TEXTURE   = Identifier.of("originsdimension", "textures/entity/ivymerchant/ivy_oleander.png");
    private static final Identifier MODEL     = Identifier.of("originsdimension", "entity/ivy_oleander");
    private static final Identifier ANIMATION = Identifier.of("originsdimension", "entity/ivy_oleander");

    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return MODEL;
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return TEXTURE;
    }

    @Override
    public Identifier getAnimationResource(Ivymerchant animatable) {
        return ANIMATION;
    }
}
