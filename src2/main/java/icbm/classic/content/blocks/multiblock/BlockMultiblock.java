package icbm.classic.content.blocks.multiblock;

import icbm.classic.ICBMConstants;
import icbm.classic.api.tile.multiblock.IMultiTile;
import net.minecraft.block.*;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

/**
 * Created by Dark on 7/4/2015.
 */
public class BlockMultiblock extends ContainerBlock {

    public BlockMultiblock() {
        super(Material.ROCK);
        this.setRegistryName(ICBMConstants.DOMAIN, "multiblock");
        this.setTranslationKey(ICBMConstants.PREFIX + "multiblock");
        this.setHardness(2f);
        ticksRandomly = true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (!worldIn.isRemote) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof TileMulti) {
                if (!((TileMulti) tile).hasHost())
                    worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
                else if (((TileMulti) tile).isHostLoaded() && ((TileMulti) tile).getHost() == null)
                    worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
        }
    }

    @Override
    public boolean isNormalCube(IBlockReader reader, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, World world, BlockPos pos, PlayerEntity player) {

        final IMultiTile multiblock = getTile(world, pos);

        if (multiblock != null && multiblock.getHost() instanceof TileEntity) {

            //Get tile
            TileEntity tileEntity = ((TileEntity) multiblock.getHost());

            //Get state
            BlockState blockState = tileEntity.getWorld().getBlockState(tileEntity.getPos());
            Block block = blockState.getBlock();

            //Get actual block state
            blockState = block.getActualState(blockState, world, pos);

            //Get stack from state
            ItemStack stack = block.getPickBlock(blockState, target, world, tileEntity.getPos(), player);
            return stack;

        }

        return ItemStack.EMPTY;

    }

    @Override
    public int quantityDropped(Random p_149745_1_) {
        return 0;
    }

    @Override
    public Item getItemDropped(BlockState state, Random rand, int fortune) {
        return null;
    }


    @Override
    public void getDrops(NonNullList<ItemStack> drops, ServerWorld world, BlockPos pos, BlockState state, int fortune) {
        //Nothing
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, BlockState state) {

        IMultiTile tile = getTile(world, pos);
        if (tile != null && tile.getHost() != null)
            tile.getHost().onMultiTileAdded(tile);

        super.onBlockAdded(world, pos, state);

    }

    @Override
    public void breakBlock(World world, BlockPos pos, BlockState state) {
        IMultiTile tile = getTile(world, pos);
        if (tile != null && tile.getHost() != null)
            tile.getHost().onMultiTileBroken(tile, null, true);
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) {
        IMultiTile tile = getTile(world, pos);
        if (tile != null && tile.getHost() != null)
            tile.getHost().onMultiTileBroken(tile, player, willHarvest);
        return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion ex) {

        IMultiTile tile = getTile(world, pos);

        if (tile != null && tile.getHost() != null)
            tile.getHost().onMultiTileBroken(tile, ex, true);

    }

    @Override
    public boolean onBlockActivated(World world, PlayerEntity player, Hand handIn, BlockRayTraceResult resultIn) {

        TileMulti tile = getTile(world, resultIn.getPos());

        if (tile != null) {

            //Kill check
            if (!world.isRemote) {

                if (!tile.hasHost() || tile.isHostLoaded() && tile.getHost() == null) {
                    world.setBlockState(resultIn.getPos(), Blocks.AIR.getDefaultState());
                    return true;
                }

            }

            //Normal click
            // TODO IMultiTileHost is flagged to be converted into a capability, this is the perfect opportunity (already needs major rework)
            return tile.getHost() != null && tile.getHost().onMultiTileActivated(tile, player, hand, side, hitX, hitY, hitZ);

        }

        return true;

    }

    @Override
    public void onBlockClicked(World world, BlockPos pos, PlayerEntity player) {

        TileMulti tile = getTile(world, pos);

        if (tile != null) {

            //Kill check
            if (!world.isRemote) {

                if (!tile.hasHost() || tile.isHostLoaded() && tile.getHost() == null) {
                    world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    return;
                }

            }

            //Normal click
            if (tile.getHost() != null)
                tile.getHost().onMultiTileClicked(tile, player);

        }

    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new TileMulti();
    }

    protected TileMulti getTile(World world, BlockPos pos) {

        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileMulti)
            return (TileMulti) tile;

        return null;

    }

}
