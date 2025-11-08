package com.jdawg3636.icbm.common.block.proximity_detector;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.machine.TileMachine;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;

public class TileProximityDetector extends TileMachine implements ITickableTileEntity {

    public TileProximityDetector(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn, ContainerReg.PROXIMITY_DETECTOR::get, ContainerProximityDetector::new, 1, 10_000, 50, 0, new ArrayList<>(), new TranslationTextComponent("gui." + ICBMReference.MODID + ".proximity_detector"));
    }

    @Override
    public void tick() {

    }

}
