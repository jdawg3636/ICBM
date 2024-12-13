package com.jdawg3636.icbm.common.reg;

import com.jdawg3636.icbm.ICBMReference;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FluidReg {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, ICBMReference.MODID);

    public static final RegistryObjectFluidPair FUEL = registerICBMFluid("fuel", ItemReg.FUEL_BUCKET, BlockReg.FUEL, builder -> builder.color(0xff_b38614).density(1024).viscosity(1024));
    public static final RegistryObjectFluidPair OIL  = registerICBMFluid("oil",  ItemReg.OIL_BUCKET,  BlockReg.OIL,  builder -> builder.color(0xff_282828).density(1024).viscosity(1024));

    public static RegistryObjectFluidPair registerICBMFluid(String name, Supplier<Item> bucketForm, Supplier<FlowingFluidBlock> blockForm, Consumer<FluidAttributes.Builder> fluidAttributesCallback) {
        final ResourceLocation sourceResourceLocation = new ResourceLocation(ICBMReference.MODID, name);
        final ResourceLocation flowingResourceLocation = new ResourceLocation(ICBMReference.MODID, "flowing_" + name);
        final FluidAttributes.Builder fluidAttributesBuilder = FluidAttributes.builder(Fluids.WATER.getAttributes().getStillTexture(), Fluids.WATER.getAttributes().getFlowingTexture());
        fluidAttributesCallback.accept(fluidAttributesBuilder);
        final ForgeFlowingFluid.Properties fluidProperties = new ForgeFlowingFluid.Properties(
                () -> ForgeRegistries.FLUIDS.getValue(sourceResourceLocation),
                () -> ForgeRegistries.FLUIDS.getValue(flowingResourceLocation),
                fluidAttributesBuilder
        ).bucket(bucketForm).block(blockForm);
        final RegistryObject<ForgeFlowingFluid> sourceRegistryObject = FLUIDS.register(sourceResourceLocation.getPath(), () -> new ForgeFlowingFluid.Source(fluidProperties));
        final RegistryObject<ForgeFlowingFluid> flowingRegistryObject = FLUIDS.register(flowingResourceLocation.getPath(), () -> new ForgeFlowingFluid.Flowing(fluidProperties));
        return new RegistryObjectFluidPair(sourceRegistryObject, flowingRegistryObject);
    }

    public static class RegistryObjectFluidPair {

        public final RegistryObject<ForgeFlowingFluid> source;
        public final RegistryObject<ForgeFlowingFluid> flowing;

        public RegistryObjectFluidPair(RegistryObject<ForgeFlowingFluid> source, RegistryObject<ForgeFlowingFluid> flowing) {
            this.source = source;
            this.flowing = flowing;
        }

    }

}
