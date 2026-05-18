package meh.shuuphe.originsdimension.registry;

import meh.shuuphe.originsdimension.OriginsDimension;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;


public class ModSounds {
    public static SoundEvent register(String name) {
        Identifier id = Identifier.of(OriginsDimension.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static final SoundEvent IVY_REACTION_TO_EGG =
            register("ivy_reaction_to_egg");
    public static final SoundEvent IVY_TRADE_START =
            register("ivy_trade_start");
    public static final SoundEvent IVY_TRADE_STOP =
            register("ivy_trade_stop");
    public static final SoundEvent NULLJAW_DIE =
            register("nulljaw_die");
    public static final SoundEvent NULLJAW_EAT =
            register("nulljaw_eat");
    public static final SoundEvent NULLJAW_GRUMBLE1 =
            register("nulljaw_grumble1");
    public static final SoundEvent NULLJAW_GRUMBLE2 =
            register("nulljaw_grumble2");
    public static final SoundEvent NULLJAW_GRUMBLE3 =
            register("nulljaw_grumble3");
    public static final SoundEvent NULLJAW_HURT =
            register("nulljaw_hurt");
    public static final SoundEvent CINDERVANE_BITE =
            register("cindervane_bite");
    public static final SoundEvent CINDERVANE_DIE =
            register("cindervane_die");
    public static final SoundEvent CINDERVANE_EAT =
            register("cindervane_eat");
    public static final SoundEvent CINDERVANE_FLAP =
            register("cindervane_flap");
    public static final SoundEvent CINDERVANE_GRUMBLE =
            register("cindervane_grumble");
    public static final SoundEvent CINDERVANE_HURT =
            register("cindervane_hurt");
    public static final SoundEvent CINDERVANE_LANDED =
            register("cindervane_landed");
    public static final SoundEvent CINDERVANE_MAGMA_BLAST=
            register("cindervane_magma_blast");
    public static final SoundEvent CINDERVANE_ROAR =
            register("cindervane_roar");
    public static final SoundEvent CINDERVANE_RUN =
            register("cindervane_run");
    public static final SoundEvent CINDERVANE_SLASH =
            register("cindervane_slash");
    public static final SoundEvent CINDERVANE_TAKEOFF =
            register("cindervane_takeoff");

    public static void registerModEvents() {}
}
