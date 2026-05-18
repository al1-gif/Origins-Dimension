package meh.shuuphe.originsdimension.models;

import meh.shuuphe.originsdimension.entity.ability.stegonaut.Stegonaut;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class StegonautModel extends GeoModel<Stegonaut> {

    private static final Identifier TEXTURE   = Identifier.of("originsdimension", "textures/entity/stegonaut/stegonaut.png");
    private static final Identifier MODEL     = Identifier.of("originsdimension", "entity/stegonaut");
    private static final Identifier ANIMATION = Identifier.of("originsdimension", "entity/stegonaut");

    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return MODEL;
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return TEXTURE;
    }

    @Override
    public Identifier getAnimationResource(Stegonaut animatable) {
        return ANIMATION;
    }
}
