package com.jdawg3636.icbm.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class BlockGlassPressurePlate extends PressurePlateBlock {

    public BlockGlassPressurePlate() {
        super(PressurePlateBlock.Sensitivity.EVERYTHING, Block.Properties.of(Material.GLASS).harvestTool(ToolType.PICKAXE).randomTicks().strength(0.3F, 1F).sound(SoundType.GLASS).noCollission());
        this.registerDefaultState(defaultBlockState().setValue(POWERED, false));
    }

}
