package com.jdawg3636.icbm.common.block;

import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMulti;
import com.jdawg3636.icbm.common.entity.EntityPrimedExplosives;
import com.jdawg3636.icbm.common.event.AbstractBlastEvent;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;

public class BlockSMine extends BlockExplosives {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public BlockSMine(RegistryObject<EntityType<EntityPrimedExplosives>> entityForm, AbstractBlastEvent.BlastEventProvider blastEventProvider, RegistryObject<Item> itemForm) {
        this(AbstractBlockMulti.getMultiblockMachineBlockProperties().noCollission().strength(0.5F), entityForm, blastEventProvider, itemForm);
    }

    public BlockSMine(AbstractBlock.Properties properties, RegistryObject<EntityType<EntityPrimedExplosives>> entityForm, AbstractBlastEvent.BlastEventProvider blastEventProvider, RegistryObject<Item> itemForm) {
        super(properties, entityForm, blastEventProvider, itemForm);
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public void entityInside(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        // Condition to prevent chains of S-Mine entities triggering other S-Mines
        if(!(entityIn.getType().equals(entityForm.get()))) {
            worldIn.playSound((PlayerEntity)null, pos, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
            if (entityIn instanceof LivingEntity)
                explode(worldIn, pos, (LivingEntity) entityIn, null, 10);
            else
                explode(worldIn, pos, null, null, 10);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.below()).isFaceSturdy(worldIn, pos, Direction.UP);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, IWorld level, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.DOWN && !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return Block.box(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    @Override
    public boolean isPossibleToRespawnInThis() {
        // This could be fun for bed traps ;)
        return true;
    }

}
