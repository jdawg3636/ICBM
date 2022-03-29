package com.jdawg3636.icbm.common.reg;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.cruise_launcher.ContainerCruiseLauncher;
import com.jdawg3636.icbm.common.block.launcher_platform.ContainerLauncherPlatform;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerReg {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, ICBMReference.MODID);

    public static final RegistryObject<ContainerType<ContainerLauncherPlatform>> LAUNCHER_PLATFORM_T1 = CONTAINERS.register(BlockReg.LAUNCHER_PLATFORM_T1.getId().getPath(), () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getCommandSenderWorld();
        return new ContainerLauncherPlatform(ContainerReg.LAUNCHER_PLATFORM_T1.get(), BlockReg.LAUNCHER_PLATFORM_T1.get(), windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<ContainerType<ContainerLauncherPlatform>> LAUNCHER_PLATFORM_T2 = CONTAINERS.register(BlockReg.LAUNCHER_PLATFORM_T2.getId().getPath(), () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getCommandSenderWorld();
        return new ContainerLauncherPlatform(ContainerReg.LAUNCHER_PLATFORM_T2.get(), BlockReg.LAUNCHER_PLATFORM_T2.get(), windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<ContainerType<ContainerLauncherPlatform>> LAUNCHER_PLATFORM_T3 = CONTAINERS.register(BlockReg.LAUNCHER_PLATFORM_T3.getId().getPath(), () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getCommandSenderWorld();
        return new ContainerLauncherPlatform(ContainerReg.LAUNCHER_PLATFORM_T3.get(), BlockReg.LAUNCHER_PLATFORM_T3.get(), windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<ContainerType<ContainerCruiseLauncher>> CRUISE_LAUNCHER = CONTAINERS.register(BlockReg.CRUISE_LAUNCHER.getId().getPath(), () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getCommandSenderWorld();
        return new ContainerCruiseLauncher(ContainerReg.CRUISE_LAUNCHER.get(), BlockReg.CRUISE_LAUNCHER.get(), windowId, world, pos, inv, inv.player);
    }));

}
