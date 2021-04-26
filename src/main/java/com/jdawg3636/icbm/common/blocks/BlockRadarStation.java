package com.jdawg3636.icbm.common.blocks;

import com.jdawg3636.icbm.common.blocks.multiblock.AbstractBlockMultiTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

public class BlockRadarStation extends AbstractBlockMultiTile {

    private static final Vector3i[] MULTIBLOCK_POSITIONS = {
            new Vector3i( 1,1,0),
            new Vector3i( 0,1,0),
            new Vector3i(-1,1,0)
    };

    /**
     * Constructor - Sets Default State for Multiblock Positioning Properties
     */
    public BlockRadarStation() {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public Vector3i[] getMultiblockPositions() {
        return MULTIBLOCK_POSITIONS;
    }

    @Override
    public void onMultiblockActivated(TileEntity tileEntity, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        // NOP
    }

}
