package com.jdawg3636.icbm.common.block.centrifugal_deposition_unit;

import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMultiTile;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.fml.RegistryObject;

public class BlockCentrifugalDepositionUnit extends AbstractBlockMultiTile {

    public static final Vector3i ITEM_OUTLET_POSITION = new Vector3i(0,0,-1);
    public static final Vector3i LIQUID_INLET_POSITION = new Vector3i(-1,0,0);

    private static final Vector3i[] MULTIBLOCK_PASSTHROUGH_TILE_POSITIONS = {
            ITEM_OUTLET_POSITION,
            LIQUID_INLET_POSITION
    };

    private static final Vector3i[] MULTIBLOCK_POSITIONS = {
            new Vector3i(-1, 0, -1),
            new Vector3i(-1, 0, 0),
            new Vector3i(-1, 0, 1),
            new Vector3i(-1, 1, -1),
            new Vector3i(-1, 1, 0),
            new Vector3i(-1, 1, 1),
            new Vector3i(-1, 2, -1),
            new Vector3i(-1, 2, 0),
            new Vector3i(-1, 2, 1),
            new Vector3i(0, 0, -1),
            /* omitting root pos */
            new Vector3i(0, 0, 1),
            new Vector3i(0, 1, -1),
            new Vector3i(0, 1, 0),
            new Vector3i(0, 1, 1),
            new Vector3i(0, 2, -1),
            new Vector3i(0, 2, 0),
            new Vector3i(0, 2, 1),
            new Vector3i(1, 0, -1),
            new Vector3i(1, 0, 0),
            new Vector3i(1, 0, 1),
            new Vector3i(1, 1, -1),
            new Vector3i(1, 1, 0),
            new Vector3i(1, 1, 1),
            new Vector3i(1, 2, -1),
            new Vector3i(1, 2, 0),
            new Vector3i(1, 2, 1)
    };

    public BlockCentrifugalDepositionUnit(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        this(tileEntityType, false);
    }

    public BlockCentrifugalDepositionUnit(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType, boolean waterloggable) {
        super(tileEntityType, waterloggable);
    }

    public BlockCentrifugalDepositionUnit(Properties properties, RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        super(properties, tileEntityType, false);
    }

    @Override
    public Vector3i[] getMultiblockOffsets() {
        return MULTIBLOCK_POSITIONS;
    }

    @Override
    public Vector3i[] getMutiblockOffsetsWhichHavePassthroughTileEntity() {
        return MULTIBLOCK_PASSTHROUGH_TILE_POSITIONS;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

}
