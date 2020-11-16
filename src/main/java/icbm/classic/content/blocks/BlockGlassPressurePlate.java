package icbm.classic.content.blocks;

import icbm.classic.ICBMConstants;
import net.minecraft.block.Block;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockGlassPressurePlate extends PressurePlateBlock {

    public BlockGlassPressurePlate() {
        super(PressurePlateBlock.Sensitivity.EVERYTHING, Block.Properties.create(Material.GLASS).tickRandomly().hardnessAndResistance(0.3F, 1F).sound(SoundType.GLASS).doesNotBlockMovement());
        this.setDefaultState(getDefaultState().with(POWERED, false));
    }

}
