package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.entity.EntityMissile;
import com.jdawg3636.icbm.common.reg.FluidReg;
import com.jdawg3636.icbm.common.reg.ICBMTags;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.NumberFormat;
import java.util.List;

public class ItemMissile extends Item {

    private final RegistryObject<EntityType<EntityMissile>> missileEntity;

    public ItemMissile(RegistryObject<EntityType<EntityMissile>> missileEntity) {
        this(new Item.Properties().tab(ICBMReference.CREATIVE_TAB).stacksTo(1), missileEntity);
    }

    public ItemMissile(Item.Properties properties, RegistryObject<EntityType<EntityMissile>> missileEntity) {
        super(properties);
        this.missileEntity = missileEntity;
    }

    public RegistryObject<EntityType<EntityMissile>> getMissileEntity() {
        return missileEntity;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if(stack.isEmpty()) return super.initCapabilities(stack, nbt);
        return new FluidHandlerItemStack(stack, 1000) {
            @Override
            public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
                return stack.getFluid().getTags().contains(ICBMTags.Fluids.FUEL.getName());
            }
            @Override
            public boolean canFillFluidType(FluidStack fluid) {
                return isFluidValid(0, fluid);
            }
        };
    }

    public double getPercentageFuelFilled(ItemStack stack) {
        if(!(stack.getItem() == this)) return 0.0;
        return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
                .filter(handler -> handler.getFluidInTank(0).getFluid().getTags().contains(ICBMTags.Fluids.FUEL.getName()))
                .map(handler -> {
                    return (handler.getFluidInTank(0).getAmount() / (double)handler.getTankCapacity(0));
                })
                .orElse(0.0);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getPercentageFuelFilled(stack) != 0.0;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1.0 - getPercentageFuelFilled(stack);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return FluidReg.FUEL.source.map(Fluid::getAttributes).map(FluidAttributes::getColor).map(color -> color & 0x00ffffff).orElse(0x00ffffff);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> tooltip, ITooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltip, tooltipFlag);
        if(level == null) return;
        double percentageFuelFilled = getPercentageFuelFilled(stack);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);
        tooltip.add(
                new TranslationTextComponent(
                        "info." + ICBMReference.MODID + ".missile.fuel_level",
                        nf.format(100 * percentageFuelFilled)
                ).setStyle(
                        Style.EMPTY
                        .withItalic(true)
                        .withColor(percentageFuelFilled < 1.0 ? Color.fromRgb(0xff3624) : Color.fromRgb(0x27db27))
                )
        );
    }


}
