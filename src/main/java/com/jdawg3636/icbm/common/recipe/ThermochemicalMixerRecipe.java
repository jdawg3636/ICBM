package com.jdawg3636.icbm.common.recipe;

import com.google.gson.JsonObject;
import com.jdawg3636.icbm.common.reg.ICBMRecipeTypes;
import com.jdawg3636.icbm.common.reg.RecipeSerializerReg;
import com.jdawg3636.icbm.mixin.accessors.IRecipeManagerAccessor;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Optional;

public class ThermochemicalMixerRecipe implements IProcessingRecipe {
    private final ResourceLocation recipeId;
    private final FluidIngredient inputFluid;
    private final MultiItemIngredient inputItemA;
    private final MultiItemIngredient inputItemB;
    private FluidIngredient result;
    private int processingTime;

    public ThermochemicalMixerRecipe(ResourceLocation recipeId, FluidIngredient inputFluid, MultiItemIngredient inputItemA, MultiItemIngredient inputItemB, FluidIngredient result, int processingTime) {
        this.recipeId = recipeId;
        this.inputFluid = inputFluid;
        this.inputItemA = inputItemA;
        this.inputItemB = inputItemB;
        this.result = result;
        this.processingTime = processingTime;
    }

    public static Optional<ThermochemicalMixerRecipe> getRecipeFor(IRecipeType<ThermochemicalMixerRecipe> recipeType, FluidStack inputFluid, ItemStack inputItemA, ItemStack inputItemB, World level) {
        return ((IRecipeManagerAccessor)level.getRecipeManager()).invokeByType(recipeType).values().stream()
                .filter(ThermochemicalMixerRecipe.class::isInstance).map(ThermochemicalMixerRecipe.class::cast)
                .filter(recipe -> recipe.matchesCustom(inputFluid, inputItemA, inputItemB))
                .findFirst();
    }

    @Deprecated
    @Override
    public boolean matches(IInventory inventory, World world) {
        // Would normally use something like this: Optional<ICraftingRecipe> optional = pLevel.getServer().getRecipeManager().getRecipeFor(IRecipeType.CRAFTING, pContainer, pLevel);
        // Won't work since the recipe input isn't an inventory. Need to instead use: Optional<ThermochemicalMixerRecipe> optional = ThermochemicalMixerRecipe.getRecipeFor(ICBMRecipeTypes.THERMOCHEMICAL_MIXER, explosionCause, level);
        return false;
    }

    public boolean matchesCustom(FluidStack inputFluid, ItemStack inputItemA, ItemStack inputItemB) {
        return this.inputFluid.test(inputFluid) && this.inputItemA.test(inputItemA) && this.inputItemB.test(inputItemB);
    }

    @Override
    public ItemStack assemble(IInventory inventory) {
        return result.getAsFluidStack().map(fs -> fs.getFluid().getBucket().getDefaultInstance()).orElse(ItemStack.EMPTY);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return result.getAsFluidStack().orElse(FluidStack.EMPTY).getFluid().getBucket().getDefaultInstance();
    }

    @Override
    public ResourceLocation getId() {
        return recipeId;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializerReg.THERMOCHEMICAL_MIXER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ICBMRecipeTypes.THERMOCHEMICAL_MIXER;
    }

    public FluidIngredient getInputFluid() {
        return this.inputFluid;
    }

    public MultiItemIngredient getInputItemA() {
        return this.inputItemA;
    }

    public MultiItemIngredient getInputItemB() {
        return this.inputItemB;
    }

    public FluidStack getOutputFluid() {
        return this.result.getAsFluidStack().orElse(FluidStack.EMPTY);
    }

    @Override
    public int getProcessingTime() {
        return this.processingTime;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ThermochemicalMixerRecipe> {

        @Override
        public ThermochemicalMixerRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            FluidIngredient inputFluid = FluidIngredient.parseFromJson(json.get("input_fluid").getAsJsonObject());
            MultiItemIngredient inputItemA = MultiItemIngredient.fromJson(json.get("input_item_a").getAsJsonObject());
            MultiItemIngredient inputItemB = MultiItemIngredient.fromJson(json.get("input_item_b").getAsJsonObject());
            FluidIngredient result = FluidIngredient.parseFromJson(json.get("result").getAsJsonObject());
            int processingTime = json.get("processing_time").getAsInt();
            return new ThermochemicalMixerRecipe(recipeId, inputFluid, inputItemA, inputItemB, result, processingTime);
        }

        @Nullable
        @Override
        public ThermochemicalMixerRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            FluidIngredient inputFluid = FluidIngredient.fromNetwork(buffer);
            MultiItemIngredient inputItemA = MultiItemIngredient.fromNetwork(buffer);
            MultiItemIngredient inputItemB = MultiItemIngredient.fromNetwork(buffer);
            FluidIngredient result = FluidIngredient.fromNetwork(buffer);
            int processingTime = buffer.readInt();
            return new ThermochemicalMixerRecipe(recipeId, inputFluid, inputItemA, inputItemB, result, processingTime);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, ThermochemicalMixerRecipe recipe) {
            recipe.inputFluid.toNetwork(buffer);
            recipe.inputItemA.toNetwork(buffer);
            recipe.inputItemB.toNetwork(buffer);
            recipe.result.toNetwork(buffer);
            buffer.writeInt(recipe.processingTime);
        }

    }

}
