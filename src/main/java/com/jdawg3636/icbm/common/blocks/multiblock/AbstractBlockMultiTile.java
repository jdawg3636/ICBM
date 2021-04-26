package com.jdawg3636.icbm.common.blocks.multiblock;

import com.jdawg3636.icbm.common.tile.TileLauncherPlatform;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public abstract class AbstractBlockMultiTile extends AbstractBlockMulti {

    public AbstractBlockMultiTile(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(MULTIBLOCK_OFFSET_HORIZONTAL) == 0 && state.get(MULTIBLOCK_OFFSET_HEIGHT) == 0 && state.get(MULTIBLOCK_OFFSET_DEPTH) == 0;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return hasTileEntity(state) ? new TileLauncherPlatform() : null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(getMultiblockCenter(world, pos, state));
            if (tileEntity != null) {
                onMultiblockActivated(tileEntity, state, world, pos, player, hand, trace);
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        return ActionResultType.SUCCESS;
    }

    public abstract void onMultiblockActivated(TileEntity tileEntity, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace);

}
