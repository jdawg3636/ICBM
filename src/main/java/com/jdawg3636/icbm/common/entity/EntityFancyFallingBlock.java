package com.jdawg3636.icbm.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityFancyFallingBlock extends FallingBlockEntity implements IEntityAdditionalSpawnData {

    public EntityFancyFallingBlock(EntityType<? extends EntityFancyFallingBlock> entityType, World level) {
        super(entityType, level);
        this.time = Integer.MIN_VALUE;
        this.dropItem = false;
        this.blocksBuilding = true;
    }

    public void addEntityToLevel(BlockPos initialPosition, BlockState blockState) {
        this.blockState = blockState;
        double x = initialPosition.getX() + 0.5;
        double y = initialPosition.getY() + (double)((1.0F - this.getBbHeight()) / 2.0F);
        double z = initialPosition.getZ() + 0.5;
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.setPos(x, y, z);
        this.setStartPos(this.blockPosition());
        this.level.addFreshEntity(this);
    }

    @Override
    public boolean isNoGravity() {
        // This prevents the vanilla code from applying gravity and allows us to handle it ourselves.
        return true;
    }

    @Override
    public void tick() {
        if(level.isClientSide()) {
            float xRotDelta = (this.uuid.getLeastSignificantBits() % 2 == 0 ? 1 : -1) * this.uuid.getLeastSignificantBits() / (float)Long.MAX_VALUE;
            float yRotDelta = (this.uuid.getMostSignificantBits() % 2 == 0 ? 1 : -1) * this.uuid.getMostSignificantBits() / (float)Long.MAX_VALUE;
            this.setRot(this.yRot + xRotDelta, this.xRot + yRotDelta);
        }
        // Emulate vanilla gravity behavior
        if(!this.entityData.get(DATA_NO_GRAVITY)) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
        }
        // Call super
        super.tick();
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
