package com.jdawg3636.icbm.common.block.cruise_launcher;

import com.jdawg3636.icbm.common.block.launcher_control_panel.ITileLaunchControlPanel;
import com.jdawg3636.icbm.common.block.machine.AbstractBlockMachineTile;
import com.jdawg3636.icbm.common.block.multiblock.IMissileLaunchApparatus;
import com.jdawg3636.icbm.common.item.ItemMissile;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
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
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
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
import net.minecraftforge.items.ItemStackHandler;

public class BlockCruiseLauncher extends AbstractBlockMachineTile implements IMissileLaunchApparatus {

    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    public BlockCruiseLauncher(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        super(tileEntityType);
        this.registerDefaultState(defaultBlockState().setValue(TRIGGERED, Boolean.FALSE));
    }

    public BlockCruiseLauncher(AbstractBlock.Properties properties, RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        super(properties, tileEntityType);
        this.registerDefaultState(defaultBlockState().setValue(TRIGGERED, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TRIGGERED);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        TileEntity tileEntity = world.getBlockEntity(pos);
        if(!world.isClientSide() && tileEntity instanceof TileCruiseLauncher) {
            if (((TileCruiseLauncher)tileEntity).itemHandlerLazyOptional.isPresent() && ((TileCruiseLauncher)tileEntity).itemHandlerLazyOptional.orElse(null).getStackInSlot(0).isEmpty() && player.getItemInHand(hand).getItem() instanceof ItemMissile) {
                ItemStack itemStack = player.getItemInHand(hand);
                if(!player.isCreative()) player.inventory.removeItem(itemStack);
                ((ItemStackHandler)((TileCruiseLauncher)tileEntity).itemHandlerLazyOptional.orElse(null)).setStackInSlot(0, itemStack);
            }
            else {
                INamedContainerProvider containerProvider = new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName() {
                        return new TranslationTextComponent("gui.icbm.cruise_launcher");
                    }

                    @Override
                    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                        return new ContainerCruiseLauncher(getContainerType(), i, world, pos, playerInventory);
                    }
                };
                NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getBlockPos());
            }
            SUpdateTileEntityPacket supdatetileentitypacket = tileEntity.getUpdatePacket();
            if (supdatetileentitypacket != null) ((ServerPlayerEntity)player).connection.send(supdatetileentitypacket);
        }
        return ActionResultType.SUCCESS;
    }

    public ContainerType<? extends Container> getContainerType() {
        return ContainerReg.CRUISE_LAUNCHER.get();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState blockState, World level, BlockPos blockPos, Block block, BlockPos fromBlockPos, boolean isMoving) {
        if (!level.isClientSide) {
            boolean flagHasSignal = level.hasNeighborSignal(blockPos) || level.hasNeighborSignal(blockPos.above());
            boolean flagStateTriggered = blockState.getValue(TRIGGERED);
            if (flagHasSignal && !flagStateTriggered) {
                level.setBlock(blockPos, blockState.setValue(TRIGGERED, Boolean.TRUE), 4);
                TileEntity tileentity = level.getBlockEntity(blockPos);
                if (tileentity instanceof ITileLaunchControlPanel) ((ITileLaunchControlPanel)tileentity).launchMissile();
            } else if (!flagHasSignal && flagStateTriggered) {
                level.setBlock(blockPos, blockState.setValue(TRIGGERED, Boolean.FALSE), 4);
            }

        }
    }

}
