package meh.shuuphe.originsdimension.registry;

import meh.shuuphe.originsdimension.OriginsDimension;
import meh.shuuphe.originsdimension.entity.ability.cindervane.CindervaneMagmaBlock;
import meh.shuuphe.originsdimension.entity.ability.ignivorus.Ignivorus;
import meh.shuuphe.originsdimension.entity.ability.ivymerchant.Ivymerchant;
import meh.shuuphe.originsdimension.entity.ability.nulljaw.Nulljaw;
import meh.shuuphe.originsdimension.entity.ability.stegonaut.Stegonaut;
import meh.shuuphe.originsdimension.entity.ability.varasuchus.Varasuchus;
import meh.shuuphe.originsdimension.entity.ability.cindervane.Cindervane;
import meh.shuuphe.originsdimension.entity.ability.volitans.Volitans;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<Varasuchus> VARASUCHUS = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(OriginsDimension.MOD_ID, "varasuchus"),
            EntityType.Builder.<Varasuchus>create(Varasuchus::new, SpawnGroup.MONSTER)
                    .dimensions(4.5f, 3.5f)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,
                            Identifier.of(OriginsDimension.MOD_ID, "varasuchus")))
    );

    public static final EntityType<Cindervane> CINDERVANE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(OriginsDimension.MOD_ID, "cindervane"),
            EntityType.Builder.<Cindervane>create(Cindervane::new, SpawnGroup.MONSTER)
                    .dimensions(2.5f, 2.5f)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,
                            Identifier.of(OriginsDimension.MOD_ID, "cindervane")))
    );

    public static final EntityType<Ivymerchant> IVYMERCHANT = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(OriginsDimension.MOD_ID, "ivymerchant"),
            EntityType.Builder.<Ivymerchant>create(Ivymerchant::new, SpawnGroup.MONSTER)
                    .dimensions(0.6f, 1.95f)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,
                            Identifier.of(OriginsDimension.MOD_ID, "ivymerchant")))
    );

    public static final EntityType<Nulljaw> NULLJAW = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(OriginsDimension.MOD_ID, "nulljaw"),
            EntityType.Builder.<Nulljaw>create(Nulljaw::new, SpawnGroup.MONSTER)
                    .dimensions(1.30f, 1.25f)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,
                            Identifier.of(OriginsDimension.MOD_ID, "nulljaw")))
    );

    public static final EntityType<Stegonaut> STEGONAUT = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(OriginsDimension.MOD_ID, "stegonaut"),
            EntityType.Builder.<Stegonaut>create(Stegonaut::new, SpawnGroup.MONSTER)
                    .dimensions(2.75f, 2.0f)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,
                            Identifier.of(OriginsDimension.MOD_ID, "stegonaut")))
    );

    public static final EntityType<Ignivorus> IGNIVORUS = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(OriginsDimension.MOD_ID, "ignivorus"),
            EntityType.Builder.<Ignivorus>create(Ignivorus::new, SpawnGroup.MONSTER)
                    .dimensions(8.0f, 6.0f)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,
                            Identifier.of(OriginsDimension.MOD_ID, "ignivorus")))
    );

    public static final EntityType<Volitans> VOLITANS = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(OriginsDimension.MOD_ID, "volitans"),
            EntityType.Builder.<Volitans>create(Volitans::new, SpawnGroup.MONSTER)
                    .dimensions(5.0f, 4.0f)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,
                            Identifier.of(OriginsDimension.MOD_ID, "volitans")))
    );

    public static final EntityType<CindervaneMagmaBlock> CINDERVANE_MAGMA_BLOCK = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(OriginsDimension.MOD_ID, "cindervane_magma_block"),
            EntityType.Builder.<CindervaneMagmaBlock>create(CindervaneMagmaBlock::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,
                            Identifier.of(OriginsDimension.MOD_ID, "cindervane_magma_block")))
    );

    public static void register() {}
}