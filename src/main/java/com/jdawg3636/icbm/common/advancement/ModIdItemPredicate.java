package com.jdawg3636.icbm.common.advancement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

/**
 * Allows the modid of an item to be used in data-driven advancement criteria.
 * We use this to award the "Ballistic Redux" advancement the first time an item from this mod is acquired.
 */
public final class ModIdItemPredicate extends ItemPredicate {
    public static final ResourceLocation ID = new ResourceLocation(ICBMReference.MODID, "modid");

    private final String modid;
    private final ItemPredicate vanillaPredicate;

    private ModIdItemPredicate(JsonObject json) {
        this.modid = JSONUtils.getAsString(json, "modid");
        if (modid.isEmpty() || !ResourceLocation.isValidResourceLocation(modid + ":item") || modid.contains(":")) {
            throw new JsonSyntaxException("Invalid item predicate modid: " + modid);
        }
        JsonObject vanillaJson = new JsonObject();
        json.entrySet().forEach(entry -> {
            if (!entry.getKey().equals("type") && !entry.getKey().equals("modid")) {
                vanillaJson.add(entry.getKey(), entry.getValue());
            }
        });
        this.vanillaPredicate = ItemPredicate.fromJson(vanillaJson);
    }

    public static void register() {
        ItemPredicate.register(ID, ModIdItemPredicate::new);
    }

    @Override
    public boolean matches(ItemStack stack) {
        ResourceLocation itemId = stack.getItem().getRegistryName();
        return !stack.isEmpty() && itemId != null && modid.equals(itemId.getNamespace()) && vanillaPredicate.matches(stack);
    }

    @Override
    public JsonElement serializeToJson() {
        JsonObject json = vanillaPredicate.serializeToJson().getAsJsonObject();
        json.addProperty("type", ID.toString());
        json.addProperty("modid", modid);
        return json;
    }
}
