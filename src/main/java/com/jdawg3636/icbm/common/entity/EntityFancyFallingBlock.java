package com.jdawg3636.icbm.common.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class EntityFancyFallingBlock extends FallingBlockEntity {

    public EntityFancyFallingBlock(EntityType<? extends FallingBlockEntity> entityType, World level) {
        super(entityType, level);
    }

    public EntityFancyFallingBlock(World level, double x, double y, double z, BlockState blockState) {
        this(EntityType.FALLING_BLOCK, level);
        this.blockState = blockState;
        this.blocksBuilding = true;
        this.setPos(x, y + (double)((1.0F - this.getBbHeight()) / 2.0F), z);
        this.setDeltaMovement(Vector3d.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.setStartPos(this.blockPosition());
    }

    @Override
    public boolean isNoGravity() {
        // This prevents the vanilla code from applying gravity and allows us to handle it ourselves.
        return true;
    }

    @Override
    public void tick() {
        // Emulate vanilla gravity behavior
        if (!this.getBlockState().isAir() && this.time + 1 != 0 && !this.level.getBlockState(this.blockPosition()).is(this.getBlockState().getBlock()) && !this.level.isClientSide && !this.entityData.get(DATA_NO_GRAVITY)) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        }
        // Call super
        super.tick();
    }

}
