package com.jdawg3636.icbm.common.reg;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.container.ContainerLauncherPlatform;
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
        World world = inv.player.getEntityWorld();
        return new ContainerLauncherPlatform(windowId, world, pos, inv, inv.player);
    }));

}
