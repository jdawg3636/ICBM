package com.jdawg3636.icbm.common.block.advanced_grindstone;

import com.jdawg3636.icbm.common.block.machine.ContainerMachine;
import com.jdawg3636.icbm.common.block.machine.ScreenMachine;
import com.jdawg3636.icbm.common.block.machine.WidgetProgressBar;
import com.jdawg3636.icbm.common.recipe.AdvancedGrindstoneRecipe;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ScreenAdvancedGrindstone extends ScreenMachine<ContainerMachine> {

    private final TileAdvancedGrindstone TILE_ENTITY;

    public ScreenAdvancedGrindstone(ContainerMachine container, PlayerInventory inventory, ITextComponent name) {
        super(container, inventory, name);
        this.TILE_ENTITY = (TileAdvancedGrindstone) super.TILE_ENTITY;
    }

    @Override
    protected void init() {
        super.init();
        int relX = (getWidth() - getImageWidth()) / 2;
        int relY = (getHeight() - getImageHeight()) / 2;
        addButton(new WidgetProgressBar(
                relX + 77, relY + 47, 22, 15, true, this,
                () -> new Vector3f(1.0F, 1.0F, 1.0F),
                () -> (float) TILE_ENTITY.getProcessingPercentage(),
                () -> new StringTextComponent(TILE_ENTITY.ticksProcessing + " / " + AdvancedGrindstoneRecipe.INTERACTIONS_REQUIRED)
        ));
    }

}
