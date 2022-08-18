package com.jdawg3636.icbm.common.item.armormaterial;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public class HazmatArmorMaterial implements IArmorMaterial {

    public static HazmatArmorMaterial HAZMAT_MASK = new HazmatArmorMaterial("icbm_hazmat_mask", () -> Items.LEATHER);
    public static HazmatArmorMaterial HAZMAT_SUIT = new HazmatArmorMaterial("icbm_hazmat_suit", () -> Items.IRON_INGOT);

    private HazmatArmorMaterial(String name, Supplier<Item> item) {
        this.item = item;
        this.name = name;
    }

    public final Supplier<Item> item;
    public final String name;
    public final int[] slotProtections = {1, 2, 3, 1};

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getName() {
        return name;
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlotType pSlot) {
        return ArmorMaterial.HEALTH_PER_SLOT[pSlot.getIndex()] * 15;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlotType pSlot) {
        return this.slotProtections[pSlot.getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_LEATHER;
    }

    @Override
    public float getToughness() {
        return 0f;
    }

    @Override
    public float getKnockbackResistance() {
        return 0f;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(Items.LEATHER);
    }

}
