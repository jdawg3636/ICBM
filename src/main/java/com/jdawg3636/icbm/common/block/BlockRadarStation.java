package com.jdawg3636.icbm.common.block;

import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMulti;
import net.minecraft.util.math.vector.Vector3i;

public class BlockRadarStation extends AbstractBlockMulti {

    private static final Vector3i[] MULTIBLOCK_POSITIONS = {
            new Vector3i( 1,1,0),
            new Vector3i( 0,1,0),
            new Vector3i(-1,1,0)
    };

    @Override
    public Vector3i[] getMultiblockPositions() {
        return MULTIBLOCK_POSITIONS;
    }

}
