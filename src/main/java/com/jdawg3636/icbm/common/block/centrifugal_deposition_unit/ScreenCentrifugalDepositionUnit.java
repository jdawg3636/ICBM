package com.jdawg3636.icbm.common.block.centrifugal_deposition_unit;

import com.jdawg3636.icbm.common.block.machine.ContainerMachine;
import com.jdawg3636.icbm.common.block.machine.ScreenMachine;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ScreenCentrifugalDepositionUnit extends ScreenMachine<ContainerMachine> {

    public final TileCentrifugalDepositionUnit TILE_ENTITY;

    public ScreenCentrifugalDepositionUnit(ContainerMachine container, PlayerInventory inventory, ITextComponent name) {
        super(container, inventory, name);
        TILE_ENTITY = (TileCentrifugalDepositionUnit)super.TILE_ENTITY;
    }

    public ScreenCentrifugalDepositionUnit(ContainerMachine container, PlayerInventory inventory, ITextComponent name, ResourceLocation backgroundTexture, int imageWidth, int imageHeight) {
        super(container, inventory, name, backgroundTexture, imageWidth, imageHeight);
        TILE_ENTITY = (TileCentrifugalDepositionUnit)super.TILE_ENTITY;
    }

    @Override
    protected void init() {
        super.init();
    }

}
