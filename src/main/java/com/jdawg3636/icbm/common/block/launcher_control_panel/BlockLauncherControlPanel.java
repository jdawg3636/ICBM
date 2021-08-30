package com.jdawg3636.icbm.common.block.launcher_control_panel;

import com.jdawg3636.icbm.ICBM;
import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMachineTile;
import com.jdawg3636.icbm.common.block.multiblock.IMissileLaunchApparatus;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
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
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

public class BlockLauncherControlPanel extends AbstractBlockMachineTile implements IMissileLaunchApparatus {

    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    public BlockLauncherControlPanel(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        this(getMultiblockMachineBlockProperties(), tileEntityType);
    }

    public BlockLauncherControlPanel(AbstractBlock.Properties properties, RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        super(properties, tileEntityType);
        this.registerDefaultState(defaultBlockState().setValue(TRIGGERED, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TRIGGERED);
    }

    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        TileEntity tileEntity = world.getBlockEntity(blockPos);
        if (tileEntity instanceof TileLauncherControlPanel) {
            if(world.isClientSide()) {
                ICBMReference.proxy.setScreenLauncherControlPanel((TileLauncherControlPanel) tileEntity);
            } else {
                SUpdateTileEntityPacket supdatetileentitypacket = tileEntity.getUpdatePacket();
                if (supdatetileentitypacket != null) ((ServerPlayerEntity) playerEntity).connection.send(supdatetileentitypacket);
            }
            return ActionResultType.sidedSuccess(world.isClientSide);
        } else {
            return ActionResultType.PASS;
        }
    }

    @Override
    public void neighborChanged(BlockState blockState, World level, BlockPos blockPos, Block p_220069_4_, BlockPos p_220069_5_, boolean p_220069_6_) {
        if (!level.isClientSide) {
            boolean flagHasSignal = level.hasNeighborSignal(blockPos) || level.hasNeighborSignal(blockPos.above());
            boolean flagStateTriggered = blockState.getValue(TRIGGERED);
            if (flagHasSignal && !flagStateTriggered) {
                level.setBlock(blockPos, blockState.setValue(TRIGGERED, Boolean.TRUE), 4);
                TileEntity tileentity = level.getBlockEntity(blockPos);
                if (tileentity instanceof TileLauncherControlPanel) ((TileLauncherControlPanel)tileentity).launchMissile();
            } else if (!flagHasSignal && flagStateTriggered) {
                level.setBlock(blockPos, blockState.setValue(TRIGGERED, Boolean.FALSE), 4);
            }

        }
    }

}
