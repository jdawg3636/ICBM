package com.jdawg3636.icbm.common.tile;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileLauncherPlatform extends TileEntity {

    private ItemStackHandler itemHandler = createHandler();
    private LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.of(() -> itemHandler);

    public TileLauncherPlatform(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(1) {

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return ItemTags.getCollection().get(new ResourceLocation(ICBMReference.MODID, "missiles")).contains(stack.getItem());
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if(!isItemValid(slot, stack)) return stack;
                return super.insertItem(slot, stack, simulate);
            }

        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) return itemHandlerLazyOptional.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        itemHandler.deserializeNBT(tag.getCompound("inv"));
        super.read(state, tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("inv", itemHandler.serializeNBT());
        return super.write(tag);
    }

}