package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.common.event.AbstractBlastEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityPrimedExplosives extends TNTEntity {

    public final AbstractBlastEvent.BlastEventProvider blastEventProvider;
    public final RegistryObject<Item> itemForm;
    public Direction blastDirection; // Can't be final - re-assigned by chained constructors

    public EntityPrimedExplosives(EntityType<? extends EntityPrimedExplosives> type, World worldIn, AbstractBlastEvent.BlastEventProvider blastEventProvider, RegistryObject<Item> itemForm, double x, double y, double z, @Nullable LivingEntity igniter) {
        super(type, worldIn);
        this.blastEventProvider = blastEventProvider;
        this.itemForm = itemForm;
        this.setPos(x, y, z);
        double d0 = worldIn.random.nextDouble() * (double)((float)Math.PI * 2F);
        this.setDeltaMovement(-Math.sin(d0) * 0.02D, (double)0.2F, -Math.cos(d0) * 0.02D);
        this.setFuse(80);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = igniter;
        this.blastDirection = (igniter != null) ? igniter.getDirection() : Direction.getRandom(worldIn.random);
    }

    public EntityPrimedExplosives(EntityType<? extends EntityPrimedExplosives> type, World worldIn, AbstractBlastEvent.BlastEventProvider blastEventProvider, RegistryObject<Item> itemForm, double x, double y, double z, @Nullable LivingEntity igniter, Direction blastDirection, int fuse) {
        this(type, worldIn, blastEventProvider, itemForm, x, y, z, igniter);
        if(blastDirection != null) this.blastDirection =  blastDirection;
        this.setFuse(fuse);
    }

    @Override
    protected void explode() {
        if(!level.isClientSide()) {
            AbstractBlastEvent.fire(blastEventProvider, AbstractBlastEvent.Type.EXPLOSIVES, (ServerWorld) level, blockPosition(), blastDirection);
        }
    }

    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return itemForm.get().getDefaultInstance();
    }

}
