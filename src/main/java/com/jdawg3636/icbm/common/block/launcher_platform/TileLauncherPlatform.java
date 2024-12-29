package com.jdawg3636.icbm.common.block.launcher_platform;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.machine.AbstractContainerMachine;
import com.jdawg3636.icbm.common.block.machine.TileMachine;
import com.jdawg3636.icbm.common.capability.missiledirector.MissileSourceType;
import com.jdawg3636.icbm.common.entity.EntityMissile;
import com.jdawg3636.icbm.common.item.ItemMissile;
import com.jdawg3636.icbm.common.reg.SoundEventReg;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class TileLauncherPlatform extends TileMachine {

    public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("gui.icbm.launcher_platform");
    public UUID missileEntityID = null;

    public TileLauncherPlatform(TileEntityType<?> tileEntityTypeIn, Supplier<ContainerType<? extends AbstractContainerMachine>> containerType, AbstractContainerMachine.IConstructor containerConstructor) {
        super(tileEntityTypeIn, containerType, containerConstructor, 1, 0, 0, 0, new ArrayList<>(), DEFAULT_NAME);
    }

    public double getMissileEntityYOffset() {
        return 5D/16D;
    }

    public boolean usesControlPanel() {
        return true;
    }

    public MissileSourceType getMissileSourceType() {
        return MissileSourceType.LAUNCHER_PLATFORM;
    }

    public double getYawRadians() {
        return 0D;
    }

    public double getPitchRadians() {
        return 0D;
    }

    public void removeMissileItemWithAction(BiFunction<ItemStack, EntityMissile, Boolean> action) {
        itemHandlerLazyOptional.ifPresent((itemHandlerUncast) -> {
            ItemStackHandler itemHandler = (ItemStackHandler)itemHandlerUncast;
            if(missileEntityID != null && level != null && !level.isClientSide()) {
                ItemStack itemStack = itemHandler.getStackInSlot(0);
                EntityMissile entity = (EntityMissile)(((ServerWorld)level).getEntity(missileEntityID));
                if(itemStack.getItem() instanceof ItemMissile) {
                    if(action.apply(itemStack, entity)) {
                        missileEntityID = null; // Necessary to disconnect, otherwise ItemStackHandler would kill the entity when the slot is cleared
                        itemHandler.setStackInSlot(0, ItemStack.EMPTY);
                    }
                }
            }
        });
    }

    public void launchMissile(BlockPos sourcePos, BlockPos destPos, float peakHeight, int totalFlightTicks) {
        removeMissileItemWithAction((itemStack, entity) -> {
            assert level != null;
            if(entity == null) return false;
            boolean hasEnoughFuel = Optional.of(itemStack)
                    .filter(stack -> stack.getItem() instanceof ItemMissile)
                    .map(stack -> ((ItemMissile)stack.getItem()).getPercentageFuelFilled(stack))
                    .map(percentageFuel -> percentageFuel >= 1.0)
                    .orElse(false);
            if(!hasEnoughFuel) return false;
            entity.updateMissileData(sourcePos, destPos, peakHeight, totalFlightTicks, getMissileSourceType());
            if(entity.launchMissile()) {
                this.level.playSound((PlayerEntity) null, sourcePos.getX(), sourcePos.getY(), sourcePos.getZ(), SoundEventReg.EFFECT_MISSILE_LAUNCH.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                ICBMReference.logger().printf(Level.INFO, "Launching Missile '%s' from (%s, %s, %s) to (%s, %s, %s) with peak height '%s' and '%s' ticks of flight time.", entity.getName().getString(), sourcePos.getX(), sourcePos.getY(), sourcePos.getZ(), destPos.getX(), destPos.getY(), destPos.getZ(), peakHeight, totalFlightTicks);
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onInventorySlotChanged(int slot, boolean isBeingDestroyed) {
        super.onInventorySlotChanged(slot, isBeingDestroyed);
        itemHandlerLazyOptional.ifPresent((itemHandlerUncast) -> {
            ItemStackHandler itemHandler = (ItemStackHandler)itemHandlerUncast;
            if(level != null && !level.isClientSide()) {

                // Kill Previous Entity (If it Exists)
                Entity previousEntity = ((ServerWorld)level).getEntity(missileEntityID);
                if(previousEntity != null) {
                    previousEntity.kill();
                }
                missileEntityID = null;

                // Spawn New Entity (If Applicable)
                Item item = itemHandler.getStackInSlot(slot).getItem();
                if(item instanceof ItemMissile) {
                    EntityMissile entity = ((ItemMissile)item).getMissileEntity().get().create(level);
                    if(entity != null) {
                        // We need to set sourcePos so that the location of the platform can be derived from the entity (useful when killing the entity and wanting to sync inventory, etc.)
                        // We also need to set missileSourceType for rendering (changes the scale of the model). All other data can wait until launch.
                        entity.updateMissileData(getBlockPos(), null, null, null, getMissileSourceType());
                        entity.addEntityToLevel(
                                new Vector3d(getBlockPos().getX() + 0.5, getBlockPos().getY() + getMissileEntityYOffset(), getBlockPos().getZ() + 0.5),
                                new Vector3d(-90F, 0F, 0F)
                        );
                        missileEntityID = entity.getUUID();
                    }
                }

            }
        });
    }

    @Override
    public boolean isInventoryItemValid(int slot, ItemStack stack) {
        return super.isInventoryItemValid(slot, stack) && (stack.getItem() instanceof ItemMissile);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) {
            return itemHandlerLazyOptional
                    .map(handler -> handler.getStackInSlot(0))
                    .filter(stack -> stack.getItem() instanceof ItemMissile)
                    .map(stack -> (LazyOptional<T>) stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY))
                    .orElse(super.getCapability(cap, side));
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        missileEntityID = null;
        try {
            missileEntityID = UUID.fromString(tag.getString("missileEntity"));
        } catch (Exception ignored) {}
        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.putString("missileEntity", missileEntityID == null ? "Empty" : missileEntityID.toString());
        return super.save(tag);
    }

}