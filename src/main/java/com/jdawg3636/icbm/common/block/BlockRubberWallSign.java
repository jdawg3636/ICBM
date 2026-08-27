package com.jdawg3636.icbm.common.block;

import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockRubberWallSign extends WallSignBlock {
    public BlockRubberWallSign(Properties properties, WoodType type) { super(properties, type); }
    @Override public TileEntity newBlockEntity(IBlockReader level) { return new TileRubberSign(); }
}
