package com.jdawg3636.icbm.common.block.launcher_control_panel;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.launcher_platform.TileLauncherPlatform;
import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMulti;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public abstract class TileLauncherControlPanel extends TileEntity implements ITileLaunchControlPanel {

    public TileLauncherControlPanel(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public void setTargetX(double targetX) {}

    public void setTargetZ(double targetZ) {}

    public void setTargetY(double targetY) {}

    public void setRadioFrequency(int radioFrequency) {}

    public double getTargetX() {
        return 0D;
    }

    public double getTargetZ() {
        return 0D;
    }

    public double getTargetY() {
        return 63D;
    }

    public int getRadioFrequency() {
        return 0;
    }

    // TODO Implement Varying Accuracy Based on Support Frames
    public void launchMissile() {
        if(level == null) return;
        BlockPos platformPos = getBlockPos().offset(getBlockState().getValue(BlockLauncherControlPanel.FACING).getOpposite().getNormal());
        BlockState platformState = level.getBlockState(platformPos);
        if(platformState.getBlock() instanceof AbstractBlockMulti) {
            platformPos = ((AbstractBlockMulti) platformState.getBlock()).getMultiblockCenter(level, platformPos, platformState);
            platformState = level.getBlockState(platformPos);
        }
        TileEntity platformTile = level.getBlockEntity(platformPos);
        BlockPos targetPos = new BlockPos(getTargetX(), getTargetY(), getTargetZ());
        if(platformTile instanceof TileLauncherPlatform && ((TileLauncherPlatform) platformTile).usesControlPanel()) {
            ((TileLauncherPlatform)platformTile).launchMissile(platformPos, targetPos, Math.max(level.getHeight() * 1.5F, getBlockPos().getY() + 128F), (int)Math.sqrt((platformPos.getX() - getTargetX()) * (platformPos.getX() - getTargetX()) + (platformPos.getZ() - getTargetZ()) * (platformPos.getZ() - getTargetZ())));
        }
    }

    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        // todo: use getTileEntityPos() when move to machine backend
        return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        return save(tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        if(level != null && level.isClientSide()) {
            handleUpdateTag(getBlockState(), pkt.getTag());
            ICBMReference.distProxy().updateScreenMachine(pkt.getPos());
        }
    }

    @Override
    public double getViewDistance() {
        return ICBMReference.getTileEntityUpdateDistance();
    }

}
