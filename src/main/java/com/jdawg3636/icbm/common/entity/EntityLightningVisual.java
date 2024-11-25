package com.jdawg3636.icbm.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityLightningVisual extends Entity {

    public long seed = this.random.nextLong();
    public int remainingTicks = 40;

    private static final DataParameter<Float> VISUAL_YAW = EntityDataManager.defineId(EntityLightningVisual.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> VISUAL_PITCH = EntityDataManager.defineId(EntityLightningVisual.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> NUM_SEGMENTS = EntityDataManager.defineId(EntityLightningVisual.class, DataSerializers.INT);
    private static final DataParameter<Float> SCALE = EntityDataManager.defineId(EntityLightningVisual.class, DataSerializers.FLOAT);

    public EntityLightningVisual(EntityType<?> entityType, World level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level instanceof ServerWorld) {
            if(this.remainingTicks == 0) {
                this.kill();
            }
            this.remainingTicks -= 1;
        } else if(tickCount % 10 == 0) {
            this.level.setSkyFlashTime(5);
            this.seed = this.random.nextLong();
        }
    }

    public void updateRotation(double targetX, double targetY, double targetZ) {

        // Compute Constants
        final double x = blockPosition().getX() + 0.5;
        final double y = blockPosition().getY() + 0.5;
        final double z = blockPosition().getZ() + 0.5;
        final double deltaX = targetX - x;
        final double deltaY = targetY - y;
        final double deltaZ = targetZ - z;
        final double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        final double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

        // Compute Angle
        double yawRadians = Math.atan2(deltaX, deltaZ);
        double pitchRadians = Math.atan2(horizontalDistance, deltaY);
        if(yawRadians < Math.PI) yawRadians += 2 * Math.PI;

        // Compute Segment Count
        int numSegments = (int)Math.ceil(distance / 16);
        float scale = (float)(distance / (numSegments * 16));

        // Assign Result
        getEntityData().set(VISUAL_YAW, (float)yawRadians);
        getEntityData().set(VISUAL_PITCH, (float)pitchRadians);
        getEntityData().set(NUM_SEGMENTS, numSegments);
        getEntityData().set(SCALE, scale);

    }

    public float getYawRadians() {
        return getEntityData().get(VISUAL_YAW);
    }

    public float getPitchRadians() {
        return getEntityData().get(VISUAL_PITCH);
    }

    public int getSegmentCount() {
        return Math.max(2, getEntityData().get(NUM_SEGMENTS));
    }

    public float getScale() {
        return getEntityData().get(SCALE);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(VISUAL_YAW, 0F);
        entityData.define(VISUAL_PITCH, 0F);
        entityData.define(NUM_SEGMENTS, 2);
        entityData.define(SCALE, 1F);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        CompoundNBT nbtCustomVisual = nbt.getCompound("custom_visual");
        getEntityData().set(VISUAL_YAW, nbtCustomVisual.getFloat("yaw"));
        getEntityData().set(VISUAL_PITCH, nbtCustomVisual.getFloat("pitch"));
        getEntityData().set(NUM_SEGMENTS, nbtCustomVisual.getInt("segment_count"));
        getEntityData().set(SCALE, nbtCustomVisual.getFloat("scale"));
        this.remainingTicks = nbtCustomVisual.getInt("remaining_ticks");
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        CompoundNBT nbtCustomVisual = new CompoundNBT();
        nbtCustomVisual.putFloat("yaw", getEntityData().get(VISUAL_YAW));
        nbtCustomVisual.putFloat("pitch", getEntityData().get(VISUAL_PITCH));
        nbtCustomVisual.putInt("segment_count", getEntityData().get(NUM_SEGMENTS));
        nbtCustomVisual.putFloat("scale", getEntityData().get(SCALE));
        nbtCustomVisual.putInt("remaining_ticks", this.remainingTicks);
        nbt.put("custom_visual", nbtCustomVisual);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getBoundingBoxForCulling() {
        // Disables frustum culling - not worth calculating
        return AxisAlignedBB.ofSize(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderAtSqrDistance(double sqrDistance) {
        // Disables distance culling
        return true;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
