package com.jdawg3636.icbm.common.reg;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.recipe.ParticleAcceleratorRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeSerializerReg {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ICBMReference.MODID);
    public static final RegistryObject<IRecipeSerializer<?>> PARTICLE_ACCELERATOR = RECIPE_SERIALIZERS.register("particle_accelerator", ParticleAcceleratorRecipe.Serializer::new);
}
