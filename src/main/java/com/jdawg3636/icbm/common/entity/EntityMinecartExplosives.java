package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.common.block.BlockExplosives;
import com.jdawg3636.icbm.common.event.AbstractBlastEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.TNTMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class EntityMinecartExplosives extends TNTMinecartEntity {

    public final RegistryObject<Block> blockForm;
    public final RegistryObject<Item> itemForm;

    public EntityMinecartExplosives(EntityType<? extends TNTMinecartEntity> entityType, World level, RegistryObject<Block> blockForm, RegistryObject<Item> itemForm) {
        super(entityType, level);
        this.blockForm = blockForm;
        this.itemForm = itemForm;
    }

    @Override
    protected void explode(double ignored) {
        if (!this.level.isClientSide) {
            if(blockForm.get() instanceof BlockExplosives) {
                AbstractBlastEvent.fire(((BlockExplosives)blockForm.get()).blastEventProvider, AbstractBlastEvent.Type.EXPLOSIVES_MINECART, (ServerWorld) level, blockPosition(), getDirection());
            }
            this.remove();
        }
    }

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return blockForm.get().defaultBlockState();
    }

    @Override
    public ItemStack getCartItem() {
        return itemForm.get().getDefaultInstance();
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return itemForm.get().getDefaultInstance();
    }

    @Override
    // Copied from vanilla - overridden to change the item dropped. Also ewww why isn't that in a loot table?
    public void destroy(DamageSource pSource) {
        double d0 = getHorizontalDistanceSqr(this.getDeltaMovement());
        if (!pSource.isFire() && !pSource.isExplosion() && !(d0 >= (double)0.01F)) {
            superDestroy(pSource);
            if (!pSource.isExplosion() && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                this.spawnAtLocation(itemForm.get());
            }
        } else {
            if (this.fuse < 0) {
                this.primeFuse();
                this.fuse = this.random.nextInt(20) + this.random.nextInt(20);
            }
        }
    }

    // Copied from AbstractMinecartEntity - need to duplicate in order to bypass override in TNTMinecartEntity
    public void superDestroy(DamageSource pSource) {
        this.remove();
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            ItemStack itemstack = new ItemStack(Items.MINECART);
            if (this.hasCustomName()) {
                itemstack.setHoverName(this.getCustomName());
            }

            this.spawnAtLocation(itemstack);
        }
    }

    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
