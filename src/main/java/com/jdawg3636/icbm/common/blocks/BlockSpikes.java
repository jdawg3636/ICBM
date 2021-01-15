package com.jdawg3636.icbm.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSpikes extends Block {

    public BlockSpikes() {
        super(Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F).doesNotBlockMovement().notSolid());
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity) entity.attackEntityFrom(DamageSource.CACTUS, 1);
    }

}
