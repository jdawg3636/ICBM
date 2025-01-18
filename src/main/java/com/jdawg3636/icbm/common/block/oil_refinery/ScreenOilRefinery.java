package com.jdawg3636.icbm.common.block.oil_refinery;

import com.jdawg3636.icbm.common.block.machine.ScreenMachine;
import com.jdawg3636.icbm.common.block.machine.WidgetFluidTank;
import com.jdawg3636.icbm.common.block.machine.WidgetProgressBar;
import com.jdawg3636.icbm.common.capability.energystorage.ICBMEnergyStorage;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class ScreenOilRefinery extends ScreenMachine<ContainerOilRefinery> {

    public final TileOilRefinery TILE_ENTITY;
    public WidgetProgressBar forgeEnergyStorageBar;
    public WidgetFluidTank inputTankView;
    public WidgetFluidTank outputTankView;
    public WidgetProgressBar progressBar;

    public ScreenOilRefinery(ContainerOilRefinery container, PlayerInventory inventory, ITextComponent name) {
        super(container, inventory, name);
        TILE_ENTITY = (TileOilRefinery)super.TILE_ENTITY;
    }

    public ScreenOilRefinery(ContainerOilRefinery container, PlayerInventory inventory, ITextComponent name, ResourceLocation backgroundTexture, int imageWidth, int imageHeight) {
        super(container, inventory, name, backgroundTexture, imageWidth, imageHeight);
        TILE_ENTITY = (TileOilRefinery)super.TILE_ENTITY;
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

        this.inputTankView = new WidgetFluidTank(
                relX + 7, relY + 45, 36, 65, this,
                () -> new StringTextComponent("" + TILE_ENTITY.fluidTanks.get(0).map(FluidTank::getFluidAmount).orElse(0)),
                TILE_ENTITY.fluidTanks.get(0)
        );
        addButton(this.inputTankView);

        this.outputTankView = new WidgetFluidTank(
                relX + 97, relY + 45, 36, 65, this,
                () -> new StringTextComponent("" + TILE_ENTITY.fluidTanks.get(1).map(FluidTank::getFluidAmount).orElse(0)),
                TILE_ENTITY.fluidTanks.get(1)
        );
        addButton(this.outputTankView);

        Vector3f progressBarColor = new Vector3f(1f, 1f, 1f);
        this.progressBar = new WidgetProgressBar(
                relX + 59, relY + 70, 22, 15, true, this,
                () -> progressBarColor,
                () -> (float) TILE_ENTITY.getPercentageFuelLeft(),
                () -> new StringTextComponent(TILE_ENTITY.getPercentageFuelLeft() * 100 + "%")
        );
        addButton(this.progressBar);

    }

}
