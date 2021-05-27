package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.common.event.BlastEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityPrimedExplosives extends TNTEntity {

    BlastEvent.BlastEventProvider blastEventProvider;
    RegistryObject<Item> itemForm;

    public EntityPrimedExplosives(EntityType<? extends EntityPrimedExplosives> type, World worldIn, BlastEvent.BlastEventProvider blastEventProvider, RegistryObject<Item> itemForm, double x, double y, double z, @Nullable LivingEntity igniter) {
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
    }

    @Override
    protected void explode() {
        if(!getCommandSenderWorld().isClientSide())
            MinecraftForge.EVENT_BUS.post(
                    blastEventProvider.getBlastEvent(blockPosition(), (ServerWorld) getCommandSenderWorld(), false)
            );
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
