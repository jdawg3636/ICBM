package com.jdawg3636.icbm.common.block.emp_tower;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.machine.ScreenMachine;
import com.jdawg3636.icbm.common.block.machine.WidgetCustomizableSlider;
import com.jdawg3636.icbm.common.block.machine.WidgetProgressBar;
import com.jdawg3636.icbm.common.capability.energystorage.ICBMEnergyStorage;
import com.jdawg3636.icbm.common.network.CPacketUpdateEMPTower;
import com.jdawg3636.icbm.common.network.ICBMNetworking;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenEMPTower extends ScreenMachine<ContainerEMPTower> {

    public final double MIN_RANGE;
    public final double MAX_RANGE;

    public final TileEMPTower TILE_ENTITY;
    public WidgetCustomizableSlider rangeSlider;
    public WidgetProgressBar forgeEnergyStorageBar;

    private boolean hasBeenUpdated;

    public ScreenEMPTower(ContainerEMPTower container, PlayerInventory inventory, ITextComponent name) {
        super(container, inventory, name);
        this.TILE_ENTITY = (TileEMPTower)super.TILE_ENTITY;
        this.MIN_RANGE = ICBMReference.COMMON_CONFIG.getEMPTowerRangeMinimum();
        this.MAX_RANGE = ICBMReference.COMMON_CONFIG.getEMPTowerRangeMaximum();
        this.hasBeenUpdated = false;
    }

    public double rawRangeToSliderValue(double range) {
        return (range - MIN_RANGE) / (MAX_RANGE - MIN_RANGE);
    }

    public double sliderValueToRawRange(double sliderValue) {
        return MathHelper.lerp(sliderValue, MIN_RANGE, MAX_RANGE);
    }

    @Override
    protected void init() {

        super.init();

        int relX = (this.getWidth() - this.getImageWidth()) / 2;
        int relY = (this.getHeight() - this.getImageHeight()) / 2;

        this.rangeSlider = new WidgetCustomizableSlider(
            (this.width / 2) - 75, relY + 100, 130, 20,
            new TranslationTextComponent(TILE_ENTITY.getRadiusSliderText()),
            rawRangeToSliderValue(TILE_ENTITY.getEMPRadius()),
            (slider) -> {
                slider.setValue(rawRangeToSliderValue((int)sliderValueToRawRange(slider.getValue())));
            },
            (slider) -> {
                ITextComponent sliderValueAsText = new StringTextComponent(String.format("%.0f", sliderValueToRawRange(slider.getValue())));
                slider.setMessage(slider.defaultMessage.copy().append(": ").append(sliderValueAsText));
            }
        );
        addButton(this.rangeSlider);

        Vector3f forgeEnergyStorageBarColor = new Vector3f(29/255f, 194/255f, 68/255f);
        this.forgeEnergyStorageBar = new WidgetProgressBar(
            relX + 151, relY + 47, 18, 49, this,
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
        this.rangeSlider.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateGui() {
        if(!this.hasBeenUpdated) {
            this.hasBeenUpdated = true;
            this.rangeSlider.setValue(this.rawRangeToSliderValue(TILE_ENTITY.getEMPRadius()));
        }
    }

    @Override
    public void sendUpdatePacketToServer() {
        if(TILE_ENTITY == null) return;
        BlockPos pos = TILE_ENTITY.getBlockPos();
        double empRadius = sliderValueToRawRange(this.rangeSlider.getValue());
        ICBMNetworking.INSTANCE.sendToServer(new CPacketUpdateEMPTower(pos, empRadius));
    }

}
