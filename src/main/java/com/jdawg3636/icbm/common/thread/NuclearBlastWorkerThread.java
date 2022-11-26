package com.jdawg3636.icbm.common.thread;

import com.jdawg3636.icbm.common.reg.ICBMTags;
import net.minecraft.block.Block;
import net.minecraft.tags.ITag;
import net.minecraft.util.math.BlockPos;

public class NuclearBlastWorkerThread extends RaytracedBlastWorkerThread {

    @Override
    public boolean shouldDecorate(BlockPos blockPos) {
        ITag<Block> tag = ICBMTags.Blocks.CAN_BE_REPLACED_AFTER_NUCLEAR_BLAST;
        return blockStateSupplier.apply(blockPos).is(tag) && randomSupplier.get().nextFloat() < 0.25F;
    }

}
