package com.jdawg3636.icbm.common.thread;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class NuclearBlastWorkerThread extends RaytracedBlastWorkerThread {

    @Override
    public boolean shouldDecorate(BlockPos blockPos) {
        ITag<Block> tag = BlockTags.getAllTags().getTag(new ResourceLocation(ICBMReference.MODID, "can_become_radioactive_material"));
        return tag != null && tag.contains(blockStateSupplier.apply(blockPos).getBlock()) && randomSupplier.get().nextFloat() < 0.05F;
    }

}
