package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.common.block.particle_accelerator.TileParticleAccelerator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityAcceleratingParticle extends Entity {

    public BlockPos particleAcceleratorPosition = null;

    public EntityAcceleratingParticle(EntityType<?> entityType, World level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        // If no accelerator is assigned, do nothing.
        if(particleAcceleratorPosition == null) {
            return;
        }
        // Kill particle if an accelerator is assigned but no longer exists
        TileEntity tileEntity = level.getBlockEntity(particleAcceleratorPosition);
        if(!(tileEntity instanceof TileParticleAccelerator)) {
            kill();
            return;
        }
        // Kill particle if assigned accelerator is inactive
        TileParticleAccelerator particleAccelerator = (TileParticleAccelerator)tileEntity;
        if(!particleAccelerator.isActive) {
            kill();
            return;
        }

    }

    @Override
    protected void defineSynchedData() {
        // todo: sync acceleration progress
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT pCompound) {
        // todo
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT pCompound) {
        // todo
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
