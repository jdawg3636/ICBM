package com.jdawg3636.icbm.common.block.siren;

import com.jdawg3636.icbm.common.reg.SoundEventReg;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;

public class TileSiren extends TileEntity implements ITickableTileEntity {

    public int tickCount = 0;

    public TileSiren(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public void tick() {
        if(getLevel().isClientSide() && tickCount++ % 30 == 0) {
            if(getLevel().hasNeighborSignal(getBlockPos())) {
                getLevel().playLocalSound(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), SoundEventReg.EFFECT_ALARM.get(), SoundCategory.BLOCKS, 0.5f, 1.0f, false);
            }
        }
    }

}
