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
import net.minecraft.client.gui.widget.button.Button;
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
    public Button[] buttonsDetectionMode;

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

        final int relX = (this.getWidth() - this.getImageWidth()) / 2;
        final int relY = (this.getHeight() - this.getImageHeight()) / 2;

        Vector3f forgeEnergyStorageBarColor = new Vector3f(29/255f, 194/255f, 68/255f);
        this.forgeEnergyStorageBar = new WidgetProgressBar(
                relX + 151, relY + 45, 18, 65, false, this,
                () -> forgeEnergyStorageBarColor,
                () -> TILE_ENTITY.energyStorageLazyOptional.map((energyStorage) -> energyStorage.getEnergyStored() / (float)energyStorage.getMaxEnergyStored()).orElse(0f),
                () -> TILE_ENTITY.energyStorageLazyOptional.map((energyStorage) ->
                        ((ICBMEnergyStorage)energyStorage).getEnergyStoredFormatted(3, true)).orElse(new StringTextComponent("ERROR!"))
        );
        addButton(this.forgeEnergyStorageBar);

        // Detection Mode (3 Buttons)
        final int numDetectionModes = TileProximityDetector.DetectionModes.values().length;
        final int detectionModeButtonPadding = 1;
        final int buttonDetectionModeWidth = (137 - detectionModeButtonPadding * (numDetectionModes - 1)) / numDetectionModes;
        this.buttonsDetectionMode = new Button[numDetectionModes];
        for(int i = 0; i < numDetectionModes; ++i) {
            TileProximityDetector.DetectionModes detectionMode = TileProximityDetector.DetectionModes.values()[i];
            final int buttonX = relX + 7 + i * (detectionModeButtonPadding + buttonDetectionModeWidth);
            this.buttonsDetectionMode[i] = new Button(buttonX, relY + 20, buttonDetectionModeWidth, 20, detectionMode.buttonLabel, button -> {
                for(Button otherButton : this.buttonsDetectionMode) {
                    otherButton.active = true;
                }
                button.active = false;
            });
            if(TILE_ENTITY.getDetectionMode() == detectionMode) {
                this.buttonsDetectionMode[i].active = false;
            }
            addButton(this.buttonsDetectionMode[i]);
        }

        this.detectionRadiusSlider = new WidgetCustomizableSlider(
                relX + 7, relY + 58, 137, 20,
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

        this.textFieldExclusionFrequency = new TextFieldWidget(this.font, relX + 69, relY + 97, (imageWidth / 2) - 14, 12, new TranslationTextComponent("gui." + ICBMReference.MODID + ".proximity_detector.exclusion_frequency"));
        this.textFieldExclusionFrequency.setFilter(ScreenLauncherControlPanel::stringIsPositiveInteger);
        this.textFieldExclusionFrequency.setMaxLength(32500);
        this.textFieldExclusionFrequency.setValue(String.valueOf(TILE_ENTITY.getExclusionFrequency()));
        this.textFieldExclusionFrequency.moveCursorToStart();
        addButton(this.textFieldExclusionFrequency);

    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        final int relX = (this.getWidth() - this.getImageWidth()) / 2;
        final int relY = (this.getHeight() - this.getImageHeight()) / 2;
        this.font.draw(matrixStack, "Frequency", relX + (float)this.inventoryLabelX, relY + 99, 0x353535);
    }

    @Override
    public void updateGui() {
        if(!this.hasBeenUpdated) {
            this.hasBeenUpdated = true;
            for(Button button : this.buttonsDetectionMode) {
                button.active = true;
            }
            this.buttonsDetectionMode[TILE_ENTITY.getDetectionMode().ordinal()].active = false;
            this.detectionRadiusSlider.setValue(this.rawRangeToSliderValue(TILE_ENTITY.getDetectionRadius()));
            this.textFieldExclusionFrequency.setValue(String.valueOf(TILE_ENTITY.getExclusionFrequency()));
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
        int detectionMode = 0;
        for(int i = 0; i < buttonsDetectionMode.length; ++i) {
            if(!buttonsDetectionMode[i].active) {
                detectionMode = i;
            }
        }
        ICBMNetworking.INSTANCE.sendToServer(new CPacketUpdateProximityDetector(pos, detectionMode, detectionRadius, exclusionFrequency));
    }

}
