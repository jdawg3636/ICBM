package com.jdawg3636.icbm.common.block.metallurgic_leaching_chamber;

import com.jdawg3636.icbm.common.block.machine.TileMachine;
import com.jdawg3636.icbm.common.block.oil_refinery.ContainerOilRefinery;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;

public class TileMetallurgicLeachingChamber extends TileMachine implements ITickableTileEntity {

    public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("gui.icbm.metallurgic_leaching_chamber");

    public TileMetallurgicLeachingChamber(TileEntityType<?> tileEntityType) {
        this(tileEntityType, DEFAULT_NAME);
    }

    public TileMetallurgicLeachingChamber(TileEntityType<?> tileEntityType, ITextComponent name) {
        super(tileEntityType, ContainerReg.OIL_REFINERY::get, ContainerOilRefinery::new, 3, 9_000, 9_000, 0, new ArrayList<>(2), name);
    }

    @Override
    public void tick() {}

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getBlockPos(), getBlockPos().offset(1, 2, 1));
    }

}
