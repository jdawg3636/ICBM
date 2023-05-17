package com.jdawg3636.icbm.common.block.machine;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
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

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState originalState, World level, BlockPos blockPos, BlockState newState, boolean flag) {
        if(originalState.getBlock() != newState.getBlock()) {
            TileEntity tileEntity = level.getBlockEntity(blockPos);
            if (tileEntity instanceof TileMachine) ((TileMachine) tileEntity).onBlockDestroyed();
        }
        super.onRemove(originalState, level, blockPos, newState, flag);
    }

}
