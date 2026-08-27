package com.jdawg3636.icbm.common.block;

import com.jdawg3636.icbm.common.reg.TileReg;
import com.jdawg3636.icbm.mixin.accessors.ITileEntityAccessor;
import net.minecraft.tileentity.SignTileEntity;

public class TileRubberSign extends SignTileEntity {
    public TileRubberSign() { ((ITileEntityAccessor)this).icbm$setType(TileReg.RUBBER_WOOD_SIGN.get()); }
}
