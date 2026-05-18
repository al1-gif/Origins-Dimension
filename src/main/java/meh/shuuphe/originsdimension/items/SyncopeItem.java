package meh.shuuphe.originsdimension.items;

import meh.shuuphe.originsdimension.registry.ModBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SyncopeItem extends Item {
    public SyncopeItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos().up();
        PlayerEntity player = context.getPlayer();

        if (world.getBlockState(pos).isAir()) {
            world.playSound(player, pos, net.minecraft.sound.SoundEvents.ITEM_FLINTANDSTEEL_USE,
                    net.minecraft.sound.SoundCategory.BLOCKS, 1.0f, world.getRandom().nextFloat() * 0.4f + 0.8f);
            world.setBlockState(pos, ModBlocks.BLUE_FIRE.getDefaultState());
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}