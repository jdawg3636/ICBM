package com.jdawg3636.icbm.common.block.launcher_control_panel;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.BlockLauncherSupportFrame;
import com.jdawg3636.icbm.common.block.launcher_platform.TileLauncherPlatform;
import com.jdawg3636.icbm.common.block.machine.AbstractBlockMachine;
import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMulti;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;

public abstract class TileLauncherControlPanel extends TileEntity implements ITileLaunchControlPanel {

    public TileLauncherControlPanel(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public void setTargetX(double targetX) {}

    @Override
    public void setTargetZ(double targetZ) {}

    @Override
    public void setTargetY(double targetY) {}

    @Override
    public void setRadioFrequency(int radioFrequency) {}

    @Override
    public double getTargetX() {
        return 0D;
    }

    @Override
    public double getTargetZ() {
        return 0D;
    }

    @Override
    public double getTargetY() {
        return 63D;
    }

    @Override
    public int getRadioFrequency() {
        return 0;
    }

    @Override
    public World deobf$getLevel() {
        return this.getLevel();
    }

    @Override
    public BlockPos deobf$getBlockPos() {
        return this.getBlockPos();
    }

    public boolean launchMissile() {
        if(level == null) return false;
        // Find Launcher Platform pos/state/tile
        Vector3i offsetFromPanelToPlatform = getBlockState().getValue(BlockLauncherControlPanel.FACING).getOpposite().getNormal();
        BlockPos platformPos = getBlockPos().offset(offsetFromPanelToPlatform);
        BlockState platformState = level.getBlockState(platformPos);
        if(platformState.getBlock() instanceof AbstractBlockMulti) {
            platformPos = ((AbstractBlockMulti) platformState.getBlock()).getMultiblockCenter(level, platformPos, platformState);
            platformState = level.getBlockState(platformPos);
        }
        TileEntity platformTile = level.getBlockEntity(platformPos);
        if(!(platformTile instanceof TileLauncherPlatform && ((TileLauncherPlatform) platformTile).usesControlPanel())) {
            return false;
        }
        // Find Launcher Support Frame Tier
        Direction platformFacingDirection = platformState.getValue(AbstractBlockMachine.FACING);
        BlockPos[] launcherSupportFramePotentialPositions = {
                platformPos.offset(platformFacingDirection.getNormal()),
                platformPos.offset(platformFacingDirection.getOpposite().getNormal())
        };
        int supportFrameTier = Arrays.stream(launcherSupportFramePotentialPositions)
                .map(level::getBlockState)
                .map(BlockState::getBlock)
                .filter(BlockLauncherSupportFrame.class::isInstance)
                .map(BlockLauncherSupportFrame.class::cast)
                .map(BlockLauncherSupportFrame::getTier)
                .reduce(Math::max)
                .orElse(0);
        // Identify target given input coordinates and applicable inaccuracy
        int inaccuracyPotential = ICBMReference.COMMON_CONFIG.getMissileInaccuracyGivenSupportFrameTier(supportFrameTier);
        BlockPos targetPos = new BlockPos(
                getTargetX() - inaccuracyPotential + level.getRandom().nextInt((2 * inaccuracyPotential) + 1),
                getTargetY() - inaccuracyPotential + level.getRandom().nextInt((2 * inaccuracyPotential) + 1),
                getTargetZ() - inaccuracyPotential + level.getRandom().nextInt((2 * inaccuracyPotential) + 1)
        );
        // Perform launch
        return ((TileLauncherPlatform)platformTile).launchMissile(
                platformPos,
                targetPos,
                Math.max(level.getHeight() * 1.5F, getBlockPos().getY() + 128F),
                (int)Math.sqrt(
                        (platformPos.getX() - targetPos.getX()) * (platformPos.getX() - targetPos.getX()) +
                        (platformPos.getZ() - targetPos.getZ()) * (platformPos.getZ() - targetPos.getZ())
                )
        );
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
