package icbm.classic.prefab.tile;

import icbm.classic.ICBMConstants;
import icbm.classic.api.tile.multiblock.IMultiTileHost;
import icbm.classic.prefab.inventory.IInventoryProvider;
import icbm.classic.content.blocks.multiblock.MultiBlockHelper;
import icbm.classic.ICBMClassic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockICBM extends ContainerBlock {

    public static final DirectionProperty ROTATION_PROP = DirectionProperty.create("rotation");
    public static final TierProperty TIER_PROP = new TierProperty();

    public BlockICBM(String name, Material mat) {

        super(
                Block.Properties.create(mat)
                .hardnessAndResistance(10f)
        );

        setRegistryName(ICBMConstants.DOMAIN, name.toLowerCase());

        setCreativeTab(ICBMClassic.CREATIVE_TAB);

    }

    public BlockICBM(String name) {
        this(name, Material.IRON);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ROTATION_PROP);
    }

    @Override
    public BlockState getStateFromMeta(int meta) {
        return getDefaultState().with(ROTATION_PROP, Direction.byIndex(meta));
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return state.get(ROTATION_PROP).ordinal();
    }

    @Override
    public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer, Hand hand) {
        return getDefaultState().with(ROTATION_PROP, placer.getHorizontalFacing());
    }

    @Override
    public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te, ItemStack stack) {
        if (te instanceof IMultiTileHost)
            MultiBlockHelper.destroyMultiBlockStructure((IMultiTileHost) te, false, true, false);
        super.harvestBlock(world, player, pos, state, te, stack);
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IMultiTileHost)
            MultiBlockHelper.destroyMultiBlockStructure((IMultiTileHost) tile, false, true, false);
        return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, BlockState state) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IInventoryProvider && ((IInventoryProvider) tile).getInventory() != null)
            InventoryHelper.dropInventoryItems(world, pos, ((IInventoryProvider) tile).getInventory());
        if (tile instanceof IMultiTileHost) {
            //At this point the structure should already be dead if broken by a player
            MultiBlockHelper.destroyMultiBlockStructure((IMultiTileHost) tile, false, true, false);
        }
        super.breakBlock(world, pos, state);
    }

}
