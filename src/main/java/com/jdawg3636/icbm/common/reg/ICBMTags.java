package com.jdawg3636.icbm.common.reg;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

/**
 * Based on net.minecraftforge.common.Tags
 * Can be used for datagen - currently just using as a reference holder to prevent use of magic strings
 */
public class ICBMTags {

    public static void makeSureThisClassIsLoaded() {
        Blocks.makeSureThisClassIsLoaded();
        Items.makeSureThisClassIsLoaded();
        Fluids.makeSureThisClassIsLoaded();
    }

    public static class Blocks {

        public static void makeSureThisClassIsLoaded() {}

        public static final net.minecraftforge.common.Tags.IOptionalNamedTag<Block> CAN_BE_REPLACED_AFTER_ENDOTHERMIC_BLAST = tag("can_be_replaced_after_endothermic_blast");
        public static final net.minecraftforge.common.Tags.IOptionalNamedTag<Block> CAN_BE_REPLACED_AFTER_EXOTHERMIC_BLAST  = tag("can_be_replaced_after_exothermic_blast");
        public static final net.minecraftforge.common.Tags.IOptionalNamedTag<Block> CAN_BE_REPLACED_AFTER_NUCLEAR_BLAST     = tag("can_be_replaced_after_nuclear_blast");
        public static final net.minecraftforge.common.Tags.IOptionalNamedTag<Block> MAGNETIC                                = tag("magnetic");

        private static net.minecraftforge.common.Tags.IOptionalNamedTag<Block> tag(String name) {
            return BlockTags.createOptional(new ResourceLocation(ICBMReference.MODID, name));
        }

    }

    public static class Items {

        public static void makeSureThisClassIsLoaded() {}

        public static final net.minecraftforge.common.Tags.IOptionalNamedTag<Item> MISSILES             = tag("missiles");
        public static final net.minecraftforge.common.Tags.IOptionalNamedTag<Item> RADIATION_PROTECTIVE = tag("radiation_protective");

        private static net.minecraftforge.common.Tags.IOptionalNamedTag<Item> tag(String name) {
            return tag(new ResourceLocation(ICBMReference.MODID, name));
        }

        private static net.minecraftforge.common.Tags.IOptionalNamedTag<Item> tag(ResourceLocation resourceLocation) {
            return net.minecraft.tags.ItemTags.createOptional(resourceLocation);
        }

    }

    public static class Fluids {

        public static void makeSureThisClassIsLoaded() {}

        public static final Tags.IOptionalNamedTag<Fluid> FUEL = tag(new ResourceLocation("forge", "fuel"));
        public static final Tags.IOptionalNamedTag<Fluid> OIL  = tag(new ResourceLocation("forge", "crude_oil"));

        private static net.minecraftforge.common.Tags.IOptionalNamedTag<Fluid> tag(String name) {
            return tag(new ResourceLocation(ICBMReference.MODID, name));
        }

        private static net.minecraftforge.common.Tags.IOptionalNamedTag<Fluid> tag(ResourceLocation resourceLocation) {
            return FluidTags.createOptional(resourceLocation);
        }

    }

}
