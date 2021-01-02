package icbm.common.reg;

import icbm.ICBM;
import icbm.ICBMReference;
//TODO//import icbm.classic.config.ConfigItems;
//TODO//import icbm.content.blocks.explosive.BlockItemExplosive;
//TODO//import icbm.content.blocks.launcher.base.TileLauncherBase;
//TODO//import icbm.content.items.ItemAntidote;
//TODO//import icbm.content.items.ItemBattery;
//TODO//import icbm.content.items.ItemBombCart;
//TODO//import icbm.content.items.ItemCrafting;
//TODO//import icbm.content.items.ItemDefuser;
//TODO//import icbm.content.items.ItemGrenade;
//TODO//import icbm.content.items.ItemLaserDetonator;
//TODO//import icbm.content.items.ItemMissile;
//TODO//import icbm.content.items.ItemRadarGun;
//TODO//import icbm.content.items.ItemRemoteDetonator;
//TODO//import icbm.content.items.ItemRocketLauncher;
//TODO//import icbm.content.items.ItemSignalDisrupter;
//TODO//import icbm.content.items.ItemTracker;
//TODO//import icbm.classic.prefab.item.ItemBase;
//TODO//import icbm.classic.prefab.item.BlockItemRotatedMultiTile;
//TODO//import icbm.classic.prefab.item.BlockItemSubTypes;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Created by Dark(DarkGuardsman, Robert) on 1/7/19.
 */
@Mod.EventBusSubscriber(modid = ICBMReference.MODID)
public class ItemReg {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ICBMReference.MODID);

    // Basic Blocks
    public static final RegistryObject<Item> CONCRETE                       = ITEMS.register("concrete",                        () -> new BlockItem(BlockReg.CONCRETE.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> CONCRETE_COMPACT               = ITEMS.register("concrete_compact",                () -> new BlockItem(BlockReg.CONCRETE_COMPACT.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> CONCRETE_REINFORCED            = ITEMS.register("concrete_reinforced",             () -> new BlockItem(BlockReg.CONCRETE_REINFORCED.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> REINFORCED_GLASS               = ITEMS.register("reinforced_glass",                () -> new BlockItem(BlockReg.REINFORCED_GLASS.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));

    // Explosives
    public static final RegistryObject<Item> EXPLOSIVES_CONDENSED           = ITEMS.register("explosives_condensed",            () -> new BlockItem(BlockReg.EXPLOSIVES_CONDENSED.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_SHRAPNEL            = ITEMS.register("explosives_shrapnel",             () -> new BlockItem(BlockReg.EXPLOSIVES_SHRAPNEL.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_INCENDIARY          = ITEMS.register("explosives_incendiary",           () -> new BlockItem(BlockReg.EXPLOSIVES_INCENDIARY.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_DEBILITATION        = ITEMS.register("explosives_debilitation",         () -> new BlockItem(BlockReg.EXPLOSIVES_DEBILITATION.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_CHEMICAL            = ITEMS.register("explosives_chemical",             () -> new BlockItem(BlockReg.EXPLOSIVES_CHEMICAL.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_ANVIL               = ITEMS.register("explosives_anvil",                () -> new BlockItem(BlockReg.EXPLOSIVES_ANVIL.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_REPULSIVE           = ITEMS.register("explosives_repulsive",            () -> new BlockItem(BlockReg.EXPLOSIVES_REPULSIVE.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_ATTRACTIVE          = ITEMS.register("explosives_attractive",           () -> new BlockItem(BlockReg.EXPLOSIVES_ATTRACTIVE.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_NIGHTMARE           = ITEMS.register("explosives_nightmare",            () -> new BlockItem(BlockReg.EXPLOSIVES_NIGHTMARE.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_FRAGMENTATION       = ITEMS.register("explosives_fragmentation",        () -> new BlockItem(BlockReg.EXPLOSIVES_FRAGMENTATION.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_CONTAGIOUS          = ITEMS.register("explosives_contagious",           () -> new BlockItem(BlockReg.EXPLOSIVES_CONTAGIOUS.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_SONIC               = ITEMS.register("explosives_sonic",                () -> new BlockItem(BlockReg.EXPLOSIVES_SONIC.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_BREACHING           = ITEMS.register("explosives_breaching",            () -> new BlockItem(BlockReg.EXPLOSIVES_BREACHING.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_REJUVENATION        = ITEMS.register("explosives_rejuvenation",         () -> new BlockItem(BlockReg.EXPLOSIVES_REJUVENATION.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_THERMOBARIC         = ITEMS.register("explosives_thermobaric",          () -> new BlockItem(BlockReg.EXPLOSIVES_THERMOBARIC.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_NUCLEAR             = ITEMS.register("explosives_nuclear",              () -> new BlockItem(BlockReg.EXPLOSIVES_NUCLEAR.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_EMP                 = ITEMS.register("explosives_emp",                  () -> new BlockItem(BlockReg.EXPLOSIVES_EMP.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_EXOTHERMIC          = ITEMS.register("explosives_exothermic",           () -> new BlockItem(BlockReg.EXPLOSIVES_EXOTHERMIC.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_ENDOTHERMIC         = ITEMS.register("explosives_endothermic",          () -> new BlockItem(BlockReg.EXPLOSIVES_ENDOTHERMIC.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_ANTIGRAVITATIONAL   = ITEMS.register("explosives_antigravitational",    () -> new BlockItem(BlockReg.EXPLOSIVES_ANTIGRAVITATIONAL.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_ENDER               = ITEMS.register("explosives_ender",                () -> new BlockItem(BlockReg.EXPLOSIVES_ENDER.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_HYPERSONIC          = ITEMS.register("explosives_hypersonic",           () -> new BlockItem(BlockReg.EXPLOSIVES_HYPERSONIC.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_ANTIMATTER          = ITEMS.register("explosives_antimatter",           () -> new BlockItem(BlockReg.EXPLOSIVES_ANTIMATTER.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_REDMATTER           = ITEMS.register("explosives_redmatter",            () -> new BlockItem(BlockReg.EXPLOSIVES_REDMATTER.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));

    // Unconventional Explosives
    public static final RegistryObject<Item> S_MINE                         = ITEMS.register("s_mine",                          () -> new BlockItem(BlockReg.S_MINE.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));

    // Missile Launch Apparatus
    public static final RegistryObject<Item> LAUNCHER_PLATFORM_T1           = ITEMS.register("launcher_platform_t1",            () -> new BlockItem(BlockReg.LAUNCHER_PLATFORM_T1.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> LAUNCHER_PLATFORM_T2           = ITEMS.register("launcher_platform_t2",            () -> new BlockItem(BlockReg.LAUNCHER_PLATFORM_T2.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> LAUNCHER_PLATFORM_T3           = ITEMS.register("launcher_platform_t3",            () -> new BlockItem(BlockReg.LAUNCHER_PLATFORM_T3.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> LAUNCHER_CONTROL_PANEL_T1      = ITEMS.register("launcher_control_panel_t1",       () -> new BlockItem(BlockReg.LAUNCHER_CONTROL_PANEL_T1.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> LAUNCHER_CONTROL_PANEL_T2      = ITEMS.register("launcher_control_panel_t2",       () -> new BlockItem(BlockReg.LAUNCHER_CONTROL_PANEL_T2.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> LAUNCHER_CONTROL_PANEL_T3      = ITEMS.register("launcher_control_panel_t3",       () -> new BlockItem(BlockReg.LAUNCHER_CONTROL_PANEL_T3.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> LAUNCHER_SUPPORT_FRAME_T1      = ITEMS.register("launcher_support_frame_t1",       () -> new BlockItem(BlockReg.LAUNCHER_SUPPORT_FRAME_T1.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> LAUNCHER_SUPPORT_FRAME_T2      = ITEMS.register("launcher_support_frame_t2",       () -> new BlockItem(BlockReg.LAUNCHER_SUPPORT_FRAME_T2.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> LAUNCHER_SUPPORT_FRAME_T3      = ITEMS.register("launcher_support_frame_t3",       () -> new BlockItem(BlockReg.LAUNCHER_SUPPORT_FRAME_T3.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));

    // Other Machinery
    public static final RegistryObject<Item> CRUISE_LAUNCHER                = ITEMS.register("cruise_launcher",                 () -> new BlockItem(BlockReg.CRUISE_LAUNCHER.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EMP_TOWER                      = ITEMS.register("emp_tower",                       () -> new BlockItem(BlockReg.EMP_TOWER.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> RADAR_STATION                  = ITEMS.register("radar_station",                   () -> new BlockItem(BlockReg.RADAR_STATION.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));

    // Spikes
    public static final RegistryObject<Item> SPIKES                         = ITEMS.register("spikes",                          () -> new BlockItem(BlockReg.SPIKES.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> SPIKES_POISON                  = ITEMS.register("spikes_poison",                   () -> new BlockItem(BlockReg.SPIKES_POISON.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> SPIKES_FIRE                    = ITEMS.register("spikes_fire",                     () -> new BlockItem(BlockReg.SPIKES_FIRE.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));

    // Ores
    public static final RegistryObject<Item> COPPER_ORE                     = ITEMS.register("ore_copper",                      () -> new BlockItem(BlockReg.ORE_COPPER.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> SULFUR_ORE                     = ITEMS.register("ore_sulfur",                      () -> new BlockItem(BlockReg.ORE_SULFUR.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> TIN_ORE                        = ITEMS.register("ore_tin",                         () -> new BlockItem(BlockReg.ORE_TIN.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));

    // Extras
    public static final RegistryObject<Item> GLASS_BUTTON                   = ITEMS.register("glass_button",                    () -> new BlockItem(BlockReg.GLASS_BUTTON.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> GLASS_PRESSURE_PLATE           = ITEMS.register("glass_pressure_plate",            () -> new BlockItem(BlockReg.GLASS_PRESSURE_PLATE.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));

}
