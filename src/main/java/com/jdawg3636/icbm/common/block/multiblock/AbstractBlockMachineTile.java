package com.jdawg3636.icbm.common.block.multiblock;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.RegistryObject;

public class AbstractBlockMachineTile extends AbstractBlockMachine {

    public final RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType;

    public AbstractBlockMachineTile(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        this(getMultiblockMachineBlockProperties(), tileEntityType);
    }

    public AbstractBlockMachineTile(AbstractBlock.Properties properties, RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        super(properties);
        this.tileEntityType = tileEntityType;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return hasTileEntity(state) ? tileEntityType.get().create() : null;
    }

}
