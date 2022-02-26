package com.jdawg3636.icbm.common.block.cruise_launcher;

import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMachineTile;
import com.jdawg3636.icbm.common.block.multiblock.IMissileLaunchApparatus;
import com.jdawg3636.icbm.common.item.ItemMissile;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockCruiseLauncher extends AbstractBlockMachineTile implements IMissileLaunchApparatus {

    public BlockCruiseLauncher(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        super(tileEntityType);
    }

    public BlockCruiseLauncher(AbstractBlock.Properties properties, RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        super(properties, tileEntityType);
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        TileEntity tileEntity = world.getBlockEntity(pos);
        if(!world.isClientSide() && tileEntity instanceof TileCruiseLauncher) {
            if (((TileCruiseLauncher)tileEntity).itemHandler.getStackInSlot(0).isEmpty() && player.getItemInHand(hand).getItem() instanceof ItemMissile) {
                ItemStack itemStack = player.getItemInHand(hand);
                if(!player.isCreative()) player.inventory.removeItem(itemStack);
                ((TileCruiseLauncher)tileEntity).itemHandler.setStackInSlot(0, itemStack);
            }
            else {
                INamedContainerProvider containerProvider = new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName() {
                        return new TranslationTextComponent("gui.icbm.cruise_launcher");
                    }

                    @Override
                    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                        return new ContainerCruiseLauncher(getContainerType(), state.getBlock(), i, world, pos, playerInventory, playerEntity);
                    }
                };
                NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getBlockPos());
            }
            SUpdateTileEntityPacket supdatetileentitypacket = tileEntity.getUpdatePacket();
            if (supdatetileentitypacket != null) ((ServerPlayerEntity)player).connection.send(supdatetileentitypacket);
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onRemove(BlockState originalState, World level, BlockPos blockPos, BlockState newState, boolean flag) {
        TileEntity tileEntity = level.getBlockEntity(blockPos);
        if(tileEntity instanceof TileCruiseLauncher) ((TileCruiseLauncher)tileEntity).onPlatformDestroyed();
        super.onRemove(originalState, level, blockPos, newState, flag);
    }

    public ContainerType<? extends Container> getContainerType() {
        return ContainerReg.CRUISE_LAUNCHER.get();
    }

}
