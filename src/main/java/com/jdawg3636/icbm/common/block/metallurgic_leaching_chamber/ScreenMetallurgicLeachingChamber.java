package com.jdawg3636.icbm.common.block.metallurgic_leaching_chamber;

import com.jdawg3636.icbm.common.block.machine.ScreenMachine;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ScreenMetallurgicLeachingChamber extends ScreenMachine<ContainerMetallurgicLeachingChamber> {

    public final TileMetallurgicLeachingChamber TILE_ENTITY;

    public ScreenMetallurgicLeachingChamber(ContainerMetallurgicLeachingChamber container, PlayerInventory inventory, ITextComponent name) {
        super(container, inventory, name);
        TILE_ENTITY = (TileMetallurgicLeachingChamber)super.TILE_ENTITY;
    }

    public ScreenMetallurgicLeachingChamber(ContainerMetallurgicLeachingChamber container, PlayerInventory inventory, ITextComponent name, ResourceLocation backgroundTexture, int imageWidth, int imageHeight) {
        super(container, inventory, name, backgroundTexture, imageWidth, imageHeight);
        TILE_ENTITY = (TileMetallurgicLeachingChamber)super.TILE_ENTITY;
    }

    @Override
    protected void init() {
        super.init();
    }

}
