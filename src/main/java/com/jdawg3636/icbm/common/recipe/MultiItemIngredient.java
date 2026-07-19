package com.jdawg3636.icbm.common.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class MultiItemIngredient implements Predicate<ItemStack> {

    private final Ingredient ingredient;
    private final int count;

    private MultiItemIngredient(Ingredient ingredient, int count) {
        this.ingredient = ingredient;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public static MultiItemIngredient fromJson(@Nullable JsonElement jsonElement) {
        if(jsonElement == null || jsonElement.isJsonNull() || !jsonElement.isJsonObject()) {
            return null;
        }
        Ingredient ingredient = Ingredient.fromJson(jsonElement);
        int count = 1;
        JsonObject jsonObject = (JsonObject) jsonElement;
        if(jsonObject.has("count")) {
            JsonElement countElement = jsonObject.get("count");
            if(countElement.isJsonPrimitive() && countElement.getAsJsonPrimitive().isNumber()) {
                count = countElement.getAsInt();
            }
            else {
                throw new JsonSyntaxException("Ingredient count must be an integer!");
            }
        }
        return new MultiItemIngredient(ingredient, count);
    }

    public static MultiItemIngredient fromNetwork(PacketBuffer buffer) {
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        int count = buffer.readVarInt();
        return new MultiItemIngredient(ingredient, count);
    }

    public final void toNetwork(PacketBuffer buffer) {
        this.ingredient.toNetwork(buffer);
        buffer.writeVarInt(count);
    }

    @Override
    public boolean test(ItemStack stack) {
        return this.ingredient.test(stack) && stack.getCount() >= this.count;
    }

}
