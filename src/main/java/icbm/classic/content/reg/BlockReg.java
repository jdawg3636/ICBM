package icbm.classic.content.reg;

import icbm.classic.ICBMConstants;
import icbm.classic.content.blocks.*;
//TODO//import icbm.classic.content.blocks.emptower.BlockEmpTower;
//TODO//import icbm.classic.content.blocks.emptower.TileEMPTower;
//TODO//import icbm.classic.content.blocks.explosive.BlockExplosive;
//TODO//import icbm.classic.content.blocks.explosive.TileEntityExplosive;
//TODO//import icbm.classic.content.blocks.launcher.base.BlockLauncherBase;
//TODO//import icbm.classic.content.blocks.launcher.base.TileLauncherBase;
//TODO//import icbm.classic.content.blocks.launcher.cruise.BlockCruiseLauncher;
//TODO//import icbm.classic.content.blocks.launcher.cruise.TileCruiseLauncher;
//TODO//import icbm.classic.content.blocks.launcher.frame.BlockLaunchFrame;
//TODO//import icbm.classic.content.blocks.launcher.frame.TileLauncherFrame;
//TODO//import icbm.classic.content.blocks.launcher.screen.BlockLaunchScreen;
//TODO//import icbm.classic.content.blocks.launcher.screen.TileLauncherScreen;
//TODO//import icbm.classic.content.blocks.multiblock.BlockMultiblock;
//TODO//import icbm.classic.content.blocks.multiblock.TileMulti;
//TODO//import icbm.classic.content.blocks.radarstation.BlockRadarStation;
//TODO//import icbm.classic.content.blocks.radarstation.TileRadarStation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockReg {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ICBMConstants.DOMAIN);

    public static final RegistryObject<Block> CONCRETE              = BLOCKS.register("concrete", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(10F, 28)));
    public static final RegistryObject<Block> CONCRETE_COMPACT      = BLOCKS.register("concrete_compact", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(10F, 280)));
    public static final RegistryObject<Block> CONCRETE_REINFORCED   = BLOCKS.register("concrete_reinforced", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(10F, 2800)));
    public static final RegistryObject<Block> GLASS_BUTTON          = BLOCKS.register("glass_button", BlockGlassButton::new);
    public static final RegistryObject<Block> GLASS_PRESSURE_PLATE  = BLOCKS.register("glass_pressure_plate", BlockGlassPressurePlate::new);
    public static final RegistryObject<Block> REINFORCED_GLASS      = BLOCKS.register("reinforced_glass", BlockReinforcedGlass::new);
    public static final RegistryObject<Block> SPIKES                = BLOCKS.register("spikes", BlockSpikes::new);
    public static final RegistryObject<Block> SPIKES_POISON         = BLOCKS.register("spikes_poison", () -> new BlockSpikes() {
        @Override
        public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
            super.onEntityCollision(state, world, pos, entity);
            if (entity instanceof LivingEntity)
                ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.POISON, 7 * 20, 0));
        }
    });
    public static final RegistryObject<Block> SPIKES_FIRE           = BLOCKS.register("spikes_fire", () -> new BlockSpikes() {
        @Override
        public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
            super.onEntityCollision(state, world, pos, entity);
            if (entity instanceof LivingEntity)
                entity.setFire(7);
        }
    });

    //TODO//public static final RegistryObject<Block> CRUISE_LAUNCHER       = BLOCKS.register("cruise_launcher",     BlockCruiseLauncher::new);
    //TODO//public static final RegistryObject<Block> EMP_TOWER             = BLOCKS.register("emp_tower",           BlockEmpTower::new);
    //TODO//public static final RegistryObject<Block> EXPLOSIVES            = BLOCKS.register("explosives",         BlockExplosive::new);
    //TODO//public static final RegistryObject<Block> LAUNCHER_BASE         = BLOCKS.register("launcher_base",       BlockLauncherBase::new);
    //TODO//public static final RegistryObject<Block> LAUNCHER_SCREEN       = BLOCKS.register("launcher_screen",     BlockLaunchScreen::new);
    //TODO//public static final RegistryObject<Block> LAUNCHER_FRAME        = BLOCKS.register("launcher_frame",      BlockLaunchFrame::new);
    //TODO//public static final RegistryObject<Block> MULTIBLOCK            = BLOCKS.register("multiblock",         BlockMultiblock::new);
    //TODO//public static final RegistryObject<Block> RADAR_STATION         = BLOCKS.register("radar_station",       BlockRadarStation::new);

    //public static Block blockCamo; //TODO re-implement
    //public static Block blockMissileCoordinator; //TODO re-implement
    //event.getRegistry().register(blockCombatRail = new BlockReinforcedRail());
    //blockCamo = manager.newBlock("icbmCCamouflage", TileCamouflage.class);
    //ICBMClassic.blockMissileCoordinator = ICBMClassic.INSTANCE.getManager().newBlock("icbmCMissileCoordinator", new TileMissileCoordinator());

    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ICBMConstants.DOMAIN);

    /*TODO
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
    */

}
