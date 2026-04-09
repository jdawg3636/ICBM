package com.jdawg3636.icbm.mixin.accessors;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(RecipeManager.class)
public interface IRecipeManagerAccessor {
    @Invoker("byType")
    <C extends IInventory, T extends IRecipe<C>> Map<ResourceLocation, IRecipe<C>> invokeByType(IRecipeType<T> pRecipeType);
}
