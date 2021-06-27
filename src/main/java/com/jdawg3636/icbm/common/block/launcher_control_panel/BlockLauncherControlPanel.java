package com.jdawg3636.icbm.common.block.launcher_control_panel;

import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMachine;
import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMachineTile;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

public class BlockLauncherControlPanel extends AbstractBlockMachineTile {

    public BlockLauncherControlPanel(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        this(getMultiblockMachineBlockProperties(), tileEntityType);
    }

    public BlockLauncherControlPanel(AbstractBlock.Properties properties, RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        super(properties, tileEntityType);
    }

    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        TileEntity tileentity = world.getBlockEntity(blockPos);
        if (tileentity instanceof TileLauncherControlPanel) {
            if(world.isClientSide()) {
                Minecraft.getInstance().setScreen(new ScreenLauncherControlPanel((TileLauncherControlPanel) tileentity));
            } else {
                SUpdateTileEntityPacket supdatetileentitypacket = tileentity.getUpdatePacket();
                if (supdatetileentitypacket != null) ((ServerPlayerEntity) playerEntity).connection.send(supdatetileentitypacket);
            }
            return ActionResultType.sidedSuccess(world.isClientSide);
        } else {
            return ActionResultType.PASS;
        }
    }

}
