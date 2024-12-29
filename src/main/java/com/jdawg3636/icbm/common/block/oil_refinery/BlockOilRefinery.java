package com.jdawg3636.icbm.common.block.oil_refinery;

import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMultiTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.fml.RegistryObject;

public class BlockOilRefinery extends AbstractBlockMultiTile {

    private static final Vector3i INLET_POSITION = new Vector3i(-1,1,-1);
    private static final Vector3i OULTET_POSITION = new Vector3i(1,1,-1);
    private static final Vector3i POWER_POSITION = new Vector3i(0,0,-1);

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

//    @Override
//    public boolean hasTileEntity(BlockState state) {
//        return doesStateMatchPosition(state, INLET_POSITION) ||
//               doesStateMatchPosition(state, OULTET_POSITION) ||
//               doesStateMatchPosition(state, POWER_POSITION) ||
//               this.isRootOfMultiblock(state);
//    }
//
//    @Override
//    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
//        return hasTileEntity(state) ? tileEntityType.get().create() : null;
//    }

    @Override
    public Vector3i[] getMultiblockOffsets() {
        return MULTIBLOCK_POSITIONS;
    }
}
