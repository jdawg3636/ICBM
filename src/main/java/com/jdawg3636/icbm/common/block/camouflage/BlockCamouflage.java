package com.jdawg3636.icbm.common.block.camouflage;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.reg.TileReg;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Optional;

public class BlockCamouflage extends Block {

    public static final BooleanProperty COLLIDABLE = BooleanProperty.create("collidable");

    public BlockCamouflage() {
        this(Properties.of(Material.LEAVES).strength(0.2F).noOcclusion().sound(SoundType.GRASS).isValidSpawn((a,b,c, d) -> false).isSuffocating((a,b,c) -> false).isViewBlocking((a,b,c) -> false));
    }

    public BlockCamouflage(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(COLLIDABLE, true));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(COLLIDABLE);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, IBlockReader level, BlockPos blockPos, ISelectionContext selectionContext) {
        return blockState.getValue(COLLIDABLE) ? VoxelShapes.block() : VoxelShapes.empty();
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState blockState, IBlockReader level, BlockPos blockPos) {
        return super.getCollisionShape(blockState, level, blockPos, ISelectionContext.empty());
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState) {
        return true;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return hasTileEntity(state) ? TileReg.CAMOUFLAGE.get().create() : null;
    }

    @Override
    public ActionResultType use(BlockState blockState, World level, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult rayTraceResult) {
        if(!level.isClientSide()) {
            // Sneak-right-click = toggle collision
            if (playerEntity.isSecondaryUseActive()) {
                level.setBlockAndUpdate(blockPos, blockState.setValue(COLLIDABLE, !blockState.getValue(COLLIDABLE)));
            } else {
                BlockState blockStateForHeldItem = getCamoAppearanceForItemStack(level, playerEntity, hand, playerEntity.getItemInHand(hand), rayTraceResult);
                // Right-click with block = set appearance
                if (blockStateForHeldItem != null) {
                    // Special case of matching BlockStates - return fail to defer behavior
                    if (getTileEntity(level,blockPos).map(TileCamouflage::getAppearanceNoNull).map(appearance -> appearance.equals(blockStateForHeldItem)).orElse(false)) {
                        return ActionResultType.FAIL;
                    }
                    getTileEntity(level, blockPos).ifPresent(te -> te.setAppearance(blockStateForHeldItem));
                }
                // Right-click with empty hand = toggle side visibility
                else {
                    getTileEntity(level, blockPos).ifPresent(te -> te.toggleSideTransparency(rayTraceResult.getDirection()));
                }
            }
        }
        return ActionResultType.sidedSuccess(true);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.DESTROY;
    }

    public static Optional<TileCamouflage> getTileEntity(IBlockReader level, BlockPos blockPos) {
        TileEntity tileEntity = level.getBlockEntity(blockPos);
        return (tileEntity instanceof TileCamouflage) ? Optional.of((TileCamouflage) tileEntity) : Optional.empty();
    }

    public static BlockState getCamoAppearanceForItemStack(World level, PlayerEntity playerEntity, Hand hand, ItemStack itemStack, BlockRayTraceResult rayTraceResult) {
        BlockItemUseContext context = new BlockItemUseContext(level, playerEntity, hand, itemStack, rayTraceResult);
        return getCamoAppearanceForItemStack(itemStack, context);
    }

    public static BlockState getCamoAppearanceForItemStack(ItemStack input, @Nullable BlockItemUseContext context) {
        Item item = input.getItem();
        if(!(item instanceof BlockItem)) return null;
        Block block = ((BlockItem) item).getBlock();
        if(context == null) {
            return block.defaultBlockState();
        }
        else {
            return block.getStateForPlacement(context);
        }
    }

    @Override
    public int getLightValue(BlockState blockState, IBlockReader level, BlockPos blockPos) {
        return getTileEntity(level, blockPos)
                .map(TileCamouflage::getAppearanceNoSelf)
                .map(appearance -> appearance.getLightValue(level, blockPos))
                .orElseGet(() -> super.getLightValue(blockState, level, blockPos));
    }

    @Override
    public int getLightBlock(BlockState blockState, IBlockReader level, BlockPos blockPos) {
        return getTileEntity(level, blockPos)
                .map(TileCamouflage::getAppearanceNoSelf)
                .map(appearance -> appearance.getLightBlock(level, blockPos))
                .orElseGet(() -> super.getLightBlock(blockState, level, blockPos));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getShadeBrightness(BlockState blockState, IBlockReader level, BlockPos blockPos) {
        return getTileEntity(level, blockPos)
                .map(TileCamouflage::getAppearanceNoSelf)
                .map(appearance -> appearance.getShadeBrightness(level, blockPos))
                .orElseGet(() -> super.getShadeBrightness(blockState, level, blockPos));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, IBlockReader level, BlockPos blockPos) {
        return getTileEntity(level, blockPos)
                .map(TileCamouflage::getAppearanceNoSelf)
                .map(appearance -> appearance.propagatesSkylightDown(level, blockPos))
                .orElseGet(() -> super.propagatesSkylightDown(blockState, level, blockPos));
    }

    /**
     * Replicating the hardcoded behavior of vanilla vines as defined by {@link net.minecraft.block.FireBlock#bootStrap}
     */
    @Override
    public int getFireSpreadSpeed(BlockState state, IBlockReader level, BlockPos pos, Direction face) {
        return ICBMReference.COMMON_CONFIG.getCamouflageFlammable() ? 15 : 0;
    }

    /**
     * Replicating the hardcoded behavior of vanilla vines as defined by {@link net.minecraft.block.FireBlock#bootStrap}
     */
    @Override
    public int getFlammability(BlockState state, IBlockReader level, BlockPos pos, Direction face) {
        return ICBMReference.COMMON_CONFIG.getCamouflageFlammable() ? 100 : 0;
    }

}
