package com.jdawg3636.icbm.common.block.radar_station;

import com.jdawg3636.icbm.common.block.emp_tower.BlockEMPTower;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.RegistryObject;

import java.util.Optional;

public class BlockRadarStation extends BlockEMPTower {

    public BlockRadarStation(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        super(tileEntityType);
    }

    public BlockRadarStation(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType, boolean waterloggable) {
        super(tileEntityType, waterloggable);
    }

    public BlockRadarStation(AbstractBlock.Properties properties, RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType, boolean waterloggable) {
        super(properties, tileEntityType, waterloggable);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, IBlockReader level, BlockPos pos, Direction direction) {
        // Defer to TileEntity if it exists, otherwise return 0
        return Optional.ofNullable(level.getBlockEntity(pos)).filter(TileRadarStation.class::isInstance).map(TileRadarStation.class::cast).map(TileRadarStation::getRedstoneStrength).orElse(0);
    }

}
