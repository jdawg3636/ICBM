package com.jdawg3636.icbm.common.block.radar_station;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileRadarStation extends TileEntity implements ITickableTileEntity {

    double animationPercent;

    public TileRadarStation(TileEntityType<?> tileEntityType) {
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
    public void tick() {
        if(level != null && level.isClientSide()) addAnimationPercent(5D);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox().expandTowards(0, 1, 0).inflate(1, 0, 1);
    }

    @Override
    public double getViewDistance() {
        return ICBMReference.distProxy().getTileEntityUpdateDistance();
    }

}
