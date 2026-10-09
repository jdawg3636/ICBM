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
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class AdvancedGrindstoneRecipe implements IProcessingRecipe {
    public static final int INTERACTIONS_REQUIRED = 20;

    private final ResourceLocation id;
    private final Ingredient input;
    private final Ingredient abrasive;
    private final Supplier<ItemStack> result;

    public AdvancedGrindstoneRecipe(ResourceLocation id, Ingredient input, Ingredient abrasive, Supplier<ItemStack> result) {
        this.id = id;
        this.input = input;
        this.abrasive = abrasive;
        this.result = result;
    }

    public static Stream<AdvancedGrindstoneRecipe> getAllRecipes(IRecipeType<AdvancedGrindstoneRecipe> recipeType, World level) {
        return ((IRecipeManagerAccessor) level.getRecipeManager()).invokeByType(recipeType).values().stream()
                .filter(AdvancedGrindstoneRecipe.class::isInstance).map(AdvancedGrindstoneRecipe.class::cast);
    }

    public static Optional<AdvancedGrindstoneRecipe> getRecipeFor(ItemStack input, ItemStack abrasive, World level) {
        return getAllRecipes(ICBMRecipeTypes.ADVANCED_GRINDSTONE, level)
                .filter(recipe -> recipe.matchesCustom(input, abrasive)).findFirst();
    }

    public boolean matchesCustom(ItemStack input, ItemStack abrasive) {
        return this.input.test(input) && (this.abrasive.getItems().length == 0 || this.abrasive.test(abrasive));
    }

    public Ingredient getInput() { return input; }
    public Ingredient getAbrasive() { return abrasive; }

    @Deprecated @Override public boolean matches(IInventory inventory, World world) { return false; }
    @Override public ItemStack assemble(IInventory inventory) { return result.get(); }
    @Override public boolean canCraftInDimensions(int width, int height) { return true; }
    @Override public ItemStack getResultItem() { return result.get(); }
    @Override public ResourceLocation getId() { return id; }
    @Override public IRecipeSerializer<?> getSerializer() { return RecipeSerializerReg.ADVANCED_GRINDSTONE.get(); }
    @Override public IRecipeType<?> getType() { return ICBMRecipeTypes.ADVANCED_GRINDSTONE; }
    @Override public int getProcessingTime() { return INTERACTIONS_REQUIRED; }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AdvancedGrindstoneRecipe> {
        @Override
        public AdvancedGrindstoneRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient input = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "input"));
            Ingredient abrasive = json.has("abrasive") ? Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "abrasive")) : Ingredient.EMPTY;
            ItemStack result = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
            return new AdvancedGrindstoneRecipe(id, input, abrasive, result::copy);
        }

        @Nullable @Override
        public AdvancedGrindstoneRecipe fromNetwork(ResourceLocation id, PacketBuffer buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            Ingredient abrasive = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            return new AdvancedGrindstoneRecipe(id, input, abrasive, result::copy);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, AdvancedGrindstoneRecipe recipe) {
            recipe.input.toNetwork(buffer);
            recipe.abrasive.toNetwork(buffer);
            buffer.writeItem(recipe.result.get());
        }
    }
}
