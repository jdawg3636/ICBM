package com.jdawg3636.icbm.common.recipe;

import com.google.gson.JsonObject;
import com.jdawg3636.icbm.common.reg.ICBMRecipeTypes;
import com.jdawg3636.icbm.common.reg.RecipeSerializerReg;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Optional;

public class RefineryRecipe implements IRecipe<IInventory> {

    private final ResourceLocation recipeId;
    private FluidIngredient ingredient;
    private FluidStack result;
    private int processingTime;

    public RefineryRecipe(ResourceLocation recipeId, FluidIngredient ingredient, FluidStack result, int processingTime) {
        this.recipeId = recipeId;
        this.ingredient = ingredient;
        this.result = result;
        this.processingTime = processingTime;
    }

    public static Optional<RefineryRecipe> getRecipeFor(IRecipeType<RefineryRecipe> recipeType, FluidStack fluidStack, World level) {
        return level.getRecipeManager().byType(recipeType).values().stream()
                .filter(RefineryRecipe.class::isInstance).map(RefineryRecipe.class::cast)
                .filter(recipe -> recipe.matchesCustom(fluidStack))
                .findFirst();
    }

    @Deprecated
    @Override
    public boolean matches(IInventory inventory, World world) {
        // Won't work for fluids - use this::matchesCustom
        return false;
    }

    public boolean matchesCustom(FluidStack input) {
        return this.ingredient.test(input);
    }

    public FluidIngredient getIngredient() {
        return this.ingredient;
    }

    public FluidStack getResult() {
        return this.result.copy();
    }

    public int getProcessingTime() {
        return this.processingTime;
    }

    @Deprecated
    @Override
    public ItemStack assemble(IInventory inventory) {
        return Items.AIR.getDefaultInstance();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return result.getFluid().getBucket().getDefaultInstance();
    }

    @Override
    public ResourceLocation getId() {
        return recipeId;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializerReg.REFINERY.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ICBMRecipeTypes.REFINERY;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RefineryRecipe>  {

        @Override
        public RefineryRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            if(json.has("ingredient") && json.has("result")) {
                FluidIngredient ingredient = FluidIngredient.parseFromJson(json.get("ingredient").getAsJsonObject());
                FluidStack result = FluidIngredient.parseFromJson(json.get("result").getAsJsonObject()).getAsFluidStack().orElse(FluidStack.EMPTY);
                int processingTime = json.get("processing_time").getAsInt();
                return new RefineryRecipe(recipeId, ingredient, result, processingTime);
            }
            throw new IllegalArgumentException("Recipe must contain both an 'ingredient' and a 'result' key");
        }

        @Override
        public RefineryRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            FluidIngredient ingredient = FluidIngredient.fromNetwork(buffer);
            Fluid resultFluid = ForgeRegistries.FLUIDS.getValue(buffer.readResourceLocation());
            int resultAmount = buffer.readInt();
            int processingTime = buffer.readInt();
            return new RefineryRecipe(recipeId, ingredient, new FluidStack(resultFluid, resultAmount), processingTime);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, RefineryRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeResourceLocation(recipe.result.getFluid().getRegistryName());
            buffer.writeInt(recipe.result.getAmount());
            buffer.writeInt(recipe.processingTime);
        }

    }

}