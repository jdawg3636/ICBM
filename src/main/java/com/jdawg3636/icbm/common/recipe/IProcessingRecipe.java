package com.jdawg3636.icbm.common.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;

public interface IProcessingRecipe extends IRecipe<IInventory> {

    int getProcessingTime();

}
