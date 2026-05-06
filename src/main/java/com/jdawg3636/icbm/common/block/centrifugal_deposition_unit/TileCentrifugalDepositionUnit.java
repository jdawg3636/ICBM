package com.jdawg3636.icbm.common.block.centrifugal_deposition_unit;

import com.jdawg3636.icbm.common.block.machine.TileMachine;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;

public class TileCentrifugalDepositionUnit extends TileMachine implements ITickableTileEntity {

    public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("gui.icbm.centrifugal_deposition_unit");

    public TileCentrifugalDepositionUnit(TileEntityType<?> tileEntityType) {
        this(tileEntityType, DEFAULT_NAME);
    }

    public TileCentrifugalDepositionUnit(TileEntityType<?> tileEntityType, ITextComponent name) {
        super(tileEntityType, ContainerReg.OIL_REFINERY::get, ContainerCentrifugalDepositionUnit::new, 3, 9_000, 9_000, 0, new ArrayList<>(2), name);
    }

    @Override
    public void tick() {}

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getBlockPos().offset(-1, 0, -1), getBlockPos().offset(2, 3, 2));
    }

}
