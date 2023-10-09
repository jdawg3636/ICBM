package com.jdawg3636.icbm.common.block.launcher_platform;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.machine.ScreenMachine;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenLauncherPlatform extends ScreenMachine<ContainerLauncherPlatform> {

    public static final ResourceLocation LAUNCHER_PLATFORM_BACKGROUND_TEXTURE = new ResourceLocation(ICBMReference.MODID, "textures/gui/gui_launcher.png");

    public ScreenLauncherPlatform(ContainerLauncherPlatform container, PlayerInventory inventory, ITextComponent name) {
        super(container, inventory, name, LAUNCHER_PLATFORM_BACKGROUND_TEXTURE, 176, 166);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.renderLabels(matrixStack, mouseX, mouseY);
        drawHorizontallyCenteredStringNoShadow(matrixStack, this.font, new TranslationTextComponent("gui.icbm.launcher_platform.place"), 1, 32, ICBMReference.ICBMTextColors.DARK_GRAY.code);
    }

    @Override
    public void renderSlotBackgrounds(MatrixStack matrixStack, int relX, int relY) {

    }

}
