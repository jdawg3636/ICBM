package com.jdawg3636.icbm.common.block.coal_generator;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class TileCoalGenerator  extends TileEntity implements ITickableTileEntity {

    public TileCoalGenerator(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public void tick() {
        // TODO
    }

    @Override
    public double getViewDistance() {
        return ICBMReference.distProxy().getTileEntityUpdateDistance();
    }

}
