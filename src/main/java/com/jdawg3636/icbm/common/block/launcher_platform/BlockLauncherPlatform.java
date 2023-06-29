package com.jdawg3636.icbm.common.block.launcher_platform;

import com.jdawg3636.icbm.common.block.machine.TileMachine;
import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMultiTile;
import com.jdawg3636.icbm.common.block.multiblock.IMissileLaunchApparatus;
import com.jdawg3636.icbm.common.item.ItemMissile;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;

public class BlockLauncherPlatform extends AbstractBlockMultiTile implements IMissileLaunchApparatus {

    private static final Vector3i[] MULTIBLOCK_POSITIONS = {
            new Vector3i(1,0,0),
            new Vector3i(1,1,0),
            new Vector3i(1,2,0),
            new Vector3i(-1,0,0),
            new Vector3i(-1,1,0),
            new Vector3i(-1,2,0)
    };

    public BlockLauncherPlatform(RegistryObject<TileEntityType<? extends TileEntity>> tileEntity) {
        this(getMultiblockMachineBlockProperties(), tileEntity);
    }

    public BlockLauncherPlatform(AbstractBlock.Properties properties, RegistryObject<TileEntityType<? extends TileEntity>> tileEntity) {
        super(properties, tileEntity);
    }

    @Override
    public Vector3i[] getMultiblockOffsets() {
        return MULTIBLOCK_POSITIONS;
    }

    @Override
    public void onUseMultiblock(TileEntity tileEntity, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if(!world.isClientSide() && tileEntity instanceof TileLauncherPlatform) {
            final ItemStackHandler itemHandler = (ItemStackHandler)((TileLauncherPlatform)tileEntity).itemHandlerLazyOptional.orElse(null);
            assert itemHandler != null;
            if (itemHandler.getStackInSlot(0).isEmpty() && player.getItemInHand(hand).getItem() instanceof ItemMissile) {
                ItemStack itemStack = player.getItemInHand(hand);
                if(!player.isCreative()) player.inventory.removeItem(itemStack);
                itemHandler.setStackInSlot(0, itemStack);
            }
            else {
                NetworkHooks.openGui((ServerPlayerEntity) player, (TileMachine)tileEntity, tileEntity.getBlockPos());
            }
        }
    }

    @Override
    public void destroyMultiblock(World worldIn, BlockPos pos, BlockState sourceState) {
        TileEntity tileEntity = worldIn.getBlockEntity(getMultiblockCenter(worldIn, pos, sourceState));
        if(tileEntity instanceof TileLauncherPlatform) ((TileLauncherPlatform)tileEntity).onBlockDestroyed();
        super.destroyMultiblock(worldIn, pos, sourceState);
    }

}
