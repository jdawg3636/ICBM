package com.jdawg3636.icbm.common.recipe;

import com.google.gson.JsonObject;
import com.jdawg3636.icbm.common.reg.ICBMRecipeTypes;
import com.jdawg3636.icbm.common.reg.RecipeSerializerReg;
import com.jdawg3636.icbm.mixin.accessors.IRecipeManagerAccessor;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public class MetallurgicLeachingRecipe implements IProcessingRecipe {
    private final ResourceLocation recipeId;
    private final FluidIngredient inputFluid;
    private final Ingredient inputItem;
    private Supplier<ItemStack> result;
    private int processingTime;

    public MetallurgicLeachingRecipe(ResourceLocation recipeId, FluidIngredient inputFluid, Ingredient inputItem, Supplier<ItemStack> result, int processingTime) {
        this.recipeId = recipeId;
        this.inputFluid = inputFluid;
        this.inputItem = inputItem;
        this.result = result;
        this.processingTime = processingTime;
    }

    public static Optional<MetallurgicLeachingRecipe> getRecipeFor(IRecipeType<MetallurgicLeachingRecipe> recipeType, FluidStack inputFluid, ItemStack inputItem, World level) {
        return ((IRecipeManagerAccessor)level.getRecipeManager()).invokeByType(recipeType).values().stream()
                .filter(MetallurgicLeachingRecipe.class::isInstance).map(MetallurgicLeachingRecipe.class::cast)
                .filter(recipe -> recipe.matchesCustom(inputFluid, inputItem))
                .findFirst();
    }

    @Deprecated
    @Override
    public boolean matches(IInventory inventory, World world) {
        // Would normally use something like this: Optional<ICraftingRecipe> optional = pLevel.getServer().getRecipeManager().getRecipeFor(IRecipeType.CRAFTING, pContainer, pLevel);
        // Won't work since the recipe input isn't an inventory. Need to instead use: Optional<MetallurgicLeachingRecipe> optional = MetallurgicLeachingRecipe.getRecipeFor(ICBMRecipeTypes.METALLURGIC_LEACHING, explosionCause, level);
        return false;
    }

    public boolean matchesCustom(FluidStack inputFluid, ItemStack inputItem) {
        return this.inputFluid.test(inputFluid) && this.inputItem.test(inputItem);
    }

    @Override
    public ItemStack assemble(IInventory inventory) {
        return result.get();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return result.get();
    }

    @Override
    public ResourceLocation getId() {
        return recipeId;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializerReg.METALLURGIC_LEACHING.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ICBMRecipeTypes.METALLURGIC_LEACHING;
    }

    public FluidIngredient getInputFluid() {
        return this.inputFluid;
    }

    public Ingredient getInputItem() {
        return this.inputItem;
    }

    @Override
    public int getProcessingTime() {
        return this.processingTime;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<MetallurgicLeachingRecipe> {

        @Override
        public MetallurgicLeachingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            FluidIngredient inputFluid = FluidIngredient.parseFromJson(json.get("input_fluid").getAsJsonObject());
            Ingredient inputItem = Ingredient.fromJson(json.get("input_item").getAsJsonObject());
            ItemStack result = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
            int processingTime = json.get("processing_time").getAsInt();
            return new MetallurgicLeachingRecipe(recipeId, inputFluid, inputItem, result::copy, processingTime);
        }

        @Nullable
        @Override
        public MetallurgicLeachingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            FluidIngredient inputFluid = FluidIngredient.fromNetwork(buffer);
            Ingredient inputItem = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            int processingTime = buffer.readInt();
            return new MetallurgicLeachingRecipe(recipeId, inputFluid, inputItem, result::copy, processingTime);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, MetallurgicLeachingRecipe recipe) {
            recipe.inputFluid.toNetwork(buffer);
            recipe.inputItem.toNetwork(buffer);
            buffer.writeItem(recipe.result.get());
            buffer.writeInt(recipe.processingTime);
        }

    }

}
