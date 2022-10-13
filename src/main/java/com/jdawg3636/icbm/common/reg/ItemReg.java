package com.jdawg3636.icbm.common.reg;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.item.*;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = ICBMReference.MODID)
public class ItemReg {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ICBMReference.MODID);

    // Basic Blocks
    public static final RegistryObject<Item> CONCRETE                       = ITEMS.register("concrete",                        () -> new BlockItem(BlockReg.CONCRETE.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> CONCRETE_COMPACT               = ITEMS.register("concrete_compact",                () -> new BlockItem(BlockReg.CONCRETE_COMPACT.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> CONCRETE_REINFORCED            = ITEMS.register("concrete_reinforced",             () -> new BlockItem(BlockReg.CONCRETE_REINFORCED.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> REINFORCED_GLASS               = ITEMS.register("reinforced_glass",                () -> new BlockItem(BlockReg.REINFORCED_GLASS.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> RADIOACTIVE_MATERIAL           = ITEMS.register("radioactive_material",            () -> new BlockItem(BlockReg.RADIOACTIVE_MATERIAL.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));

    // Ores
    public static final RegistryObject<Item> ORE_COPPER                     = ITEMS.register("ore_copper",                      () -> new BlockItem(BlockReg.ORE_COPPER.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> ORE_SULFUR                     = ITEMS.register("ore_sulfur",                      () -> new BlockItem(BlockReg.ORE_SULFUR.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> ORE_TIN                        = ITEMS.register("ore_tin",                         () -> new BlockItem(BlockReg.ORE_TIN.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> ORE_URANIUM                    = ITEMS.register("ore_uranium",                     () -> new BlockItem(BlockReg.ORE_URANIUM.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));

    // Explosives
    public static final RegistryObject<Item> EXPLOSIVES_CONDENSED           = ITEMS.register("explosives_condensed",            () -> new BlockItem(BlockReg.EXPLOSIVES_CONDENSED.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_SHRAPNEL            = ITEMS.register("explosives_shrapnel",             () -> new BlockItem(BlockReg.EXPLOSIVES_SHRAPNEL.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_INCENDIARY          = ITEMS.register("explosives_incendiary",           () -> new BlockItem(BlockReg.EXPLOSIVES_INCENDIARY.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_DEBILITATION        = ITEMS.register("explosives_debilitation",         () -> new BlockItem(BlockReg.EXPLOSIVES_DEBILITATION.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_CHEMICAL            = ITEMS.register("explosives_chemical",             () -> new BlockItem(BlockReg.EXPLOSIVES_CHEMICAL.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_ANVIL               = ITEMS.register("explosives_anvil",                () -> new BlockItem(BlockReg.EXPLOSIVES_ANVIL.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_REPULSIVE           = ITEMS.register("explosives_repulsive",            () -> new BlockItem(BlockReg.EXPLOSIVES_REPULSIVE.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_ATTRACTIVE          = ITEMS.register("explosives_attractive",           () -> new BlockItem(BlockReg.EXPLOSIVES_ATTRACTIVE.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_NIGHTMARE           = ITEMS.register("explosives_nightmare",            () -> new BlockItem(BlockReg.EXPLOSIVES_NIGHTMARE.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_FRAGMENTATION       = ITEMS.register("explosives_fragmentation",        () -> new BlockItem(BlockReg.EXPLOSIVES_FRAGMENTATION.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_CONTAGION           = ITEMS.register("explosives_contagion",            () -> new BlockItem(BlockReg.EXPLOSIVES_CONTAGION.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_SONIC               = ITEMS.register("explosives_sonic",                () -> new BlockItem(BlockReg.EXPLOSIVES_SONIC.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_BREACHING           = ITEMS.register("explosives_breaching",            () -> new BlockItem(BlockReg.EXPLOSIVES_BREACHING.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_REJUVENATION        = ITEMS.register("explosives_rejuvenation",         () -> new BlockItem(BlockReg.EXPLOSIVES_REJUVENATION.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_THERMOBARIC         = ITEMS.register("explosives_thermobaric",          () -> new BlockItem(BlockReg.EXPLOSIVES_THERMOBARIC.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_NUCLEAR             = ITEMS.register("explosives_nuclear",              () -> new BlockItem(BlockReg.EXPLOSIVES_NUCLEAR.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_EMP                 = ITEMS.register("explosives_emp",                  () -> new BlockItem(BlockReg.EXPLOSIVES_EMP.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_EXOTHERMIC          = ITEMS.register("explosives_exothermic",           () -> new BlockItem(BlockReg.EXPLOSIVES_EXOTHERMIC.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_ENDOTHERMIC         = ITEMS.register("explosives_endothermic",          () -> new BlockItem(BlockReg.EXPLOSIVES_ENDOTHERMIC.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_ANTIGRAVITATIONAL   = ITEMS.register("explosives_antigravitational",    () -> new BlockItem(BlockReg.EXPLOSIVES_ANTIGRAVITATIONAL.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_ENDER               = ITEMS.register("explosives_ender",                () -> new BlockItem(BlockReg.EXPLOSIVES_ENDER.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_HYPERSONIC          = ITEMS.register("explosives_hypersonic",           () -> new BlockItem(BlockReg.EXPLOSIVES_HYPERSONIC.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_ANTIMATTER          = ITEMS.register("explosives_antimatter",           () -> new BlockItem(BlockReg.EXPLOSIVES_ANTIMATTER.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_REDMATTER           = ITEMS.register("explosives_redmatter",            () -> new BlockItem(BlockReg.EXPLOSIVES_REDMATTER.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));

    // Unconventional Explosives
    public static final RegistryObject<Item> S_MINE                         = ITEMS.register("s_mine",                          () -> new BlockItem(BlockReg.S_MINE.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));

    // Missiles
    public static final RegistryObject<Item> MISSILE_MODULE                 = ITEMS.register("missile_module",                  () -> new ItemMissile(EntityReg.MISSILE_MODULE));
    public static final RegistryObject<Item> MISSILE_CONVENTIONAL           = ITEMS.register("missile_conventional",            () -> new ItemMissile(EntityReg.MISSILE_CONVENTIONAL));
    public static final RegistryObject<Item> MISSILE_SHRAPNEL               = ITEMS.register("missile_shrapnel",                () -> new ItemMissile(EntityReg.MISSILE_SHRAPNEL));
    public static final RegistryObject<Item> MISSILE_INCENDIARY             = ITEMS.register("missile_incendiary",              () -> new ItemMissile(EntityReg.MISSILE_INCENDIARY));
    public static final RegistryObject<Item> MISSILE_DEBILITATION           = ITEMS.register("missile_debilitation",            () -> new ItemMissile(EntityReg.MISSILE_DEBILITATION));
    public static final RegistryObject<Item> MISSILE_CHEMICAL               = ITEMS.register("missile_chemical",                () -> new ItemMissile(EntityReg.MISSILE_CHEMICAL));
    public static final RegistryObject<Item> MISSILE_ANVIL                  = ITEMS.register("missile_anvil",                   () -> new ItemMissile(EntityReg.MISSILE_ANVIL));
    public static final RegistryObject<Item> MISSILE_REPULSIVE              = ITEMS.register("missile_repulsive",               () -> new ItemMissile(EntityReg.MISSILE_REPULSIVE));
    public static final RegistryObject<Item> MISSILE_ATTRACTIVE             = ITEMS.register("missile_attractive",              () -> new ItemMissile(EntityReg.MISSILE_ATTRACTIVE));
    public static final RegistryObject<Item> MISSILE_NIGHTMARE              = ITEMS.register("missile_nightmare",               () -> new ItemMissile(EntityReg.MISSILE_NIGHTMARE));
    public static final RegistryObject<Item> MISSILE_FRAGMENTATION          = ITEMS.register("missile_fragmentation",           () -> new ItemMissile(EntityReg.MISSILE_FRAGMENTATION));
    public static final RegistryObject<Item> MISSILE_CONTAGION              = ITEMS.register("missile_contagion",               () -> new ItemMissile(EntityReg.MISSILE_CONTAGION));
    public static final RegistryObject<Item> MISSILE_SONIC                  = ITEMS.register("missile_sonic",                   () -> new ItemMissile(EntityReg.MISSILE_SONIC));
    public static final RegistryObject<Item> MISSILE_BREACHING              = ITEMS.register("missile_breaching",               () -> new ItemMissile(EntityReg.MISSILE_BREACHING));
    public static final RegistryObject<Item> MISSILE_REJUVENATION           = ITEMS.register("missile_rejuvenation",            () -> new ItemMissile(EntityReg.MISSILE_REJUVENATION));
    public static final RegistryObject<Item> MISSILE_THERMOBARIC            = ITEMS.register("missile_thermobaric",             () -> new ItemMissile(EntityReg.MISSILE_THERMOBARIC));
    public static final RegistryObject<Item> MISSILE_NUCLEAR                = ITEMS.register("missile_nuclear",                 () -> new ItemMissile(EntityReg.MISSILE_NUCLEAR));
    public static final RegistryObject<Item> MISSILE_EMP                    = ITEMS.register("missile_emp",                     () -> new ItemMissile(EntityReg.MISSILE_EMP));
    public static final RegistryObject<Item> MISSILE_EXOTHERMIC             = ITEMS.register("missile_exothermic",              () -> new ItemMissile(EntityReg.MISSILE_EXOTHERMIC));
    public static final RegistryObject<Item> MISSILE_ENDOTHERMIC            = ITEMS.register("missile_endothermic",             () -> new ItemMissile(EntityReg.MISSILE_ENDOTHERMIC));
    public static final RegistryObject<Item> MISSILE_ANTIGRAVITATIONAL      = ITEMS.register("missile_antigravitational",       () -> new ItemMissile(EntityReg.MISSILE_ANTIGRAVITATIONAL));
    public static final RegistryObject<Item> MISSILE_ENDER                  = ITEMS.register("missile_ender",                   () -> new ItemMissile(EntityReg.MISSILE_ENDER));
    public static final RegistryObject<Item> MISSILE_HYPERSONIC             = ITEMS.register("missile_hypersonic",              () -> new ItemMissile(EntityReg.MISSILE_HYPERSONIC));
    public static final RegistryObject<Item> MISSILE_ANTIMATTER             = ITEMS.register("missile_antimatter",              () -> new ItemMissile(EntityReg.MISSILE_ANTIMATTER));
    public static final RegistryObject<Item> MISSILE_REDMATTER              = ITEMS.register("missile_redmatter",               () -> new ItemMissile(EntityReg.MISSILE_REDMATTER));
    public static final RegistryObject<Item> MISSILE_HOMING                 = ITEMS.register("missile_homing",                  () -> new ItemMissile(EntityReg.MISSILE_HOMING));
    public static final RegistryObject<Item> MISSILE_ANTIBALLISTIC          = ITEMS.register("missile_antiballistic",           () -> new ItemMissile(EntityReg.MISSILE_ANTIBALLISTIC));
    public static final RegistryObject<Item> MISSILE_CLUSTER                = ITEMS.register("missile_cluster",                 () -> new ItemMissile(EntityReg.MISSILE_CLUSTER));
    public static final RegistryObject<Item> MISSILE_CLUSTER_NUCLEAR        = ITEMS.register("missile_cluster_nuclear",         () -> new ItemMissile(EntityReg.MISSILE_CLUSTER_NUCLEAR));

    // Minecarts
    public static final RegistryObject<Item> MINECART_EXPLOSIVE             = ITEMS.register("minecart_explosive",              () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_EXPLOSIVE));
    public static final RegistryObject<Item> MINECART_SHRAPNEL              = ITEMS.register("minecart_shrapnel",               () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_SHRAPNEL));
    public static final RegistryObject<Item> MINECART_INCENDIARY            = ITEMS.register("minecart_incendiary",             () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_INCENDIARY));
    public static final RegistryObject<Item> MINECART_DEBILITATION          = ITEMS.register("minecart_debilitation",           () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_DEBILITATION));
    public static final RegistryObject<Item> MINECART_CHEMICAL              = ITEMS.register("minecart_chemical",               () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_CHEMICAL));
    public static final RegistryObject<Item> MINECART_ANVIL                 = ITEMS.register("minecart_anvil",                  () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_ANVIL));
    public static final RegistryObject<Item> MINECART_REPULSIVE             = ITEMS.register("minecart_repulsive",              () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_REPULSIVE));
    public static final RegistryObject<Item> MINECART_ATTRACTIVE            = ITEMS.register("minecart_attractive",             () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_ATTRACTIVE));
    public static final RegistryObject<Item> MINECART_NIGHTMARE             = ITEMS.register("minecart_nightmare",              () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_NIGHTMARE));
    public static final RegistryObject<Item> MINECART_FRAGMENTATION         = ITEMS.register("minecart_fragmentation",          () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_FRAGMENTATION));
    public static final RegistryObject<Item> MINECART_CONTAGION             = ITEMS.register("minecart_contagion",              () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_CONTAGION));
    public static final RegistryObject<Item> MINECART_SONIC                 = ITEMS.register("minecart_sonic",                  () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_SONIC));
    public static final RegistryObject<Item> MINECART_BREACHING             = ITEMS.register("minecart_breaching",              () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_BREACHING));
    public static final RegistryObject<Item> MINECART_REJUVENATION          = ITEMS.register("minecart_rejuvenation",           () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_REJUVENATION));
    public static final RegistryObject<Item> MINECART_THERMOBARIC           = ITEMS.register("minecart_thermobaric",            () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_THERMOBARIC));
    public static final RegistryObject<Item> MINECART_NUCLEAR               = ITEMS.register("minecart_nuclear",                () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_NUCLEAR));
    public static final RegistryObject<Item> MINECART_EMP                   = ITEMS.register("minecart_emp",                    () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_EMP));
    public static final RegistryObject<Item> MINECART_EXOTHERMIC            = ITEMS.register("minecart_exothermic",             () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_EXOTHERMIC));
    public static final RegistryObject<Item> MINECART_ENDOTHERMIC           = ITEMS.register("minecart_endothermic",            () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_ENDOTHERMIC));
    public static final RegistryObject<Item> MINECART_ANTIGRAVITATIONAL     = ITEMS.register("minecart_antigravitational",      () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_ANTIGRAVITATIONAL));
    public static final RegistryObject<Item> MINECART_ENDER                 = ITEMS.register("minecart_ender",                  () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_ENDER));
    public static final RegistryObject<Item> MINECART_HYPERSONIC            = ITEMS.register("minecart_hypersonic",             () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_HYPERSONIC));
    public static final RegistryObject<Item> MINECART_ANTIMATTER            = ITEMS.register("minecart_antimatter",             () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_ANTIMATTER));
    public static final RegistryObject<Item> MINECART_REDMATTER             = ITEMS.register("minecart_redmatter",              () -> new ItemMinecartExplosives((new Item.Properties()).stacksTo(1).tab(ICBMReference.CREATIVE_TAB), EntityReg.MINECART_REDMATTER));

    // Grenades
    public static final RegistryObject<Item> GRENADE_CONVENTIONAL           = ITEMS.register("grenade_conventional",            () -> new ItemGrenade(EntityReg.GRENADE_CONVENTIONAL));
    public static final RegistryObject<Item> GRENADE_SHRAPNEL               = ITEMS.register("grenade_shrapnel",                () -> new ItemGrenade(EntityReg.GRENADE_SHRAPNEL));
    public static final RegistryObject<Item> GRENADE_INCENDIARY             = ITEMS.register("grenade_incendiary",              () -> new ItemGrenade(EntityReg.GRENADE_INCENDIARY));
    public static final RegistryObject<Item> GRENADE_DEBILITATION           = ITEMS.register("grenade_debilitation",            () -> new ItemGrenade(EntityReg.GRENADE_DEBILITATION));
    public static final RegistryObject<Item> GRENADE_CHEMICAL               = ITEMS.register("grenade_chemical",                () -> new ItemGrenade(EntityReg.GRENADE_CHEMICAL));
    public static final RegistryObject<Item> GRENADE_ANVIL                  = ITEMS.register("grenade_anvil",                   () -> new ItemGrenade(EntityReg.GRENADE_ANVIL));
    public static final RegistryObject<Item> GRENADE_REPULSIVE              = ITEMS.register("grenade_repulsive",               () -> new ItemGrenade(EntityReg.GRENADE_REPULSIVE));
    public static final RegistryObject<Item> GRENADE_ATTRACTIVE             = ITEMS.register("grenade_attractive",              () -> new ItemGrenade(EntityReg.GRENADE_ATTRACTIVE));

    // Spikes
    public static final RegistryObject<Item> SPIKES                         = ITEMS.register("spikes",                          () -> new BlockItem(BlockReg.SPIKES.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> SPIKES_POISON                  = ITEMS.register("spikes_poison",                   () -> new BlockItem(BlockReg.SPIKES_POISON.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> SPIKES_FIRE                    = ITEMS.register("spikes_fire",                     () -> new BlockItem(BlockReg.SPIKES_FIRE.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));

    // Missile Launch Apparatus
    public static final RegistryObject<Item> LAUNCHER_PLATFORM_T1           = ITEMS.register("launcher_platform_t1",            () -> new BlockItem(BlockReg.LAUNCHER_PLATFORM_T1.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> LAUNCHER_PLATFORM_T2           = ITEMS.register("launcher_platform_t2",            () -> new BlockItem(BlockReg.LAUNCHER_PLATFORM_T2.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> LAUNCHER_PLATFORM_T3           = ITEMS.register("launcher_platform_t3",            () -> new BlockItem(BlockReg.LAUNCHER_PLATFORM_T3.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> LAUNCHER_CONTROL_PANEL_T1      = ITEMS.register("launcher_control_panel_t1",       () -> new BlockItem(BlockReg.LAUNCHER_CONTROL_PANEL_T1.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> LAUNCHER_CONTROL_PANEL_T2      = ITEMS.register("launcher_control_panel_t2",       () -> new BlockItem(BlockReg.LAUNCHER_CONTROL_PANEL_T2.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> LAUNCHER_CONTROL_PANEL_T3      = ITEMS.register("launcher_control_panel_t3",       () -> new BlockItem(BlockReg.LAUNCHER_CONTROL_PANEL_T3.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> LAUNCHER_SUPPORT_FRAME_T1      = ITEMS.register("launcher_support_frame_t1",       () -> new BlockItem(BlockReg.LAUNCHER_SUPPORT_FRAME_T1.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> LAUNCHER_SUPPORT_FRAME_T2      = ITEMS.register("launcher_support_frame_t2",       () -> new BlockItem(BlockReg.LAUNCHER_SUPPORT_FRAME_T2.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> LAUNCHER_SUPPORT_FRAME_T3      = ITEMS.register("launcher_support_frame_t3",       () -> new BlockItem(BlockReg.LAUNCHER_SUPPORT_FRAME_T3.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));

    // Other Machinery
    public static final RegistryObject<Item> COAL_GENERATOR                 = ITEMS.register("coal_generator",                  () -> new BlockItem(BlockReg.COAL_GENERATOR.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> CRUISE_LAUNCHER                = ITEMS.register("cruise_launcher",                 () -> new BlockItem(BlockReg.CRUISE_LAUNCHER.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> EMP_TOWER                      = ITEMS.register("emp_tower",                       () -> new BlockItem(BlockReg.EMP_TOWER.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> RADAR_STATION                  = ITEMS.register("radar_station",                   () -> new BlockItem(BlockReg.RADAR_STATION.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));

    // Particle Accelerator Components (Deliberately Non-Alphabetized so that the Particle Accelerator itself will appear first in the Creative Inventory)
    public static final RegistryObject<Item> PARTICLE_ACCELERATOR           = ITEMS.register("particle_accelerator",            () -> new BlockItem(BlockReg.PARTICLE_ACCELERATOR.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> ELECTROMAGNET                  = ITEMS.register("electromagnet",                   () -> new BlockItem(BlockReg.ELECTROMAGNET.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> ELECTROMAGNETIC_GLASS          = ITEMS.register("electromagnetic_glass",           () -> new BlockItem(BlockReg.ELECTROMAGNETIC_GLASS.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));

    // Functional Items
    public static final RegistryObject<Item> BATTERY                        = ITEMS.register("battery",                         () -> new Item(new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> DEFUSER                        = ITEMS.register("defuser",                         ItemDefuser::new);
    public static final RegistryObject<Item> LASER_DESIGNATOR               = ITEMS.register("laser_designator",                ItemLaserDesignator::new);
    public static final RegistryObject<Item> RADAR_GUN                      = ITEMS.register("radar_gun",                       ItemRadarGun::new);
    public static final RegistryObject<Item> REMOTE_DETONATOR               = ITEMS.register("remote_detonator",                ItemRemoteDetonator::new);
    public static final RegistryObject<Item> ROCKET_LAUNCHER                = ITEMS.register("rocket_launcher",                 ItemRocketLauncher::new);
    public static final RegistryObject<Item> SCANNER                        = ITEMS.register("scanner",                         ItemScanner::new);
    public static final RegistryObject<Item> SIGNAL_DISRUPTOR               = ITEMS.register("signal_disruptor",                () -> new Item(new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> TRACKER                        = ITEMS.register("tracker",                         ItemTracker::new);

    // Basic Crafting Items (No functionality)
    public static final RegistryObject<Item> POISON_POWDER                  = ITEMS.register("poison_powder",                   () -> new Item(new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> SULFUR                         = ITEMS.register("sulfur",                          () -> new Item(new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> COPPER_WIRE                    = ITEMS.register("copper_wire",                     () -> new Item(new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> CIRCUIT_BASIC                  = ITEMS.register("circuit_basic",                   () -> new Item(new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> CIRCUIT_ADVANCED               = ITEMS.register("circuit_advanced",                () -> new Item(new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> CIRCUIT_ELITE                  = ITEMS.register("circuit_elite",                   () -> new Item(new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> DUST_BRONZE                    = ITEMS.register("dust_bronze",                     () -> new Item(new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> DUST_STEEL                     = ITEMS.register("dust_steel",                      () -> new Item(new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> ELECTRIC_MOTOR                 = ITEMS.register("electric_motor",                  () -> new Item(new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> INGOT_BRONZE                   = ITEMS.register("ingot_bronze",                    () -> new Item(new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> INGOT_COPPER                   = ITEMS.register("ingot_copper",                    () -> new Item(new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> INGOT_STEEL                    = ITEMS.register("ingot_steel",                     () -> new Item(new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> INGOT_TIN                      = ITEMS.register("ingot_tin",                       () -> new Item(new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> INGOT_URANIUM                  = ITEMS.register("ingot_uranium",                   () -> new Item(new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> PLATE_BRONZE                   = ITEMS.register("plate_bronze",                    () -> new Item(new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> PLATE_STEEL                    = ITEMS.register("plate_steel",                     () -> new Item(new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));

    // Extras
    public static final RegistryObject<Item> ANTIDOTE                       = ITEMS.register("antidote",                        () -> new Item(new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> GLASS_BUTTON                   = ITEMS.register("glass_button",                    () -> new BlockItem(BlockReg.GLASS_BUTTON.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));
    public static final RegistryObject<Item> GLASS_PRESSURE_PLATE           = ITEMS.register("glass_pressure_plate",            () -> new BlockItem(BlockReg.GLASS_PRESSURE_PLATE.get(), new Item.Properties().tab(ICBMReference.CREATIVE_TAB)));

    // Armor
    public static final RegistryObject<Item> HAZMAT_MASK    = ITEMS.register("hazmat_mask",   () -> new ItemHazmatArmor(EquipmentSlotType.HEAD));
    public static final RegistryObject<Item> HAZMAT_JACKET  = ITEMS.register("hazmat_jacket", () -> new ItemHazmatArmor(EquipmentSlotType.CHEST));
    public static final RegistryObject<Item> HAZMAT_PANTS   = ITEMS.register("hazmat_pants",  () -> new ItemHazmatArmor(EquipmentSlotType.LEGS));
    public static final RegistryObject<Item> HAZMAT_BOOTS   = ITEMS.register("hazmat_boots",  () -> new ItemHazmatArmor(EquipmentSlotType.FEET));

}
