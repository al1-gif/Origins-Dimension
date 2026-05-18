package meh.shuuphe.originsdimension.registry;

import meh.shuuphe.originsdimension.OriginsDimension;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class ModCreativeTab {

    public static final RegistryKey<ItemGroup> ORIGINS_DIMENSION_TAB = RegistryKey.of(
            RegistryKeys.ITEM_GROUP,
            Identifier.of(OriginsDimension.MOD_ID, "origins_dimension")
    );

    public static void registerCreativeTab() {
        Registry.register(Registries.ITEM_GROUP, ORIGINS_DIMENSION_TAB,
                FabricItemGroup.builder()
                        .icon(() -> new ItemStack(ModItems.PRIMEVELITE_INGOT))
                        .displayName(Text.translatable("itemGroup.originsdimension.origins_dimension"))
                        .entries((context, entries) -> {
                            Set<Item> seen = new HashSet<>();

                            for (Field field : ModItems.class.getFields()) {
                                if (field.getType() != Item.class) continue;
                                try {
                                    Item item = (Item) field.get(null);
                                    if (item != null && seen.add(item))
                                        entries.add(new ItemStack(item));
                                } catch (IllegalAccessException ignored) {}
                            }

                            for (Field field : ModBlocks.class.getFields()) {
                                if (field.getType() != Block.class) continue;
                                try {
                                    Block block = (Block) field.get(null);
                                    if (block == null) continue;
                                    Item item = Item.fromBlock(block);
                                    if (item != Items.AIR && seen.add(item))
                                        entries.add(new ItemStack(item));
                                } catch (IllegalAccessException ignored) {}
                            }
                        })
                        .build()
        );
    }
}