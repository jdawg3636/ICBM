package com.jdawg3636.icbm.common.recipe;

import com.google.gson.JsonObject;
import com.jdawg3636.icbm.common.event.EventBlastAcceleratingParticle;
import com.jdawg3636.icbm.common.reg.ICBMRecipeTypes;
import com.jdawg3636.icbm.common.reg.RecipeSerializerReg;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public class ParticleAcceleratorRecipe implements IRecipe<IInventory> {
    private final ResourceLocation recipeId;
    private EventBlastAcceleratingParticle.ExplosionCause explosionCause;
    private float speedPercentRequired;
    private float dropChance;
    private Supplier<ItemStack> result;

    public ParticleAcceleratorRecipe(ResourceLocation recipeId, EventBlastAcceleratingParticle.ExplosionCause explosionCause, float speedPercentRequired, float dropChance, Supplier<ItemStack> result) {
        this.recipeId = recipeId;
        this.explosionCause = explosionCause;
        this.speedPercentRequired = speedPercentRequired;
        this.dropChance = dropChance;
        this.result = result;
    }

    public static Optional<ParticleAcceleratorRecipe> getRecipeFor(IRecipeType<ParticleAcceleratorRecipe> recipeType, EventBlastAcceleratingParticle.ExplosionCause explosionCause, World level) {
        return level.getRecipeManager().byType(recipeType).values().stream().flatMap((recipe) -> {
            return Util.toStream(recipe instanceof ParticleAcceleratorRecipe && ((ParticleAcceleratorRecipe) recipe).matchesCustom(explosionCause) ? Optional.of((ParticleAcceleratorRecipe)recipe) : Optional.empty());
        }).findFirst();
    }

    @Deprecated
    @Override
    public boolean matches(IInventory inventory, World world) {
        // Would normally use something like this: Optional<ICraftingRecipe> optional = pLevel.getServer().getRecipeManager().getRecipeFor(IRecipeType.CRAFTING, pContainer, pLevel);
        // Won't work since the recipe input isn't an inventory. Need to instead use: Optional<ParticleAcceleratorRecipe> optional = ParticleAcceleratorRecipe.getRecipeFor(ICBMRecipeTypes.PARTICLE_ACCELERATOR, explosionCause, level);
        return false;
    }

    public boolean matchesCustom(EventBlastAcceleratingParticle.ExplosionCause explosionCause) {
        return this.explosionCause == explosionCause;
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
        return RecipeSerializerReg.PARTICLE_ACCELERATOR.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ICBMRecipeTypes.PARTICLE_ACCELERATOR;
    }

    public EventBlastAcceleratingParticle.ExplosionCause getExplosionCause() {
        return this.explosionCause;
    }
    public float getSpeedPercentRequired() {
        return this.speedPercentRequired;
    }
    public float getDropChance() {
        return this.dropChance;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ParticleAcceleratorRecipe> {

        @Override
        public ParticleAcceleratorRecipe fromJson(ResourceLocation recipeId, JsonObject jsonObject) {
            EventBlastAcceleratingParticle.ExplosionCause explosionCause = EventBlastAcceleratingParticle.ExplosionCause.valueOf(JSONUtils.getAsString(jsonObject, "explosion_cause"));
            float speedPercentRequired = JSONUtils.getAsFloat(jsonObject, "speed_percent_required");
            float dropChance = JSONUtils.getAsFloat(jsonObject, "drop_chance");
            ItemStack result = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(jsonObject, "result"));
            return new ParticleAcceleratorRecipe(recipeId, explosionCause, speedPercentRequired, dropChance, result::copy);
        }

        @Nullable
        @Override
        public ParticleAcceleratorRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            EventBlastAcceleratingParticle.ExplosionCause explosionCause = EventBlastAcceleratingParticle.ExplosionCause.valueOf(buffer.readUtf());
            float speedPercentRequired = buffer.readFloat();
            float dropChance = buffer.readFloat();
            ItemStack result = buffer.readItem();
            return new ParticleAcceleratorRecipe(recipeId, explosionCause, speedPercentRequired, dropChance, result::copy);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, ParticleAcceleratorRecipe recipe) {
            buffer.writeUtf(recipe.explosionCause.name());
            buffer.writeFloat(recipe.speedPercentRequired);
            buffer.writeFloat(recipe.dropChance);
            buffer.writeItem(recipe.result.get());
        }

    }

}
