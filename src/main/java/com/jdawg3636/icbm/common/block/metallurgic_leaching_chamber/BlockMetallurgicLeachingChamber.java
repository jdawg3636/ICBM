package com.jdawg3636.icbm.common.block.metallurgic_leaching_chamber;

import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMultiTile;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.fml.RegistryObject;

public class BlockMetallurgicLeachingChamber extends AbstractBlockMultiTile {

    public static final Vector3i LIQUID_INLET_POSITION = new Vector3i(0,1,0);

    private static final Vector3i[] MULTIBLOCK_PASSTHROUGH_TILE_POSITIONS = {
            LIQUID_INLET_POSITION
    };

    private static final Vector3i[] MULTIBLOCK_POSITIONS = {
            LIQUID_INLET_POSITION
    };

    public BlockMetallurgicLeachingChamber(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        this(tileEntityType, false);
    }

    public BlockMetallurgicLeachingChamber(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType, boolean waterloggable) {
        super(tileEntityType, waterloggable);
    }

    public BlockMetallurgicLeachingChamber(Properties properties, RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
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
