package meh.shuuphe.originsdimension.models;

import net.minecraft.util.Identifier;
import meh.shuuphe.originsdimension.entity.ability.ignivorus.Ignivorus;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class IgnivorusModel extends GeoModel<Ignivorus> {

    private static final Identifier TEXTURE   = Identifier.of("originsdimension", "textures/entity/ignivorus/ignivorus.png");
    private static final Identifier MODEL     = Identifier.of("originsdimension", "entity/ignivorus");
    private static final Identifier ANIMATION = Identifier.of("originsdimension", "entity/ignivorus");

    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return MODEL;
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return TEXTURE;
    }

    @Override
    public Identifier getAnimationResource(Ignivorus animatable) {
        return ANIMATION;
    }
}