package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.entity.EntityLightningVisual;
import com.jdawg3636.icbm.common.reg.EntityReg;
import com.jdawg3636.icbm.common.reg.SoundEventReg;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.ArrayList;
import java.util.Optional;

public class EventBlastEmp extends AbstractBlastEvent {

    public EventBlastEmp(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection, SoundEventReg.EXPLOSION_EMP);
    }

    public static void drainEnergyInALoop(IEnergyStorage energyStorage) {
        final int amountPerLoop = energyStorage.getMaxEnergyStored();
        int lastDrain = Integer.MAX_VALUE;
        for(int i = 0; i < 500 && lastDrain > 0; ++i) {
            lastDrain = energyStorage.extractEnergy(amountPerLoop, false);
        }
    }

    @Override
    public boolean executeBlast() {
        ICBMBlastEventUtil.doBlastSoundAndParticles(this);

        // Early return if the explosion epicenter is inside an explosion-resistant fluid (ex. lava, water)
        if(getBlastWorld().getBlockState(getBlastPosition()).getFluidState().getExplosionResistance() > 0) {
            return true;
        }

        final double radius = ICBMReference.COMMON_CONFIG.getBlastRadiusEMP();
        AxisAlignedBB aabb = new AxisAlignedBB(getBlastPosition()).inflate(radius);
        new ArrayList<>(getBlastWorld().blockEntityList).stream()
                .filter(tileEntity -> tileEntity != null && aabb.contains(Vector3d.atCenterOf(tileEntity.getBlockPos())))
                .forEach(tileEntity -> {
                    boolean anySideImplementsForgeEnergyAPI = false;
                    boolean anySideCanExtract = false;
                    boolean anySideCanReceive = false;
                    // Try to play by the rules first - use API to extract all energy
                    for (Direction direction : Direction.values()) {
                        IEnergyStorage energyStorage = tileEntity.getCapability(ICBMReference.FORGE_ENERGY_CAPABILITY, direction).orElse(null);
                        if(energyStorage != null) {
                            anySideImplementsForgeEnergyAPI = true;
                            anySideCanExtract = anySideCanExtract || energyStorage.canExtract();
                            anySideCanReceive = anySideCanReceive || energyStorage.canReceive();
                            drainEnergyInALoop(energyStorage);
                        }
                        // Drain energy from the items too
                        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
                            for(int i = 0; i < itemHandler.getSlots(); ++i) {
                                itemHandler.getStackInSlot(i).getCapability(ICBMReference.FORGE_ENERGY_CAPABILITY).ifPresent(itemEnergyStorage -> {
                                    drainEnergyInALoop(itemEnergyStorage);
                                });
                            }
                        });
                    }
                    // API isn't good enough - loop back through, bypass API using an access transformer (only works for built-in Forge implementation), and drain any remaining energy that's not supposed to be extractable
                    for (Direction direction : Direction.values()) {
                        EnergyStorage rawEnergyStorage = tileEntity.getCapability(ICBMReference.FORGE_ENERGY_CAPABILITY, direction).filter(EnergyStorage.class::isInstance).map(EnergyStorage.class::cast).orElse(null);
                        if(rawEnergyStorage != null && rawEnergyStorage.energy > 0) {
                            rawEnergyStorage.energy = 0;
                            // If we changed anything, send an update packet to the client.
                            SUpdateTileEntityPacket updatePacket = tileEntity.getUpdatePacket();
                            if (updatePacket != null && getBlastWorld().getServer() != null) {
                                getBlastWorld().getServer().getPlayerList().broadcast(null, tileEntity.getBlockPos().getX(), tileEntity.getBlockPos().getY(), tileEntity.getBlockPos().getZ(), ICBMReference.getTileEntityUpdateDistance(), getBlastWorld().dimension(), updatePacket);
                            }
                        }
                    }
                    // Back to playing by the rules
                    // If something is a generator (either by behavior or by registry name), destroy and drop as an item.
                    boolean isGeneratorByBehavior = anySideCanExtract && !anySideCanReceive;
                    boolean isGeneratorByRegistryName = Optional.of(tileEntity).map(TileEntity::getBlockState).map(BlockState::getBlock).map(Block::getRegistryName).map(regName -> regName.getPath().contains("generator")).orElse(false);
                    boolean isGenerator = anySideImplementsForgeEnergyAPI && (isGeneratorByBehavior || isGeneratorByRegistryName);
                    if(isGenerator) ICBMBlastEventUtil.dropLootForBlockPos(getBlastWorld(), tileEntity.getBlockPos(), getBlastSource(), true);
                    // Summon visual lightning
                    EntityLightningVisual entityLightningVisual = EntityReg.LIGHTNING_VISUAL.get().create(getBlastWorld());
                    if(entityLightningVisual != null) {
                        entityLightningVisual.setPos(getBlastPosition().getX() + 0.5, getBlastPosition().getY() + 0.5, getBlastPosition().getZ() + 0.5);
                        entityLightningVisual.updateRotation(tileEntity.getBlockPos().getX() + 0.5, tileEntity.getBlockPos().getY() + 0.5, tileEntity.getBlockPos().getZ() + 0.5);
                        getBlastWorld().addFreshEntity(entityLightningVisual);
                    }
                });
        return true;
    }

}
