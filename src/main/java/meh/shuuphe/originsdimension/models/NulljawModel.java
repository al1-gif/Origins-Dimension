package meh.shuuphe.originsdimension.models;

import meh.shuuphe.originsdimension.entity.ability.nulljaw.Nulljaw;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class NulljawModel extends GeoModel<Nulljaw> {

    private static final Identifier TEXTURE   = Identifier.of("originsdimension", "textures/entity/nulljaw/nulljaw.png");
    private static final Identifier MODEL     = Identifier.of("originsdimension", "entity/nulljaw");
    private static final Identifier ANIMATION = Identifier.of("originsdimension", "entity/nulljaw");

    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return MODEL;
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return TEXTURE;
    }

    @Override
    public Identifier getAnimationResource(Nulljaw animatable) {
        return ANIMATION;
    }
}
