package com.jdawg3636.icbm.common.block;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.entity.EntityPrimedExplosives;
import com.jdawg3636.icbm.common.event.AbstractBlastEvent;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;

// Pretty much this entire class is directly copy/pasted from net.minecraft.block.TNTBlock
// Had to be duplicated rather than extended because the explode() method is static
public class BlockExplosives extends Block {

    public RegistryObject<EntityType<EntityPrimedExplosives>> entityForm;
    public AbstractBlastEvent.BlastEventProvider blastEventProvider;
    public RegistryObject<Item> itemForm;

    /**
     * Parameterless Constructor
     * */
    public BlockExplosives(RegistryObject<EntityType<EntityPrimedExplosives>> entityForm, AbstractBlastEvent.BlastEventProvider blastEventProvider, RegistryObject<Item> itemForm) {
        this(Block.Properties.of(Material.EXPLOSIVE).instabreak().sound(SoundType.GRASS), entityForm, blastEventProvider, itemForm);
    }

    /**
     * Verbose Constructor (Used by S-Mine to implement a custom material)
     * */
    public BlockExplosives(AbstractBlock.Properties properties, RegistryObject<EntityType<EntityPrimedExplosives>> entityForm, AbstractBlastEvent.BlastEventProvider blastEventProvider, RegistryObject<Item> itemForm) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(UNSTABLE, Boolean.FALSE));
        this.entityForm = entityForm;
        this.blastEventProvider = blastEventProvider;
        this.itemForm = itemForm;
    }

    /**
     * Normal Ignition Routine
     * */
    public void explode(World world, BlockPos pos, @Nullable LivingEntity igniter) {
        explode(world, pos, igniter, null);
    }

    public void explode(World world, BlockPos pos, @Nullable LivingEntity igniter, @Nullable Direction blastDirection) {
        explode(world, pos, igniter, blastDirection, 80);
    }

    public void explode(World world, BlockPos pos, @Nullable LivingEntity igniter, @Nullable Direction blastDirection, int fuse) {
        if (!world.isClientSide) {
            world.setBlock(pos, world.getFluidState(pos).getType() == Fluids.WATER ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 11);
            EntityPrimedExplosives explosives_entity = new EntityPrimedExplosives(entityForm.get(), world, blastEventProvider, itemForm, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, igniter, blastDirection, fuse);
            world.addFreshEntity(explosives_entity);
            world.playSound((PlayerEntity)null, explosives_entity.getX(), explosives_entity.getY(), explosives_entity.getZ(), SoundEvents.TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

    /**
     * Block destroyed by another explosion
     * Ignites with a shorter fuse and no sound effect
     * */
    public void wasExploded(World world, BlockPos pos, Explosion explosionIn) {
        if (!world.isClientSide) {
            EntityPrimedExplosives explosives_entity = new EntityPrimedExplosives(entityForm.get(), world, blastEventProvider, itemForm, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, explosionIn.getSourceMob());
            explosives_entity.setFuse((short)(world.random.nextInt(explosives_entity.getLife() / 4) + explosives_entity.getLife() / 8));
            world.addFreshEntity(explosives_entity);
        }
    }

    /**
     * Easter Egg for Redcoats
     */
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        if(ICBMReference.COMMON_CONFIG.getEnableEasterEggForRedcoats()) {
            if (state != null && context.getPlayer() != null) {
                String playerName = context.getPlayer().getName().getString();
                if (playerName.equals("SlushierZeus69") || playerName.equals("dig_dug__")) {
                    state = state.setValue(UNSTABLE, Boolean.TRUE);
                }
            }
        }
        return state;
    }

    @Nullable
    public static Direction getDirectionBetweenBlockPos(BlockPos source, BlockPos dest) {
        Vector3i delta = new Vector3i(dest.getX() - source.getX(), dest.getY() - source.getY(), dest.getZ() - source.getZ());
        Direction direction = null;
        for(Direction candidateDirection : Direction.values()) {
            if(candidateDirection.getNormal().equals(delta)) {
                direction = candidateDirection;
                break;
            }
        }
        return direction;
    }

    @Nullable
    public static Direction getNeighborSignalDirection(World level, BlockPos blockPos) {
        if (level.getSignal(blockPos.below(), Direction.DOWN) > 0) {
            return Direction.DOWN;
        } else if (level.getSignal(blockPos.above(), Direction.UP) > 0) {
            return Direction.UP;
        } else if (level.getSignal(blockPos.north(), Direction.NORTH) > 0) {
            return Direction.NORTH;
        } else if (level.getSignal(blockPos.south(), Direction.SOUTH) > 0) {
            return Direction.SOUTH;
        } else if (level.getSignal(blockPos.west(), Direction.WEST) > 0) {
            return Direction.WEST;
        } else if (level.getSignal(blockPos.east(), Direction.EAST) > 0) {
            return Direction.EAST;
        } else {
            return null;
        }
    }

    // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- //
    // -- Begin Copy/Paste from net.minecraft.block.TNTBlock -- //
    // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- //

    public static final BooleanProperty UNSTABLE = BlockStateProperties.UNSTABLE;

    public void catchFire(BlockState state, World world, BlockPos pos, @Nullable net.minecraft.util.Direction face, @Nullable LivingEntity igniter) {
        if (face == null) {
            explode(world, pos, igniter);
        } else {
            explode(world, pos, igniter, face.getOpposite());
        }
    }

    public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock())) {
            Direction neighborSignalDirection = getNeighborSignalDirection(worldIn, pos);
            if (neighborSignalDirection != null) {
                explode(worldIn, pos, null, neighborSignalDirection.getOpposite());
                worldIn.removeBlock(pos, false);
            }
        }
    }

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (worldIn.hasNeighborSignal(pos)) {
            explode(worldIn, pos, null, getDirectionBetweenBlockPos(fromPos, pos));
            worldIn.removeBlock(pos, false);
        }
    }

    /**
     * Called before the Block is set to air in the world. Called regardless of if the player's tool can actually collect
     * this block
     */
    public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!worldIn.isClientSide() && !player.isCreative() && state.getValue(UNSTABLE))
            catchFire(state, worldIn, pos, null, null);
        super.playerWillDestroy(worldIn, pos, state, player);
    }

    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack itemstack = player.getItemInHand(handIn);
        Item item = itemstack.getItem();
        if (item != Items.FLINT_AND_STEEL && item != Items.FIRE_CHARGE) {
            return super.use(state, worldIn, pos, player, handIn, hit);
        } else {
            catchFire(state, worldIn, pos, hit.getDirection(), player);
            if (!player.isCreative()) {
                if (item == Items.FLINT_AND_STEEL) {
                    itemstack.hurtAndBreak(1, player, (player1) -> {
                        player1.broadcastBreakEvent(handIn);
                    });
                } else {
                    itemstack.shrink(1);
                }
            }
            return ActionResultType.sidedSuccess(worldIn.isClientSide);
        }
    }

    public void onProjectileHit(World worldIn, BlockState state, BlockRayTraceResult hit, ProjectileEntity projectile) {
        if (!worldIn.isClientSide) {
            Entity entity = projectile.getOwner();
            if (projectile.isOnFire()) {
                BlockPos blockpos = hit.getBlockPos();
                catchFire(state, worldIn, blockpos, null, entity instanceof LivingEntity ? (LivingEntity)entity : null);
                worldIn.removeBlock(blockpos, false);
            }
        }
    }

    /**
     * Return whether this block can drop from an explosion.
     */
    public boolean dropFromExplosion(Explosion explosionIn) {
        return false;
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(UNSTABLE);
    }

}
