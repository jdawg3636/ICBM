package com.jdawg3636.icbm.common.block.proximity_detector;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.launcher_control_panel.ScreenLauncherControlPanel;
import com.jdawg3636.icbm.common.block.machine.ScreenMachine;
import com.jdawg3636.icbm.common.block.machine.WidgetCustomizableSlider;
import com.jdawg3636.icbm.common.block.machine.WidgetProgressBar;
import com.jdawg3636.icbm.common.capability.energystorage.ICBMEnergyStorage;
import com.jdawg3636.icbm.common.network.CPacketUpdateProximityDetector;
import com.jdawg3636.icbm.common.network.ICBMNetworking;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ScreenProximityDetector extends ScreenMachine<ContainerProximityDetector> {

    public final double MIN_RADIUS;
    public final double MAX_RADIUS;

    public final TileProximityDetector TILE_ENTITY;
    public WidgetProgressBar forgeEnergyStorageBar;
    public WidgetCustomizableSlider detectionRadiusSlider;
    public TextFieldWidget textFieldExclusionFrequency;

    private boolean hasBeenUpdated;

    public ScreenProximityDetector(ContainerProximityDetector container, PlayerInventory inventory, ITextComponent name) {
        super(container, inventory, name);
        TILE_ENTITY = (TileProximityDetector)super.TILE_ENTITY;
        this.MIN_RADIUS = ICBMReference.COMMON_CONFIG.getProximityDetectorRadiusMinimum();
        this.MAX_RADIUS = ICBMReference.COMMON_CONFIG.getProximityDetectorRadiusMaximum();
        this.hasBeenUpdated = false;
    }

    public double rawRangeToSliderValue(double range) {
        return (range - MIN_RADIUS) / (MAX_RADIUS - MIN_RADIUS);
    }

    public double sliderValueToRawRange(double sliderValue) {
        return MathHelper.lerp(sliderValue, MIN_RADIUS, MAX_RADIUS);
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

        this.detectionRadiusSlider = new WidgetCustomizableSlider(
                (this.width / 2) - 75, relY + 61, 130, 20,
                new TranslationTextComponent(TILE_ENTITY.getRadiusSliderText()),
                rawRangeToSliderValue(TILE_ENTITY.getDetectionRadius()),
                (slider) -> {
                    slider.setValue(rawRangeToSliderValue((int)sliderValueToRawRange(slider.getValue())));
                },
                (slider) -> {
                    ITextComponent sliderValueAsText = new StringTextComponent(String.format("%.0f", sliderValueToRawRange(slider.getValue())));
                    slider.setMessage(slider.defaultMessage.copy().append(": ").append(sliderValueAsText));
                }
        );
        addButton(this.detectionRadiusSlider);

        this.textFieldExclusionFrequency = new TextFieldWidget(this.font, this.width / 2 + 7, ((height - imageHeight) / 2) + 20 + (15 * 1), (imageWidth / 2) - 14, 12, new TranslationTextComponent("gui." + ICBMReference.MODID + ".proximity_detector.exclusion_frequency"));
        this.textFieldExclusionFrequency.setFilter(ScreenLauncherControlPanel::stringIsPositiveInteger);
        this.textFieldExclusionFrequency.setMaxLength(32500);
        this.textFieldExclusionFrequency.setValue(String.valueOf(TILE_ENTITY.getExclusionFrequency()));
        this.textFieldExclusionFrequency.moveCursorToStart();
        addButton(this.textFieldExclusionFrequency);

        // TODO: Detection Mode (3 Buttons)

    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        int relX = (this.getWidth() - this.getImageWidth()) / 2;
        this.font.draw(matrixStack, "amongus", relX + (float)this.inventoryLabelX, (float)this.inventoryLabelY + 10, 0x404040);
    }

    @Override
    public void updateGui() {
        if(!this.hasBeenUpdated) {
            this.hasBeenUpdated = true;
            this.detectionRadiusSlider.setValue(this.rawRangeToSliderValue(TILE_ENTITY.getDetectionRadius()));
            this.textFieldExclusionFrequency.setValue(String.valueOf(TILE_ENTITY.getExclusionFrequency()));
            // TODO update for detection mode
        }
    }
    
    @Override
    public void sendUpdatePacketToServer() {
        if(TILE_ENTITY == null) return;
        BlockPos pos = TILE_ENTITY.getBlockPos();
        double detectionRadius = sliderValueToRawRange(this.detectionRadiusSlider.getValue());
        int exclusionFrequency = 0;
        try {
            exclusionFrequency = Integer.parseInt(textFieldExclusionFrequency.getValue());
        } catch (NumberFormatException ignored) {}
        // TODO update for detection mode
        ICBMNetworking.INSTANCE.sendToServer(new CPacketUpdateProximityDetector(pos, detectionRadius, exclusionFrequency));
    }

}
