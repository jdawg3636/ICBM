package com.jdawg3636.icbm.mixin.accessors;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(PlayerInventory.class)
public interface IPlayerInventoryAccessor {
    @Accessor("compartments")
    List<NonNullList<ItemStack>> getCompartments();
}
