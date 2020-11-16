package icbm.classic.content.blocks;

import icbm.classic.ICBMConstants;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public class BlockGlassButton extends AbstractButtonBlock {

    public BlockGlassButton() {
        super(false, Block.Properties.create(Material.GLASS).tickRandomly().sound(SoundType.GLASS).hardnessAndResistance(0.5F).doesNotBlockMovement());
    }

    protected SoundEvent getSoundEvent(boolean p_196369_1_) {
        return p_196369_1_ ? SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON : SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF;
    }

}
