package com.jdawg3636.icbm.common.block.emp_tower;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.machine.CustomizableSliderWidget;
import com.jdawg3636.icbm.common.block.machine.ScreenMachine;
import com.jdawg3636.icbm.common.network.CPacketUpdateEMPTower;
import com.jdawg3636.icbm.common.network.ICBMNetworking;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
    public CustomizableSliderWidget rangeSlider;

    public ScreenEMPTower(ContainerEMPTower container, PlayerInventory inventory, ITextComponent name) {
        super(container, inventory, name);
        this.TILE_ENTITY = (TileEMPTower)super.TILE_ENTITY;
        this.MIN_RANGE = ICBMReference.COMMON_CONFIG.getEMPTowerRangeMinimum();
        this.MAX_RANGE = ICBMReference.COMMON_CONFIG.getEMPTowerRangeMaximum();
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
        this.rangeSlider = new CustomizableSliderWidget((this.width / 2) - 75, ((height - imageHeight) / 2) + 100, 150, 20, new TranslationTextComponent("gui.icbm.emp_tower.radius"), rawRangeToSliderValue(TILE_ENTITY.getEMPRadius()), (slider) -> {}, (slider) -> {
            ITextComponent sliderValueAsText = new StringTextComponent(String.format("%.2f", sliderValueToRawRange(slider.getValue())));
            slider.setMessage(slider.defaultMessage.copy().append(": ").append(sliderValueAsText));
        });
        addButton(this.rangeSlider);
        this.children.add(this.rangeSlider);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.rangeSlider.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateGui() {
        this.rangeSlider.setValue(this.rawRangeToSliderValue(TILE_ENTITY.getEMPRadius()));
    }

    @Override
    public void sendUpdatePacketToServer() {
        if(TILE_ENTITY == null) return;
        BlockPos pos = TILE_ENTITY.getBlockPos();
        double empRadius = sliderValueToRawRange(this.rangeSlider.getValue());
        ICBMNetworking.INSTANCE.sendToServer(new CPacketUpdateEMPTower(pos, empRadius));
    }

}
