package com.jdawg3636.icbm.common.block.cruise_launcher;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.launcher_control_panel.ITileLaunchControlPanel;
import com.jdawg3636.icbm.common.block.launcher_platform.TileLauncherPlatform;
import com.jdawg3636.icbm.common.entity.EntityMissile;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class TileCruiseLauncher extends TileLauncherPlatform implements ITileLaunchControlPanel {

    // Client
    private double yawRadiansSrc = 0;
    private double pitchRadiansSrc = 0;
    private double yawRadiansDst = 0;
    private double pitchRadiansDst = 0;
    private long tickAnimationStarted = 0;

    // Common
    private double targetX;
    private double targetZ;
    private double targetY;
    private int radioFrequency;

    public TileCruiseLauncher(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    private void updateRotation(long tickAnimationStarted) {

        // Update Source Pos
        yawRadiansSrc = getYawRadians();
        pitchRadiansSrc = getPitchRadians();

        // Compute Constants
        final double x = getBlockPos().getX() + 0.5;
        final double y = getBlockPos().getY() + 0.5;
        final double z = getBlockPos().getZ() + 0.5;
        final double deltaX = getTargetX() - x;
        final double deltaY = getTargetY() - y;
        final double deltaZ = getTargetZ() - z;
        final double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        // Compute Angle
        double yawRadiansDst = Math.atan2(deltaX, deltaZ) - Math.PI;
        double pitchRadiansDst = Math.atan2(deltaY, horizontalDistance);
        if(yawRadiansDst < Math.PI) yawRadiansDst += 2 * Math.PI;

        // Assign Result
        this.yawRadiansDst = yawRadiansDst;
        this.pitchRadiansDst = pitchRadiansDst;
        this.tickAnimationStarted = tickAnimationStarted;

        // Update Nearby Clients
        SUpdateTileEntityPacket updatePacket = this.getUpdatePacket();
        if(updatePacket != null && level != null && level.getServer() != null) {
            level.getServer().getPlayerList().broadcast(null, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), ICBMReference.proxy.getTileEntityUpdateDistance(), level.dimension(), updatePacket);
        }

    }

    @Override
    public double getYawRadians() {
        return level != null && level.isClientSide() ? MathHelper.rotLerp((float)MathHelper.clamp((level.getGameTime() - tickAnimationStarted) / 40D, 0D, 1D), (float)yawRadiansSrc, (float)yawRadiansDst) : yawRadiansDst;
    }

    @Override
    public double getPitchRadians() {
        return level != null && level.isClientSide() ? MathHelper.rotLerp((float)MathHelper.clamp((level.getGameTime() - tickAnimationStarted) / 40D, 0D, 1D), (float)pitchRadiansSrc, (float)pitchRadiansDst) : pitchRadiansDst;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox().expandTowards(0, 1, 0).inflate(0.5, 0.5, 0.5);
    }

    @Override
    public void setTargetX(double targetX) {
        this.targetX = targetX;
        if(level != null) updateRotation(level.getGameTime());
    }

    @Override
    public void setTargetZ(double targetZ) {
        this.targetZ = targetZ;
        if(level != null) updateRotation(level.getGameTime());
    }

    @Override
    public void setTargetY(double targetY) {
        this.targetY = targetY;
        if(level != null) updateRotation(level.getGameTime());
    }

    @Override
    public void setRadioFrequency(int radioFrequency) {
        if(radioFrequency >= 0) this.radioFrequency = radioFrequency;
    }

    @Override
    public double getTargetX() {
        return targetX;
    }

    @Override
    public double getTargetZ() {
        return targetZ;
    }

    @Override
    public double getTargetY() {
        return targetY;
    }

    @Override
    public int getRadioFrequency() {
        return radioFrequency;
    }

    @Override
    public boolean usesControlPanel() {
        return false;
    }

    @Override
    public EntityMissile.MissileSourceType getMissileSourceType() {
        return EntityMissile.MissileSourceType.CRUISE_LAUNCHER;
    }

    @Override
    public void launchMissile() {
        if(level == null) return;
        launchMissile(getBlockPos(), new BlockPos(targetX, targetY, targetZ), level.getHeight(), (int)Math.sqrt((getBlockPos().getX() - getTargetX()) * (getBlockPos().getX() - getTargetX()) + (getBlockPos().getZ() - getTargetZ()) * (getBlockPos().getZ() - getTargetZ())));
    }

    @Override
    public CompoundNBT save(CompoundNBT compoundNBT) {
        super.save(compoundNBT);
        compoundNBT.putDouble("targetX", targetX);
        compoundNBT.putDouble("targetZ", targetZ);
        compoundNBT.putDouble("targetY", targetY);
        compoundNBT.putInt("radioFrequency", radioFrequency);
        return compoundNBT;
    }

    @Override
    public void load(BlockState blockState, CompoundNBT compoundNBT) {
        super.load(blockState, compoundNBT);
        setTargetX(compoundNBT.getDouble("targetX"));
        setTargetZ(compoundNBT.getDouble("targetZ"));
        setTargetY(compoundNBT.getDouble("targetY"));
        setRadioFrequency(compoundNBT.getInt("radioFrequency"));
    }

    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
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
            ICBMReference.proxy.updateScreenLauncherControlPanel();
        }
    }

    @Override
    public double getViewDistance() {
        return ICBMReference.proxy.getTileEntityUpdateDistance();
    }

}
