package com.jdawg3636.icbm.common.block;

import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public class BlockGlassButton extends AbstractButtonBlock {

    public BlockGlassButton() {
        super(false, Block.Properties.of(Material.GLASS).randomTicks().sound(SoundType.GLASS).strength(0.5F).noCollission());
    }

    protected SoundEvent getSound(boolean p_196369_1_) {
        return p_196369_1_ ? SoundEvents.STONE_BUTTON_CLICK_ON : SoundEvents.STONE_BUTTON_CLICK_OFF;
    }

}
