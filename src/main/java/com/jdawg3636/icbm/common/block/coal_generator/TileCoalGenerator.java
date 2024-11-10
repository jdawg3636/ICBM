package com.jdawg3636.icbm.common.block.coal_generator;

import com.jdawg3636.icbm.common.block.machine.TileMachine;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;

public class TileCoalGenerator extends TileMachine implements ITickableTileEntity {

    public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("gui.icbm.coal_generator");

    public static enum SlotIDs {
        FUEL,
        BATTERY,
    }

    public TileCoalGenerator(TileEntityType<?> tileEntityType) {
        this(tileEntityType, DEFAULT_NAME);
    }

    public TileCoalGenerator(TileEntityType<?> tileEntityType, ITextComponent name) {
        super(tileEntityType, ContainerReg.COAL_GENERATOR::get, ContainerCoalGenerator::new, 2, 1_000_000, 0, 1_000_000, name);
    }

    @Override
    public void tick() {

    }

    @Override
    public boolean isInventoryItemValid(int slot, @Nonnull ItemStack stack) {
        if(slot == SlotIDs.BATTERY.ordinal()) {
            return stack.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::canReceive).orElse(false);
        }
        if(slot == SlotIDs.FUEL.ordinal()) {
            return ForgeHooks.getBurnTime(stack, IRecipeType.SMELTING) > 0;
        }
        return false;
    }

}
