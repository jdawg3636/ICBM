package com.jdawg3636.icbm.common.block.proximity_detector;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.machine.TileMachine;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class TileProximityDetector extends TileMachine implements ITickableTileEntity {

    public static enum SlotIDs {
        BATTERY,
    }

    public TileProximityDetector(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn, ContainerReg.PROXIMITY_DETECTOR::get, ContainerProximityDetector::new, 1, 10_000, 50, 0, new ArrayList<>(), new TranslationTextComponent("gui." + ICBMReference.MODID + ".proximity_detector"));
    }

    @Override
    public void tick() {
        assert this.level != null;
        // Ingest energy from battery
        this.getEnergyStorageOfItemInSlot(SlotIDs.BATTERY.ordinal()).ifPresent(batteryEnergyStorage -> {
            tryReceiveEnergy(batteryEnergyStorage, batteryEnergyStorage.getEnergyStored());
        });
    }

    @Override
    public boolean isInventoryItemValid(int slot, @Nonnull ItemStack stack) {
        if(slot == SlotIDs.BATTERY.ordinal()) {
            return stack.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::canExtract).orElse(false);
        }
        return false;
    }

    public int getSignal(BlockState blockState, IBlockReader level, BlockPos blockPos, Direction p_180656_4_) {
        return 15;
    }

}
