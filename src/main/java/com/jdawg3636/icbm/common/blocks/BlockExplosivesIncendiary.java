package com.jdawg3636.icbm.common.blocks;

import com.jdawg3636.icbm.common.entity.EntityExplosivesIncendiary;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;

// Pretty much this entire class is directly copy/pasted from net.minecraft.block.TNTBlock
// Had to be duplicated rather than extended because the explode() method is static
public class BlockExplosivesIncendiary extends Block {

    /**
     * Parameterless Constructor
     * */
    public BlockExplosivesIncendiary() {
        this(Block.Properties.create(Material.TNT).hardnessAndResistance(2).sound(SoundType.PLANT));
    }

    /**
     * Normal Ignition Routine
     * */
    private static void explode(World world, BlockPos pos, @Nullable LivingEntity igniter) {
        if (!world.isRemote) {
            EntityExplosivesIncendiary explosives_entity = new EntityExplosivesIncendiary(world, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, igniter);
            world.addEntity(explosives_entity);
            world.playSound((PlayerEntity)null, explosives_entity.getPosX(), explosives_entity.getPosY(), explosives_entity.getPosZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

    /**
     * Overload copied from Vanilla
     * Can't actually find any uses of this, but no real reason to leave it out.
     * */
    public static void explode(World world, BlockPos pos) {
        explode(world, pos, (LivingEntity)null);
    }

    /**
     * Block destroyed by another explosion
     * Ignites with a shorter fuse and no sound effect
     * */
    public void onExplosionDestroy(World world, BlockPos pos, Explosion explosionIn) {
        if (!world.isRemote) {
            EntityExplosivesIncendiary explosives_entity = new EntityExplosivesIncendiary(world, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, explosionIn.getExplosivePlacedBy());
            explosives_entity.setFuse((short)(world.rand.nextInt(explosives_entity.getFuse() / 4) + explosives_entity.getFuse() / 8));
            world.addEntity(explosives_entity);
        }
    }

    // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- //
    // -- Begin Copy/Paste from net.minecraft.block.TNTBlock -- //
    // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- //

    public static final BooleanProperty UNSTABLE = BlockStateProperties.UNSTABLE;

    public BlockExplosivesIncendiary(AbstractBlock.Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(UNSTABLE, Boolean.valueOf(false)));
    }

    public void catchFire(BlockState state, World world, BlockPos pos, @Nullable net.minecraft.util.Direction face, @Nullable LivingEntity igniter) {
        explode(world, pos, igniter);
    }

    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.isIn(state.getBlock())) {
            if (worldIn.isBlockPowered(pos)) {
                catchFire(state, worldIn, pos, null, null);
                worldIn.removeBlock(pos, false);
            }
        }
    }

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (worldIn.isBlockPowered(pos)) {
            catchFire(state, worldIn, pos, null, null);
            worldIn.removeBlock(pos, false);
        }
    }

    /**
     * Called before the Block is set to air in the world. Called regardless of if the player's tool can actually collect
     * this block
     */
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!worldIn.isRemote() && !player.isCreative() && state.get(UNSTABLE))
            catchFire(state, worldIn, pos, null, null);
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack itemstack = player.getHeldItem(handIn);
        Item item = itemstack.getItem();
        if (item != Items.FLINT_AND_STEEL && item != Items.FIRE_CHARGE) {
            return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
        } else {
            catchFire(state, worldIn, pos, hit.getFace(), player);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
            if (!player.isCreative()) {
                if (item == Items.FLINT_AND_STEEL) {
                    itemstack.damageItem(1, player, (player1) -> {
                        player1.sendBreakAnimation(handIn);
                    });
                } else {
                    itemstack.shrink(1);
                }
            }
            return ActionResultType.func_233537_a_(worldIn.isRemote);
        }
    }

    public void onProjectileCollision(World worldIn, BlockState state, BlockRayTraceResult hit, ProjectileEntity projectile) {
        if (!worldIn.isRemote) {
            Entity entity = projectile.func_234616_v_();
            if (projectile.isBurning()) {
                BlockPos blockpos = hit.getPos();
                catchFire(state, worldIn, blockpos, null, entity instanceof LivingEntity ? (LivingEntity)entity : null);
                worldIn.removeBlock(blockpos, false);
            }
        }
    }

    /**
     * Return whether this block can drop from an explosion.
     */
    public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(UNSTABLE);
    }

}
