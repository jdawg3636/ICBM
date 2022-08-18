package com.jdawg3636.icbm.common.item;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.item.armormaterial.HazmatArmorMaterial;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;

public class ItemHazmatArmor extends ArmorItem implements IDyeableArmorItem {

    public final int color;

    public ItemHazmatArmor(EquipmentSlotType equipmentSlotType) {
        this(equipmentSlotType == EquipmentSlotType.HEAD ? HazmatArmorMaterial.HAZMAT_MASK : HazmatArmorMaterial.HAZMAT_SUIT, equipmentSlotType, new Item.Properties().tab(ICBMReference.CREATIVE_TAB));
    }

    public ItemHazmatArmor(IArmorMaterial armorMaterial, EquipmentSlotType equipmentSlotType, Item.Properties itemProperties) {
        super(armorMaterial, equipmentSlotType, itemProperties);
        if(equipmentSlotType == EquipmentSlotType.HEAD || equipmentSlotType == EquipmentSlotType.FEET) {
            color = 0x1D1D21;
        }
        else {
            color = 0xE9BF3E;
        }
    }

    @Override
    public int getColor(ItemStack itemStack) {
        CompoundNBT displayNBT = itemStack.getTagElement("display");
        return displayNBT != null && displayNBT.contains("color", 99) ? displayNBT.getInt("color") : color;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return String.format("%s:textures/models/armor/%s_layer_%d%s.png", ICBMReference.MODID, "hazmat", slot == EquipmentSlotType.LEGS ? 2 : 1, type == null ? "" : String.format("_%s", type));
    }

}
