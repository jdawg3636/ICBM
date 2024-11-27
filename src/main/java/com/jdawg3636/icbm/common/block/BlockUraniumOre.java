package com.jdawg3636.icbm.common.block;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class BlockUraniumOre extends OreBlock {

    public BlockUraniumOre(Properties properties) {
        super(properties);
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState blockState, World level, BlockPos blockPos, Random random) {
        for(int i = 0; i < 3; ++i) {
            ICBMReference.spawnRadiationParticle(level, blockPos, random);
        }
    }

}
