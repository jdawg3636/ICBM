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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.Level;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TileLauncherPlatform extends TileMachine {

    public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("gui.icbm.launcher_platform");
    public UUID missileEntityID = null;

    public TileLauncherPlatform(TileEntityType<?> tileEntityTypeIn, Supplier<ContainerType<? extends AbstractContainerMachine>> containerType) {
        super(tileEntityTypeIn, containerType, 1, 0, 0, 0, DEFAULT_NAME);
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

    public void removeMissileItemWithAction(Consumer<EntityMissile> action) {
        itemHandlerLazyOptional.ifPresent((itemHandlerUncast) -> {
            ItemStackHandler itemHandler = (ItemStackHandler)itemHandlerUncast;
            if(missileEntityID != null && level != null && !level.isClientSide()) {
                Item item = itemHandler.getStackInSlot(0).getItem();
                EntityMissile entity = (EntityMissile)(((ServerWorld)level).getEntity(missileEntityID));
                if(item instanceof ItemMissile) {
                    missileEntityID = null; // Necessary to disconnect, otherwise ItemStackHandler would kill the entity when the slot is cleared
                    itemHandler.setStackInSlot(0, ItemStack.EMPTY);
                    action.accept(entity);
                }
            }
        });
    }

    public void launchMissile(BlockPos sourcePos, BlockPos destPos, float peakHeight, int totalFlightTicks) {
        removeMissileItemWithAction((entity) -> {
            assert level != null;
            if(entity != null) {
                entity.updateMissileData(sourcePos, destPos, peakHeight, totalFlightTicks, getMissileSourceType());
                entity.launchMissile();
                this.level.playSound((PlayerEntity) null, sourcePos.getX(), sourcePos.getY(), sourcePos.getZ(), SoundEventReg.EFFECT_MISSILE_LAUNCH.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                ICBMReference.logger().printf(Level.INFO, "Launching Missile '%s' from (%s, %s, %s) to (%s, %s, %s) with peak height '%s' and '%s' ticks of flight time.", entity.getName().getString(), sourcePos.getX(), sourcePos.getY(), sourcePos.getZ(), destPos.getX(), destPos.getY(), destPos.getZ(), peakHeight, totalFlightTicks);
            }
        });
    }

    @Override
    protected void onInventorySlotChanged(int slot) {
        super.onInventorySlotChanged(slot);
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