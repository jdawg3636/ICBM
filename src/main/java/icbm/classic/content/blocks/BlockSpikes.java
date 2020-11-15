package icbm.classic.content.blocks;

import com.google.common.collect.Lists;
import icbm.classic.ICBMClassic;
import icbm.classic.ICBMConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
//TODO//import net.minecraft.block.properties.PropertyEnum;
//TODO//import net.minecraft.block.state.BlockFaceShape;
//TODO//import net.minecraft.block.state.BlockStateContainer;
//TODO//import net.minecraft.block.state.IBlockState;
//TODO//import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
//TODO//import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
//TODO//import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
//TODO//import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
//TODO//import net.minecraftforge.fml.relauncher.Side;
//TODO//import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSpikes extends Block {

    public BlockSpikes() {
        super(Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F).doesNotBlockMovement());
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

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        // If the entity is a living entity
        if (entity instanceof LivingEntity) {

            entity.attackEntityFrom(DamageSource.CACTUS, 1);

        }
    }

}
