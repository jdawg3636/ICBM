package com.jdawg3636.icbm.common.block;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.block.BlockState;
import net.minecraft.block.GrassBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class BlockRadioactiveMaterial extends GrassBlock {

    public BlockRadioactiveMaterial(Properties properties) {
        super(properties);
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState blockState, World level, BlockPos blockPos, Random random) {
        ICBMReference.spawnRadiationParticle(level, blockPos, random);
    }

}
