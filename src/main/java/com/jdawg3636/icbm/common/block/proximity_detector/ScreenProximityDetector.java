package com.jdawg3636.icbm.common.block.proximity_detector;

import com.jdawg3636.icbm.common.block.machine.ScreenMachine;
import com.jdawg3636.icbm.common.block.machine.WidgetProgressBar;
import com.jdawg3636.icbm.common.capability.energystorage.ICBMEnergyStorage;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ScreenProximityDetector extends ScreenMachine<ContainerProximityDetector> {

    public final TileProximityDetector TILE_ENTITY;
    public WidgetProgressBar forgeEnergyStorageBar;

    public ScreenProximityDetector(ContainerProximityDetector container, PlayerInventory inventory, ITextComponent name) {
        super(container, inventory, name);
        TILE_ENTITY = (TileProximityDetector)super.TILE_ENTITY;
    }

    public ScreenProximityDetector(ContainerProximityDetector container, PlayerInventory inventory, ITextComponent name, ResourceLocation backgroundTexture, int imageWidth, int imageHeight) {
        super(container, inventory, name, backgroundTexture, imageWidth, imageHeight);
        TILE_ENTITY = (TileProximityDetector)super.TILE_ENTITY;
    }

    @Override
    protected void init() {

        super.init();

        int relX = (this.getWidth() - this.getImageWidth()) / 2;
        int relY = (this.getHeight() - this.getImageHeight()) / 2;

        Vector3f forgeEnergyStorageBarColor = new Vector3f(29/255f, 194/255f, 68/255f);
        this.forgeEnergyStorageBar = new WidgetProgressBar(
                relX + 151, relY + 45, 18, 65, false, this,
                () -> forgeEnergyStorageBarColor,
                () -> TILE_ENTITY.energyStorageLazyOptional.map((energyStorage) -> energyStorage.getEnergyStored() / (float)energyStorage.getMaxEnergyStored()).orElse(0f),
                () -> TILE_ENTITY.energyStorageLazyOptional.map((energyStorage) ->
                        ((ICBMEnergyStorage)energyStorage).getEnergyStoredFormatted(3, true)).orElse(new StringTextComponent("ERROR!"))
        );
        addButton(this.forgeEnergyStorageBar);

    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        int relX = (this.getWidth() - this.getImageWidth()) / 2;
        this.font.draw(matrixStack, "amongus", relX + (float)this.inventoryLabelX, (float)this.inventoryLabelY + 10, 0x404040);
    }

}
