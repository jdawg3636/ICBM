package com.jdawg3636.icbm.common.reg;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.recipe.ParticleAcceleratorRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class ICBMRecipeTypes {

    public static IRecipeType<ParticleAcceleratorRecipe> PARTICLE_ACCELERATOR;
    public static IRecipeType<ParticleAcceleratorRecipe> REFINERY;

    public static void registerAll() {
        PARTICLE_ACCELERATOR = register("particle_accelerator");
        REFINERY = register("refinery");
    }

    private static <T extends IRecipe<?>> IRecipeType<T> register(final String identifier) {
        return (IRecipeType<T>) Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(ICBMReference.MODID, identifier), new IRecipeType<T>() {
            public String toString() {
                return identifier;
            }
        });
    }

}
