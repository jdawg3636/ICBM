package com.jdawg3636.icbm.common.reg;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.launcher_platform.TileLauncherPlatformT1;
import com.jdawg3636.icbm.common.block.launcher_platform.TileLauncherPlatformT2;
import com.jdawg3636.icbm.common.block.launcher_platform.TileLauncherPlatformT3;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileReg {

    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ICBMReference.MODID);

    public static final RegistryObject<TileEntityType<? extends TileEntity>> LAUNCHER_PLATFORM_T1 = TILES.register(BlockReg.LAUNCHER_PLATFORM_T1.getId().getPath(), () -> TileEntityType.Builder.of(TileLauncherPlatformT1::new, BlockReg.LAUNCHER_PLATFORM_T1.get()).build(null));
    public static final RegistryObject<TileEntityType<? extends TileEntity>> LAUNCHER_PLATFORM_T2 = TILES.register(BlockReg.LAUNCHER_PLATFORM_T2.getId().getPath(), () -> TileEntityType.Builder.of(TileLauncherPlatformT2::new, BlockReg.LAUNCHER_PLATFORM_T2.get()).build(null));
    public static final RegistryObject<TileEntityType<? extends TileEntity>> LAUNCHER_PLATFORM_T3 = TILES.register(BlockReg.LAUNCHER_PLATFORM_T3.getId().getPath(), () -> TileEntityType.Builder.of(TileLauncherPlatformT3::new, BlockReg.LAUNCHER_PLATFORM_T3.get()).build(null));

}
