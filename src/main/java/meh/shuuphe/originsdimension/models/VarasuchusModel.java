package meh.shuuphe.originsdimension.models;

import meh.shuuphe.originsdimension.entity.ability.varasuchus.Varasuchus;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class VarasuchusModel extends GeoModel<Varasuchus> {

    private static final Identifier TEXTURE   = Identifier.of("originsdimension", "textures/entity/varasuchus/varasuchus.png");
    private static final Identifier MODEL     = Identifier.of("originsdimension", "entity/varasuchus");
    private static final Identifier ANIMATION = Identifier.of("originsdimension", "entity/varasuchus");

    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return MODEL;
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return TEXTURE;
    }

    @Override
    public Identifier getAnimationResource(Varasuchus animatable) {
        return ANIMATION;
    }
}