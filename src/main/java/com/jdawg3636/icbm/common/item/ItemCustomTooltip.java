package com.jdawg3636.icbm.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.function.Supplier;

public class ItemCustomTooltip extends Item {

    private Supplier<ITextComponent> tooltip;

    public ItemCustomTooltip(Item.Properties properties, Supplier<ITextComponent> tooltip) {
        super(properties);
        this.tooltip = tooltip;
    }

    public ITextComponent getTooltip() {
        return tooltip.get();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemStack, World level, List<ITextComponent> tooltip, ITooltipFlag flagAdvancedTooltips) {
        tooltip.add(this.tooltip.get());
    }

}
