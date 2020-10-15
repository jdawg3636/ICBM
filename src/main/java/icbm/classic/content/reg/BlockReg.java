package icbm.classic.content.reg;

import icbm.classic.ICBMConstants;
import icbm.classic.content.blocks.*;
import icbm.classic.content.blocks.emptower.BlockEmpTower;
import icbm.classic.content.blocks.emptower.TileEMPTower;
import icbm.classic.content.blocks.explosive.BlockExplosive;
import icbm.classic.content.blocks.explosive.TileEntityExplosive;
import icbm.classic.content.blocks.launcher.base.BlockLauncherBase;
import icbm.classic.content.blocks.launcher.base.TileLauncherBase;
import icbm.classic.content.blocks.launcher.cruise.BlockCruiseLauncher;
import icbm.classic.content.blocks.launcher.cruise.TileCruiseLauncher;
import icbm.classic.content.blocks.launcher.frame.BlockLaunchFrame;
import icbm.classic.content.blocks.launcher.frame.TileLauncherFrame;
import icbm.classic.content.blocks.launcher.screen.BlockLaunchScreen;
import icbm.classic.content.blocks.launcher.screen.TileLauncherScreen;
import icbm.classic.content.blocks.multiblock.BlockMultiblock;
import icbm.classic.content.blocks.multiblock.TileMulti;
import icbm.classic.content.blocks.radarstation.BlockRadarStation;
import icbm.classic.content.blocks.radarstation.TileRadarStation;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockReg {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ICBMConstants.DOMAIN);

    public static final RegistryObject<Block> CONCRETE              = BLOCKS.register("concrete",           BlockConcrete::new);
    public static final RegistryObject<Block> GLASS_BUTTON          = BLOCKS.register("glassButton",        BlockGlassButton::new);
    public static final RegistryObject<Block> GLASS_PRESSURE_PLATE  = BLOCKS.register("glassPressurePlate", BlockGlassPressurePlate::new);
    public static final RegistryObject<Block> REINFORCED_GLASS      = BLOCKS.register("reinforcedGlass",    BlockReinforcedGlass::new);
    public static final RegistryObject<Block> SPIKES                = BLOCKS.register("spikes",             BlockSpikes::new);

    public static final RegistryObject<Block> CRUISE_LAUNCHER       = BLOCKS.register("cruiseLauncher",     BlockCruiseLauncher::new);
    public static final RegistryObject<Block> EMP_TOWER             = BLOCKS.register("emptower",           BlockEmpTower::new);
    public static final RegistryObject<Block> EXPLOSIVES            = BLOCKS.register("explosives",         BlockExplosive::new);
    public static final RegistryObject<Block> LAUNCHER_BASE         = BLOCKS.register("launcherbase",       BlockLauncherBase::new);
    public static final RegistryObject<Block> LAUNCHER_SCREEN       = BLOCKS.register("launcherscreen",     BlockLaunchScreen::new);
    public static final RegistryObject<Block> LAUNCHER_FRAME        = BLOCKS.register("launcherframe",      BlockLaunchFrame::new);
    public static final RegistryObject<Block> MULTIBLOCK            = BLOCKS.register("multiblock",         BlockMultiblock::new);
    public static final RegistryObject<Block> RADAR_STATION         = BLOCKS.register("radarStation",       BlockRadarStation::new);

    //public static Block blockCamo; //TODO re-implement
    //public static Block blockMissileCoordinator; //TODO re-implement
    //event.getRegistry().register(blockCombatRail = new BlockReinforcedRail());
    //blockCamo = manager.newBlock("icbmCCamouflage", TileCamouflage.class);
    //ICBMClassic.blockMissileCoordinator = ICBMClassic.INSTANCE.getManager().newBlock("icbmCMissileCoordinator", new TileMissileCoordinator());

    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ICBMConstants.DOMAIN);

    public static final RegistryObject<TileEntityType<TileCruiseLauncher>> CRUISE_LAUNCHER_TILE = TILES.register(
            "cruiseLauncher",
            () -> TileEntityType.Builder.create(TileCruiseLauncher::new, CRUISE_LAUNCHER.get()).build(null)
    );
    public static final RegistryObject<TileEntityType<TileEMPTower>> EMP_TOWER_TILE = TILES.register(
            "emptower",
            () -> TileEntityType.Builder.create(TileEMPTower::new, EMP_TOWER.get()).build(null)
    );
    // TODO Implement Data Fixer
    public static final RegistryObject<TileEntityType<TileEntityExplosive>> EXPLOSIVES_TILE = TILES.register(
            "explosives",
            () -> TileEntityType.Builder.create(TileEntityExplosive::new, EXPLOSIVES.get()).build(null)
    );
    public static final RegistryObject<TileEntityType<TileLauncherBase>> LAUNCHER_BASE_TILE = TILES.register(
            "launcherbase",
            () -> TileEntityType.Builder.create(TileLauncherBase::new, LAUNCHER_BASE.get()).build(null)
    );
    public static final RegistryObject<TileEntityType<TileLauncherScreen>> LAUNCHER_SCREEN_TILE = TILES.register(
            "launcherscreen",
            () -> TileEntityType.Builder.create(TileLauncherScreen::new, LAUNCHER_SCREEN.get()).build(null)
    );
    public static final RegistryObject<TileEntityType<TileLauncherFrame>> LAUNCHER_FRAME_TILE = TILES.register(
            "launcherframe",
            () -> TileEntityType.Builder.create(TileLauncherFrame::new, LAUNCHER_FRAME.get()).build(null)
    );
    public static final RegistryObject<TileEntityType<TileMulti>> MULTIBLOCK_TILE = TILES.register(
            "multiblock",
            () -> TileEntityType.Builder.create(TileMulti::new, MULTIBLOCK.get()).build(null)
    );
    // TODO Implement Data Fixer
    public static final RegistryObject<TileEntityType<TileRadarStation>> RADAR_STATION_TILE = TILES.register(
            "radarStation",
            () -> TileEntityType.Builder.create(TileRadarStation::new, RADAR_STATION.get()).build(null)
    );

}
