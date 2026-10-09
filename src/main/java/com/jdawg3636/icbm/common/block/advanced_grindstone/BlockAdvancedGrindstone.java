package com.jdawg3636.icbm.common.block.advanced_grindstone;

import com.jdawg3636.icbm.common.block.machine.AbstractBlockMachineTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

public class BlockAdvancedGrindstone extends AbstractBlockMachineTile {

    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 13, 16);

    public BlockAdvancedGrindstone(RegistryObject<TileEntityType<? extends TileEntity>> type) {
        super(type, false);
    }

    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if (trace.getDirection() == Direction.UP) {
            TileEntity tile = level.getBlockEntity(pos);
            if (!level.isClientSide && tile instanceof TileAdvancedGrindstone) {
                ((TileAdvancedGrindstone) tile).grind();
            }
            return ActionResultType.sidedSuccess(level.isClientSide);
        }
        return super.use(state, level, pos, player, hand, trace);
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

}
