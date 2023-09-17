package com.jdawg3636.icbm.common.block.emp_tower;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.machine.TileMachine;
import com.jdawg3636.icbm.common.entity.EntityMissile;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class TileEMPTower extends TileMachine implements ITickableTileEntity {

    public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("gui.icbm.emp_tower");

    double animationPercent;
    double empRadius;

    public TileEMPTower(TileEntityType<?> tileEntityType) {
        super(tileEntityType, ContainerReg.EMP_TOWER::get, 1, 1_000_000, 5_000, 0, DEFAULT_NAME);
    }

    public void addAnimationPercent(double increment) {
        animationPercent += increment;
        while(animationPercent > 100) animationPercent -= 100D;
    }

    public float getAnimationRadians() {
        return (float)(animationPercent * 0.01 * 2 * Math.PI);
    }

    public void setEMPRadius(double empRadius) {
        empRadius = Math.min(Math.max(empRadius, ICBMReference.COMMON_CONFIG.getEMPTowerRangeMinimum()), ICBMReference.COMMON_CONFIG.getEMPTowerRangeMaximum());
        this.empRadius = empRadius;
    }

    public double getEMPRadius() {
        return empRadius;
    }

    public void triggerEMPBlast() {
        if(level == null || level.isClientSide) return;
        List<EntityMissile> missilesToZap = level.getEntitiesOfClass(EntityMissile.class, new AxisAlignedBB(getBlockPos().above()).inflate(empRadius));
        for(EntityMissile missile : missilesToZap) {
            if(missile.missileLaunchPhase != EntityMissile.MissileLaunchPhase.STATIONARY) {
                missile.strikeWithEMP();
            }
        }
    }

    @Override
    public void tick() {
        if(level == null) return;
        if(level.isClientSide()) {
            addAnimationPercent(5D);
        } else {
            if(redstoneSignalPresent()) {
                triggerEMPBlast();
            }
        }
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        this.setEMPRadius(tag.getDouble("emp_radius"));
        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.putDouble("emp_radius", empRadius);
        return super.save(tag);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox().expandTowards(0, 1, 0).inflate(1D/16D, 0, 1D/16D);
    }

}
