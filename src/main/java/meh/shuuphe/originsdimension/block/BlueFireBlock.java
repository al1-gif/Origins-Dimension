package meh.shuuphe.originsdimension.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public class BlueFireBlock extends AbstractFireBlock {
    public static final MapCodec<BlueFireBlock> CODEC = createCodec(BlueFireBlock::new);

    public BlueFireBlock(Settings settings) {
        super(settings, 2.0f);
    }

    @Override
    protected MapCodec<? extends AbstractFireBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world,
                                                   ScheduledTickView tickView, BlockPos pos,
                                                   Direction direction, BlockPos neighborPos,
                                                   BlockState neighborState, Random random) {
        return this.canPlaceAt(state, world, pos) ? this.getDefaultState() : Blocks.AIR.getDefaultState();
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState below = world.getBlockState(pos.down());
        return below.isSideSolidFullSquare(world, pos.down(), Direction.UP)
                || below.isBurnable();
    }

    @Override
    protected boolean isFlammable(BlockState state) {
        return false;
    }
}