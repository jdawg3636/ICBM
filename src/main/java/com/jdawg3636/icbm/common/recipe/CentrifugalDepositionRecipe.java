package com.jdawg3636.icbm.common.recipe;

import com.google.gson.JsonObject;
import com.jdawg3636.icbm.common.reg.ICBMRecipeTypes;
import com.jdawg3636.icbm.common.reg.RecipeSerializerReg;
import com.jdawg3636.icbm.mixin.accessors.IRecipeManagerAccessor;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
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

public class CentrifugalDepositionRecipe implements IProcessingRecipe {

    private final ResourceLocation recipeId;
    private final FluidIngredient inputFluid;
    private final Supplier<ItemStack> resultA;
    private final Supplier<ItemStack> resultB;
    private final int processingTime;

    public CentrifugalDepositionRecipe(ResourceLocation recipeId, FluidIngredient inputFluid, Supplier<ItemStack> resultA, Supplier<ItemStack> resultB, int processingTime) {
        this.recipeId = recipeId;
        this.inputFluid = inputFluid;
        this.resultA = resultA;
        this.resultB = resultB;
        this.processingTime = processingTime;
    }

    public static Optional<CentrifugalDepositionRecipe> getRecipeFor(IRecipeType<CentrifugalDepositionRecipe> recipeType, FluidStack inputFluid, World level) {
        return ((IRecipeManagerAccessor)level.getRecipeManager()).invokeByType(recipeType).values().stream()
                .filter(CentrifugalDepositionRecipe.class::isInstance).map(CentrifugalDepositionRecipe.class::cast)
                .filter(recipe -> recipe.matchesCustom(inputFluid))
                .findFirst();
    }

    @Deprecated
    @Override
    public boolean matches(IInventory inventory, World world) {
        // Won't work for fluids - use this::matchesCustom
        return false;
    }

    public boolean matchesCustom(FluidStack inputFluid) {
        return this.inputFluid.test(inputFluid);
    }

    public FluidIngredient getInputFluid() {
        return this.inputFluid;
    }

    public ItemStack getResultA() {
        return this.resultA.get();
    }

    public ItemStack getResultB() {
        return this.resultB.get();
    }

    @Override
    public int getProcessingTime() {
        return this.processingTime;
    }

    @Override
    public ItemStack assemble(IInventory inventory) {
        return resultA.get();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return resultA.get();
    }

    @Override
    public ResourceLocation getId() {
        return recipeId;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializerReg.CENTRIFUGAL_DEPOSITION.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ICBMRecipeTypes.CENTRIFUGAL_DEPOSITION;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CentrifugalDepositionRecipe> {

        @Override
        public CentrifugalDepositionRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            FluidIngredient inputFluid = FluidIngredient.parseFromJson(json.get("input_fluid").getAsJsonObject());
            ItemStack resultA = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result_a"));
            ItemStack resultB = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result_b"));
            int processingTime = json.get("processing_time").getAsInt();
            return new CentrifugalDepositionRecipe(recipeId, inputFluid, resultA::copy, resultB::copy, processingTime);
        }

        @Nullable
        @Override
        public CentrifugalDepositionRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            FluidIngredient inputFluid = FluidIngredient.fromNetwork(buffer);
            ItemStack resultA = buffer.readItem();
            ItemStack resultB = buffer.readItem();
            int processingTime = buffer.readInt();
            return new CentrifugalDepositionRecipe(recipeId, inputFluid, resultA::copy, resultB::copy, processingTime);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, CentrifugalDepositionRecipe recipe) {
            recipe.inputFluid.toNetwork(buffer);
            buffer.writeItem(recipe.resultA.get());
            buffer.writeItem(recipe.resultB.get());
            buffer.writeInt(recipe.processingTime);
        }

    }

}
