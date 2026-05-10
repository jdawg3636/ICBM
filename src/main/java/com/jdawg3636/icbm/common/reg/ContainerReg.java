package com.jdawg3636.icbm.common.reg;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.machine.ContainerMachine;
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
    public static final RegistryObject<ContainerType<ContainerMachine>> LAUNCHER_PLATFORM_T1 = registerContainer(() -> ContainerReg.LAUNCHER_PLATFORM_T1, BlockReg.LAUNCHER_PLATFORM_T1, ContainerMachine::new, 8, 84);
    public static final RegistryObject<ContainerType<ContainerMachine>> LAUNCHER_PLATFORM_T2 = registerContainer(() -> ContainerReg.LAUNCHER_PLATFORM_T2, BlockReg.LAUNCHER_PLATFORM_T2, ContainerMachine::new, 8, 84);
    public static final RegistryObject<ContainerType<ContainerMachine>> LAUNCHER_PLATFORM_T3 = registerContainer(() -> ContainerReg.LAUNCHER_PLATFORM_T3, BlockReg.LAUNCHER_PLATFORM_T3, ContainerMachine::new, 8, 84);

    // Other Machinery
    public static final RegistryObject<ContainerType<ContainerMachine>> CENTRIFUGAL_DEPOSITION_UNIT = registerContainer(() -> ContainerReg.CENTRIFUGAL_DEPOSITION_UNIT, BlockReg.CENTRIFUGAL_DEPOSITION_UNIT, ContainerMachine::new);
    public static final RegistryObject<ContainerType<ContainerMachine>> COAL_GENERATOR = registerContainer(() -> ContainerReg.COAL_GENERATOR, BlockReg.COAL_GENERATOR, ContainerMachine::new, 8, 84);
    public static final RegistryObject<ContainerType<ContainerMachine>> CRUISE_LAUNCHER = registerContainer(() -> ContainerReg.CRUISE_LAUNCHER, BlockReg.CRUISE_LAUNCHER, ContainerMachine::new);
    public static final RegistryObject<ContainerType<ContainerMachine>> EMP_TOWER = registerContainer(() -> ContainerReg.EMP_TOWER, BlockReg.EMP_TOWER, ContainerMachine::new);
    public static final RegistryObject<ContainerType<ContainerMachine>> METALLURGIC_LEACHING_CHAMBER = registerContainer(() -> ContainerReg.METALLURGIC_LEACHING_CHAMBER, BlockReg.METALLURGIC_LEACHING_CHAMBER, ContainerMachine::new);
    public static final RegistryObject<ContainerType<ContainerMachine>> OIL_REFINERY = registerContainer(() -> ContainerReg.OIL_REFINERY, BlockReg.OIL_REFINERY, ContainerMachine::new);
    public static final RegistryObject<ContainerType<ContainerMachine>> PROXIMITY_DETECTOR = registerContainer(() -> ContainerReg.PROXIMITY_DETECTOR, BlockReg.PROXIMITY_DETECTOR, ContainerMachine::new);
    public static final RegistryObject<ContainerType<ContainerMachine>> RADAR_STATION = registerContainer(() -> ContainerReg.RADAR_STATION, BlockReg.RADAR_STATION, ContainerMachine::new);

    // Particle Accelerator Components
    public static final RegistryObject<ContainerType<ContainerMachine>> PARTICLE_ACCELERATOR = registerContainer(() -> ContainerReg.PARTICLE_ACCELERATOR, BlockReg.PARTICLE_ACCELERATOR, ContainerMachine::new);

    @FunctionalInterface
    public interface IModContainerFactory<T extends ContainerMachine> {
        T construct(ContainerType<T> containerType, int windowId, World level, BlockPos blockPos, PlayerInventory playerInventory, int playerInventorySlotsLeftCol, int playerInventorySlotsTopRow);
    }

    public static <T extends ContainerMachine> RegistryObject<ContainerType<T>> registerContainer(Supplier<RegistryObject<ContainerType<T>>> self, RegistryObject<Block> block, IModContainerFactory<T> containerConstructor) {
        return registerContainer(self, block, containerConstructor, 8, 135);
    }

    public static <T extends ContainerMachine> RegistryObject<ContainerType<T>> registerContainer(Supplier<RegistryObject<ContainerType<T>>> self, RegistryObject<Block> block, IModContainerFactory<T> containerConstructor, int playerInventorySlotsLeftCol, int playerInventorySlotsTopRow) {
        return CONTAINERS.register(
                block.getId().getPath(),
                () -> {
                    return IForgeContainerType.create(
                            (windowId, inv, data) -> {
                                BlockPos pos = data.readBlockPos();
                                World world = inv.player.getCommandSenderWorld();
                                return containerConstructor.construct(self.get().get(), windowId, world, pos, inv, playerInventorySlotsLeftCol, playerInventorySlotsTopRow);
                            }
                    );
                }
        );
    }

}
