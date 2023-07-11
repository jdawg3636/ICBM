package com.jdawg3636.icbm.common.block.radar_station;

import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMultiTile;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

public class BlockRadarStation extends AbstractBlockMultiTile {

    private static final Vector3i[] MULTIBLOCK_POSITIONS = {
            new Vector3i(0,1,0)
    };

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
    public Vector3i[] getMultiblockOffsets() {
        return MULTIBLOCK_POSITIONS;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return isRootOfMultiblock(state) ? BlockRenderType.ENTITYBLOCK_ANIMATED : BlockRenderType.INVISIBLE;
    }

    @Override
    public void onUseMultiblock(TileEntity tileEntity, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        // TODO GUI/Screen NYI
    }

}
