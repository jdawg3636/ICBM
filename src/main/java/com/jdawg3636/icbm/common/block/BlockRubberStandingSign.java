package com.jdawg3636.icbm.common.block;

import net.minecraft.block.StandingSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockRubberStandingSign extends StandingSignBlock {
    public BlockRubberStandingSign(Properties properties, WoodType type) { super(properties, type); }
    @Override public TileEntity newBlockEntity(IBlockReader level) { return new TileRubberSign(); }
}
