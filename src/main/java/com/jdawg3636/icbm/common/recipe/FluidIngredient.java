package com.jdawg3636.icbm.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;
import java.util.function.Predicate;

public class FluidIngredient implements Predicate<FluidStack> {

    public static enum Type {
        FLUID,
        TAG
    }

    public Type ingredientType;
    public ResourceLocation resourceLocation;
    public int amount;

    public FluidIngredient(Type ingredientType, ResourceLocation resourceLocation, int amount) {
        this.ingredientType = ingredientType;
        this.resourceLocation = resourceLocation;
        this.amount = amount;
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        if(fluidStack.getAmount() < this.amount) return false;
        if(this.ingredientType == Type.TAG) {
            return fluidStack.getFluid().is(FluidTags.createOptional(resourceLocation));
        }
        return fluidStack.getFluid().equals(ForgeRegistries.FLUIDS.getValue(this.resourceLocation));
    }

    public Optional<FluidStack> getAsFluidStack() {
        if(this.ingredientType != Type.FLUID) {
            return Optional.empty();
        }
        return Optional.of(new FluidStack(ForgeRegistries.FLUIDS.getValue(this.resourceLocation), this.amount));
    }

    public static FluidIngredient parseFromJson(JsonObject json) {

        Type ingredientType;
        ResourceLocation resourceLocation;
        int amount;

        if (json.has("tag")) {
            ingredientType = Type.TAG;
            resourceLocation = new ResourceLocation(json.get("tag").getAsString());
        } else if (json.has("fluid")) {
            ingredientType = Type.FLUID;
            resourceLocation = new ResourceLocation(json.get("fluid").getAsString());
        } else {
            throw new IllegalArgumentException("Ingredient must have either a 'tag' or 'fluid' key");
        }

        if (json.has("amount")) {
            amount = json.get("amount").getAsInt();
        } else {
            amount = 1;
        }

        return new FluidIngredient(ingredientType, resourceLocation, amount);

    }

    public void toNetwork(PacketBuffer buffer) {
        buffer.writeInt(this.ingredientType.ordinal());
        buffer.writeResourceLocation(this.resourceLocation);
        buffer.writeInt(this.amount);
    }

    public static FluidIngredient fromNetwork(PacketBuffer buffer) {
        Type ingredientType = Type.values()[buffer.readInt()];
        ResourceLocation resourceLocation = buffer.readResourceLocation();
        int amount = buffer.readInt();
        return new FluidIngredient(ingredientType, resourceLocation, amount);
    }

}
