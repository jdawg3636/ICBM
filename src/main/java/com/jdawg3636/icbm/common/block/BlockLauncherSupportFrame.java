package com.jdawg3636.icbm.common.block;

import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMulti;
import com.jdawg3636.icbm.common.block.multiblock.IMissileLaunchApparatus;
import net.minecraft.util.math.vector.Vector3i;

public class BlockLauncherSupportFrame extends AbstractBlockMulti implements IMissileLaunchApparatus {

    private static final Vector3i[] MULTIBLOCK_POSITIONS = {
            new Vector3i(0,1,0),
            new Vector3i(0,2,0)
    };

    @Override
    public Vector3i[] getMultiblockOffsets() {
        return MULTIBLOCK_POSITIONS;
    }

}
