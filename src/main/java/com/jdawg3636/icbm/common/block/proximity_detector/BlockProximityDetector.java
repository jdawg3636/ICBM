package com.jdawg3636.icbm.common.block.proximity_detector;

import com.jdawg3636.icbm.common.block.machine.AbstractBlockMachineTile;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.RegistryObject;

public class BlockProximityDetector extends AbstractBlockMachineTile {

    public BlockProximityDetector(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        super(tileEntityType, false);
    }

    // Non-deprecated call is IForgeBlock::canConnectRedstone, which just redirects to this.
    @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }

    @Override
    public int getSignal(BlockState blockState, IBlockReader level, BlockPos blockPos, Direction p_180656_4_) {
        TileEntity tileEntity = level.getBlockEntity(blockPos);
        if(tileEntity instanceof TileProximityDetector) {
            return ((TileProximityDetector)tileEntity).getSignal(blockState, level, blockPos, p_180656_4_);
        }
        return 0;
    }

}
