package com.jdawg3636.icbm.common.blocks;

import com.jdawg3636.icbm.common.blocks.multiblock.AbstractBlockMulti;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.vector.Vector3i;

public class BlockLauncherSupportFrame extends AbstractBlockMulti {

    private static final Vector3i[] MULTIBLOCK_POSITIONS = {
            new Vector3i(0,1,0),
            new Vector3i(0,2,0)
    };

    /**
     * Constructor - Sets Default State for Multiblock Positioning Properties
     */
    public BlockLauncherSupportFrame() {
        super(Block.Properties.create(Material.IRON));
    }

    @Override
    public Vector3i[] getMultiblockPositions() {
        return MULTIBLOCK_POSITIONS;
    }

}
