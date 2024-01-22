package com.jdawg3636.icbm.common.block.radar_station;

import com.jdawg3636.icbm.common.block.emp_tower.BlockEMPTower;
import net.minecraft.block.AbstractBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

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

}
