package com.jdawg3636.icbm.common.reg;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.cruise_launcher.TileCruiseLauncher;
import com.jdawg3636.icbm.common.block.emp_tower.TileEMPTower;
import com.jdawg3636.icbm.common.block.launcher_control_panel.TileLauncherControlPanelT1;
import com.jdawg3636.icbm.common.block.launcher_control_panel.TileLauncherControlPanelT2;
import com.jdawg3636.icbm.common.block.launcher_control_panel.TileLauncherControlPanelT3;
import com.jdawg3636.icbm.common.block.launcher_platform.TileLauncherPlatform;
import com.jdawg3636.icbm.common.block.radar_station.TileRadarStation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

// Suppression required as IntelliJ is convinced that TileEntityType::build cannot accept null as a DataFixer, even though the vanilla game does this without issue.
@SuppressWarnings("ConstantConditions")
public class TileReg {

    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ICBMReference.MODID);

    // Launcher Platforms
    public static final RegistryObject<TileEntityType<? extends TileEntity>> LAUNCHER_PLATFORM_T1 = TILES.register(BlockReg.LAUNCHER_PLATFORM_T1.getId().getPath(), () -> TileEntityType.Builder.of(() -> new TileLauncherPlatform(TileReg.LAUNCHER_PLATFORM_T1.get()), BlockReg.LAUNCHER_PLATFORM_T1.get()).build(null));
    public static final RegistryObject<TileEntityType<? extends TileEntity>> LAUNCHER_PLATFORM_T2 = TILES.register(BlockReg.LAUNCHER_PLATFORM_T2.getId().getPath(), () -> TileEntityType.Builder.of(() -> new TileLauncherPlatform(TileReg.LAUNCHER_PLATFORM_T2.get()), BlockReg.LAUNCHER_PLATFORM_T2.get()).build(null));
    public static final RegistryObject<TileEntityType<? extends TileEntity>> LAUNCHER_PLATFORM_T3 = TILES.register(BlockReg.LAUNCHER_PLATFORM_T3.getId().getPath(), () -> TileEntityType.Builder.of(() -> new TileLauncherPlatform(TileReg.LAUNCHER_PLATFORM_T3.get()) {
        @Override
        public double getMissileEntityYOffset() {
            return 6D/16D;
        }
    }, BlockReg.LAUNCHER_PLATFORM_T3.get()).build(null));

    // Launcher Control Panels
    public static final RegistryObject<TileEntityType<? extends TileEntity>> LAUNCHER_CONTROL_PANEL_T1 = TILES.register(
            BlockReg.LAUNCHER_CONTROL_PANEL_T1.getId().getPath(),
            () -> TileEntityType.Builder.of(
                    () -> new TileLauncherControlPanelT1(TileReg.LAUNCHER_CONTROL_PANEL_T1.get()),
                    BlockReg.LAUNCHER_CONTROL_PANEL_T1.get()
            ).build(null)
    );
    public static final RegistryObject<TileEntityType<? extends TileEntity>> LAUNCHER_CONTROL_PANEL_T2 = TILES.register(
            BlockReg.LAUNCHER_CONTROL_PANEL_T2.getId().getPath(),
            () -> TileEntityType.Builder.of(
                    () -> new TileLauncherControlPanelT2(TileReg.LAUNCHER_CONTROL_PANEL_T2.get()),
                    BlockReg.LAUNCHER_CONTROL_PANEL_T2.get()
            ).build(null)
    );
    public static final RegistryObject<TileEntityType<? extends TileEntity>> LAUNCHER_CONTROL_PANEL_T3 = TILES.register(
            BlockReg.LAUNCHER_CONTROL_PANEL_T3.getId().getPath(),
            () -> TileEntityType.Builder.of(
                    () -> new TileLauncherControlPanelT3(TileReg.LAUNCHER_CONTROL_PANEL_T3.get()),
                    BlockReg.LAUNCHER_CONTROL_PANEL_T3.get()
            ).build(null)
    );

    // Other Machinery
    public static final RegistryObject<TileEntityType<? extends TileEntity>> CRUISE_LAUNCHER = TILES.register(BlockReg.CRUISE_LAUNCHER.getId().getPath(), () -> TileEntityType.Builder.of(() -> new TileCruiseLauncher(TileReg.CRUISE_LAUNCHER.get()), BlockReg.CRUISE_LAUNCHER.get()).build(null));
    public static final RegistryObject<TileEntityType<? extends TileEntity>> EMP_TOWER       = TILES.register(BlockReg.EMP_TOWER.getId().getPath(),       () -> TileEntityType.Builder.of(() -> new TileEMPTower(TileReg.EMP_TOWER.get()),             BlockReg.EMP_TOWER.get()      ).build(null));
    public static final RegistryObject<TileEntityType<? extends TileEntity>> RADAR_STATION   = TILES.register(BlockReg.RADAR_STATION.getId().getPath(),   () -> TileEntityType.Builder.of(() -> new TileRadarStation(TileReg.RADAR_STATION.get()),     BlockReg.RADAR_STATION.get()  ).build(null));

}
