package com.jdawg3636.icbm.common.block.launcher_platform;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.machine.TileMachine;
import com.jdawg3636.icbm.common.entity.EntityMissile;
import com.jdawg3636.icbm.common.item.ItemMissile;
import com.jdawg3636.icbm.common.reg.SoundEventReg;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.Level;

import java.util.UUID;

public class TileLauncherPlatform extends TileMachine {

    public UUID missileEntityID = null;

    public TileLauncherPlatform(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn, 1);
    }

    public double getMissileEntityYOffset() {
        return 5D/16D;
    }

    public boolean usesControlPanel() {
        return true;
    }

    public EntityMissile.MissileSourceType getMissileSourceType() {
        return EntityMissile.MissileSourceType.LAUNCHER_PLATFORM;
    }

    public double getYawRadians() {
        return 0D;
    }

    public double getPitchRadians() {
        return 0D;
    }

    public void launchMissile(BlockPos sourcePos, BlockPos destPos, float peakHeight, int totalFlightTicks) {
        if(missileEntityID != null && itemHandler != null && level != null && !level.isClientSide()) {
            Item item = itemHandler.getStackInSlot(0).getItem();
            EntityMissile entity = (EntityMissile)(((ServerWorld)level).getEntity(missileEntityID));
            if(item instanceof ItemMissile && entity != null) {

                missileEntityID = null; // Necessary to disconnect, otherwise ItemStackHandler would kill the entity when the slot is cleared
                itemHandler.setStackInSlot(0, ItemStack.EMPTY);

                entity.updateMissileData(sourcePos, destPos, peakHeight, totalFlightTicks, getMissileSourceType(), EntityMissile.MissileLaunchPhase.LAUNCHED);

                this.level.playSound((PlayerEntity) null, sourcePos.getX(), sourcePos.getY(), sourcePos.getZ(), SoundEventReg.EFFECT_MISSILE_LAUNCH.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);

                ICBMReference.logger().printf(Level.INFO, "Launching Missile '%s' from (%s, %s, %s) to (%s, %s, %s) with peak height '%s' and '%s' ticks of flight time.", entity.getName().getString(), sourcePos.getX(), sourcePos.getY(), sourcePos.getZ(), destPos.getX(), destPos.getY(), destPos.getZ(), peakHeight, totalFlightTicks);

            }
        }
    }

    @Override
    protected void onInventorySlotChanged(int slot) {
        super.onInventorySlotChanged(slot);
        assert itemHandler != null;
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
                    entity.setRot(0, -90F);
                    entity.setPos(getBlockPos().getX() + 0.5, getBlockPos().getY() + getMissileEntityYOffset(), getBlockPos().getZ() + 0.5);
                    entity.updateMissileData(null, null, null, null, getMissileSourceType(), null);
                    level.addFreshEntity(entity);
                    missileEntityID = entity.getUUID();
                }
            }

        }
    }

    @Override
    public boolean isInventoryItemValid(int slot, ItemStack stack) {
        return stack.getItem() instanceof ItemMissile;
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