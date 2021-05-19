package com.jdawg3636.icbm.common.blocks;

import com.jdawg3636.icbm.common.blocks.multiblock.AbstractBlockMulti;
import com.jdawg3636.icbm.common.entity.EntityPrimedExplosives;
import com.jdawg3636.icbm.common.event.BlastEvent;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

public class BlockSMine extends BlockExplosives {

    public BlockSMine(RegistryObject<EntityType<EntityPrimedExplosives>> entityForm, BlastEvent.BlastEventProvider blastEventProvider, RegistryObject<Item> itemForm) {
        this(AbstractBlockMulti.getMultiblockMachineBlockProperties().doesNotBlockMovement().hardnessAndResistance(0.5F), entityForm, blastEventProvider, itemForm);
    }

    public BlockSMine(AbstractBlock.Properties properties, RegistryObject<EntityType<EntityPrimedExplosives>> entityForm, BlastEvent.BlastEventProvider blastEventProvider, RegistryObject<Item> itemForm) {
        super(properties, entityForm, blastEventProvider, itemForm);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.down();
        return hasSolidSideOnTop(worldIn, blockpos) || hasEnoughSolidSide(worldIn, blockpos, Direction.UP);
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        // Condition to prevent chains of S-Mine entities triggering other S-Mines
        if(!(entityIn.getType().equals(entityForm.get()))) {
            worldIn.playSound((PlayerEntity)null, pos, SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
            if (entityIn instanceof LivingEntity)
                explode(worldIn, pos, (LivingEntity) entityIn);
            else
                explode(worldIn, pos, null);
        }
    }

    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    // This one could be fun for bed traps ;)
    @Override
    public boolean canSpawnInBlock() {
        return true;
    }

}
