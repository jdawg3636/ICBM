package com.jdawg3636.icbm.common.block.oil_refinery;

import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMultiTile;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.fml.RegistryObject;

public class BlockOilRefinery extends AbstractBlockMultiTile {

    public static final Vector3i INLET_POSITION = new Vector3i(-1,1,-1);
    public static final Vector3i OULTET_POSITION = new Vector3i(1,1,-1);
    public static final Vector3i POWER_POSITION = new Vector3i(0,0,-1);

    private static final Vector3i[] MULTIBLOCK_PASSTHROUGH_TILE_POSITIONS = {
            INLET_POSITION,
            OULTET_POSITION,
            POWER_POSITION
    };

    private static final Vector3i[] MULTIBLOCK_POSITIONS = {
            // Back Bottom
            new Vector3i(1,0,-1),
            POWER_POSITION,
            new Vector3i(-1,0,-1),
            // Back Top
            OULTET_POSITION,
            new Vector3i(0,1,-1),
            INLET_POSITION,
            // Front Left/Right
            new Vector3i(1,0,0),
            new Vector3i(-1,0,0),
            // Front Top
            new Vector3i(0,1,0),
    };

    public BlockOilRefinery(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        this(tileEntityType, false);
    }

    public BlockOilRefinery(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType, boolean waterloggable) {
        super(tileEntityType, waterloggable);
    }

    public BlockOilRefinery(Properties properties, RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
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
