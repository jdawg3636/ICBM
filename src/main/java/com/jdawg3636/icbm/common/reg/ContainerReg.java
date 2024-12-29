package com.jdawg3636.icbm.common.reg;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.coal_generator.ContainerCoalGenerator;
import com.jdawg3636.icbm.common.block.cruise_launcher.ContainerCruiseLauncher;
import com.jdawg3636.icbm.common.block.emp_tower.ContainerEMPTower;
import com.jdawg3636.icbm.common.block.launcher_platform.ContainerLauncherPlatform;
import com.jdawg3636.icbm.common.block.machine.AbstractContainerMachine;
import com.jdawg3636.icbm.common.block.oil_refinery.ContainerOilRefinery;
import com.jdawg3636.icbm.common.block.particle_accelerator.ContainerParticleAccelerator;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ContainerReg {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, ICBMReference.MODID);

    // Missile Launch Apparatus
    public static final RegistryObject<ContainerType<ContainerLauncherPlatform>> LAUNCHER_PLATFORM_T1 = registerContainer(BlockReg.LAUNCHER_PLATFORM_T1, ContainerLauncherPlatform::new, () -> ContainerReg.LAUNCHER_PLATFORM_T1);
    public static final RegistryObject<ContainerType<ContainerLauncherPlatform>> LAUNCHER_PLATFORM_T2 = registerContainer(BlockReg.LAUNCHER_PLATFORM_T2, ContainerLauncherPlatform::new, () -> ContainerReg.LAUNCHER_PLATFORM_T2);
    public static final RegistryObject<ContainerType<ContainerLauncherPlatform>> LAUNCHER_PLATFORM_T3 = registerContainer(BlockReg.LAUNCHER_PLATFORM_T3, ContainerLauncherPlatform::new, () -> ContainerReg.LAUNCHER_PLATFORM_T3);

    // Other Machinery
    public static final RegistryObject<ContainerType<ContainerCoalGenerator>> COAL_GENERATOR = registerContainer(BlockReg.COAL_GENERATOR, ContainerCoalGenerator::new, () -> ContainerReg.COAL_GENERATOR);
    public static final RegistryObject<ContainerType<ContainerCruiseLauncher>> CRUISE_LAUNCHER = registerContainer(BlockReg.CRUISE_LAUNCHER, ContainerCruiseLauncher::new, () -> ContainerReg.CRUISE_LAUNCHER);
    public static final RegistryObject<ContainerType<ContainerEMPTower>> EMP_TOWER = registerContainer(BlockReg.EMP_TOWER, ContainerEMPTower::new, () -> ContainerReg.EMP_TOWER);
    public static final RegistryObject<ContainerType<ContainerOilRefinery>> OIL_REFINERY = registerContainer(BlockReg.OIL_REFINERY, ContainerOilRefinery::new, () -> ContainerReg.OIL_REFINERY);

    // Particle Accelerator Components
    public static final RegistryObject<ContainerType<ContainerParticleAccelerator>> PARTICLE_ACCELERATOR = registerContainer(BlockReg.PARTICLE_ACCELERATOR, ContainerParticleAccelerator::new, () -> ContainerReg.PARTICLE_ACCELERATOR);

    @FunctionalInterface
    public interface IModContainerFactory<T extends AbstractContainerMachine> {
        T construct(ContainerType<T> containerType, int windowId, World level, BlockPos blockPos, PlayerInventory playerInventory);
    }

    public static <T extends AbstractContainerMachine> RegistryObject<ContainerType<T>> registerContainer(RegistryObject<Block> block, IModContainerFactory<T> containerConstructor, Supplier<RegistryObject<ContainerType<T>>> self) {
        return CONTAINERS.register(
                block.getId().getPath(),
                () -> {
                    return IForgeContainerType.create(
                            (windowId, inv, data) -> {
                                BlockPos pos = data.readBlockPos();
                                World world = inv.player.getCommandSenderWorld();
                                return containerConstructor.construct(self.get().get(), windowId, world, pos, inv);
                            }
                    );
                }
        );
    }

}
