package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.common.event.AbstractBlastEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkHooks;

@OnlyIn(
        value = Dist.CLIENT,
        _interface = IRendersAsItem.class
)
public class EntityGrenade extends ThrowableEntity implements IRendersAsItem {

    public AbstractBlastEvent.BlastEventProvider blastEventProvider;
    public RegistryObject<Item> itemForm;
    public int fuse = 40;

    public EntityGrenade(EntityType<? extends EntityGrenade> type, World worldIn, AbstractBlastEvent.BlastEventProvider blastEventProvider, RegistryObject<Item> itemForm) {
        super(type, worldIn);
        this.blastEventProvider = blastEventProvider;
        this.itemForm = itemForm;
    }

    @Override
    public void tick() {

        if (!isNoGravity()) {
            this.setDeltaMovement(getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        }

        move(MoverType.SELF, getDeltaMovement());
        this.setDeltaMovement(getDeltaMovement().scale(0.98D));
        if (onGround) {
            this.setDeltaMovement(getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
        }
        --fuse;
        if (fuse <= 0) {
            remove();
            if(!level.isClientSide()) {
                AbstractBlastEvent.fire(blastEventProvider, AbstractBlastEvent.Type.GRENADE, (ServerWorld) level, blockPosition(), getDirection());
            }
        } else {
            updateInWaterStateAndDoFluidPushing();
            if (level.isClientSide) {
                level.addParticle(ParticleTypes.SMOKE, getX(), getY() + 0.5D, getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
        compound.putInt("Fuse", fuse);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {
        fuse = compound.getInt("Fuse");
    }

    @Override
    public ItemStack getItem() {
        return getPickedResult(null);
    }

    @Override
    protected void defineSynchedData() {
        // NOP
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return itemForm.get().getDefaultInstance();
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
