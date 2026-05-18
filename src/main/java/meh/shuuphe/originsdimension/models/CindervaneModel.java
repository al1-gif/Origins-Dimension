package meh.shuuphe.originsdimension.models;

import meh.shuuphe.originsdimension.entity.ability.cindervane.Cindervane;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class CindervaneModel extends GeoModel<Cindervane> {

    private static final Identifier TEXTURE   = Identifier.of("originsdimension", "textures/entity/cindervane/cindervane.png");
    private static final Identifier MODEL     = Identifier.of("originsdimension", "entity/cindervane");
    private static final Identifier ANIMATION = Identifier.of("originsdimension", "entity/cindervane");

    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return MODEL;
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return TEXTURE;
    }

    @Override
    public Identifier getAnimationResource(Cindervane animatable) {
        return ANIMATION;
    }
}