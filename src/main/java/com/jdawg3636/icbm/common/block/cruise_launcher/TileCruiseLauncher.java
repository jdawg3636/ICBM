package com.jdawg3636.icbm.common.block.cruise_launcher;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.launcher_control_panel.ITileLaunchControlPanel;
import com.jdawg3636.icbm.common.block.launcher_platform.TileLauncherPlatform;
import com.jdawg3636.icbm.common.entity.EntityMissile;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class TileCruiseLauncher extends TileLauncherPlatform implements ITickableTileEntity, ITileLaunchControlPanel {

    // Client
    double animationPercent;

    // Common
    private double targetX;
    private double targetZ;
    private double targetY;
    private int radioFrequency;

    public TileCruiseLauncher(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public void addAnimationPercent(double increment) {
        animationPercent += increment;
        while(animationPercent > 100) animationPercent -= 100D;
    }

    public float getAnimationRadians() {
        return (float)(animationPercent * 0.01 * 2 * Math.PI);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox().expandTowards(0, 1, 0).inflate(0.5, 0.5, 0.5);
    }

    @Override
    public void tick() {
        if(level != null && level.isClientSide()) addAnimationPercent(5D);
    }

    @Override
    public void setTargetX(double targetX) {
        this.targetX = targetX;
    }

    @Override
    public void setTargetZ(double targetZ) {
        this.targetZ = targetZ;
    }

    @Override
    public void setTargetY(double targetY) {
        this.targetY = targetY;
    }

    @Override
    public void setRadioFrequency(int radioFrequency) {
        this.radioFrequency = radioFrequency;
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
        targetX = compoundNBT.getDouble("targetX");
        targetZ = compoundNBT.getDouble("targetZ");
        targetY = compoundNBT.getDouble("targetY");
        radioFrequency = compoundNBT.getInt("radioFrequency");
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

}
