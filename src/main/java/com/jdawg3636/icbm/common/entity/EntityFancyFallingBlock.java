package com.jdawg3636.icbm.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.UUID;

public class EntityFancyFallingBlock extends FallingBlockEntity implements IEntityAdditionalSpawnData {

    public static final DataParameter<Float> PER_TICK_GRAVITY = EntityDataManager.defineId(EntityFancyFallingBlock.class, DataSerializers.FLOAT);

    // These don't need synced - client-side only and derived from UUID
    public float xRotDelta;
    public float yRotDelta;

    public EntityFancyFallingBlock(EntityType<? extends EntityFancyFallingBlock> entityType, World level) {
        super(entityType, level);
        this.time = Integer.MIN_VALUE;
        this.dropItem = false;
        this.blocksBuilding = true;
        setUUID(this.uuid);
    }

    public void addEntityToLevel(BlockPos initialPosition, BlockState blockState, float perTickGravity) {
        getEntityData().set(PER_TICK_GRAVITY, perTickGravity);
        this.blockState = blockState;
        double x = initialPosition.getX() + 0.5;
        double y = initialPosition.getY();
        double z = initialPosition.getZ() + 0.5;
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.setPos(x, y, z);
        this.setStartPos(this.blockPosition());
        this.level.addFreshEntity(this);
    }

    @Override
    public void setUUID(UUID uuid) {
        // Call super
        super.setUUID(uuid);
        // Update UUID-derived per-tick rotation deltas
        this.xRotDelta = (this.uuid.getLeastSignificantBits() % 2 == 0 ? 1 : -1) * this.uuid.getLeastSignificantBits() / (float)Long.MAX_VALUE;
        this.yRotDelta = (this.uuid.getMostSignificantBits() % 2 == 0 ? 1 : -1) * this.uuid.getMostSignificantBits() / (float)Long.MAX_VALUE;
    }

    @Override
    public boolean isNoGravity() {
        // This prevents the vanilla code from applying gravity and allows us to handle it ourselves.
        return true;
    }

    @Override
    public void tick() {
        // Update visual rotation
        if(level.isClientSide()) {
            this.setRot(this.yRot + yRotDelta, this.xRot + xRotDelta);
        }
        // If levitating (inverted gravity), solidify to block when time runs out (vanilla logic will only solidify if we hit the ground)
        if(!level.isClientSide() && getEntityData().get(PER_TICK_GRAVITY) > 0 && this.time + 1 >= 0) {
            this.time = -5 * 20;
            getEntityData().set(PER_TICK_GRAVITY, -0.04f);
        }
        // Apply Gravity
        if(!this.entityData.get(DATA_NO_GRAVITY)) {
            final float perTickGravity = getEntityData().get(PER_TICK_GRAVITY);
            // If positive, rise at a fixed rate
            if(perTickGravity > 0) {
                this.setDeltaMovement(new Vector3d(getDeltaMovement().x, perTickGravity, getDeltaMovement().z));
            }
            // If negative, emulate vanilla gravity behavior
            else {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, getEntityData().get(PER_TICK_GRAVITY), 0.0));
            }
        }
        // Call super
        super.tick();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(PER_TICK_GRAVITY, -0.04f);
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putFloat("per_tick_gravity", getEntityData().get(PER_TICK_GRAVITY));
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        getEntityData().set(PER_TICK_GRAVITY, nbt.getFloat("per_tick_gravity"));
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeInt(Block.getId(this.getBlockState()));
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        this.blockState = Block.stateById(additionalData.readInt());
    }

}
