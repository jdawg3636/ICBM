package icbm.content.blocks;

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

    /* TODO

    @Override
    public boolean isBlockNormalCube(IBlockState blockState)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    */

}
