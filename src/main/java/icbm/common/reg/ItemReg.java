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

    public static final RegistryObject<Item> CONCRETE                     = ITEMS.register("concrete",                     () -> new BlockItem(BlockReg.CONCRETE.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> CONCRETE_COMPACT             = ITEMS.register("concrete_compact",             () -> new BlockItem(BlockReg.CONCRETE_COMPACT.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> CONCRETE_REINFORCED          = ITEMS.register("concrete_reinforced",          () -> new BlockItem(BlockReg.CONCRETE_REINFORCED.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> GLASS_BUTTON                 = ITEMS.register("glass_button",                 () -> new BlockItem(BlockReg.GLASS_BUTTON.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> GLASS_PRESSURE_PLATE         = ITEMS.register("glass_pressure_plate",         () -> new BlockItem(BlockReg.GLASS_PRESSURE_PLATE.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> REINFORCED_GLASS             = ITEMS.register("reinforced_glass",             () -> new BlockItem(BlockReg.REINFORCED_GLASS.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> SPIKES                       = ITEMS.register("spikes",                       () -> new BlockItem(BlockReg.SPIKES.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> SPIKES_POISON                = ITEMS.register("spikes_poison",                () -> new BlockItem(BlockReg.SPIKES_POISON.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> SPIKES_FIRE                  = ITEMS.register("spikes_fire",                  () -> new BlockItem(BlockReg.SPIKES_FIRE.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));

    public static final RegistryObject<Item> EXPLOSIVES_CONDENSED         = ITEMS.register("explosives_condensed",         () -> new BlockItem(BlockReg.EXPLOSIVES_CONDENSED.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_SHRAPNEL          = ITEMS.register("explosives_shrapnel",          () -> new BlockItem(BlockReg.EXPLOSIVES_SHRAPNEL.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_INCENDIARY        = ITEMS.register("explosives_incendiary",        () -> new BlockItem(BlockReg.EXPLOSIVES_INCENDIARY.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_DEBILITATION      = ITEMS.register("explosives_debilitation",      () -> new BlockItem(BlockReg.EXPLOSIVES_DEBILITATION.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_CHEMICAL          = ITEMS.register("explosives_chemical",          () -> new BlockItem(BlockReg.EXPLOSIVES_CHEMICAL.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_ANVIL             = ITEMS.register("explosives_anvil",             () -> new BlockItem(BlockReg.EXPLOSIVES_ANVIL.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_REPULSIVE         = ITEMS.register("explosives_repulsive",         () -> new BlockItem(BlockReg.EXPLOSIVES_REPULSIVE.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_ATTRACTIVE        = ITEMS.register("explosives_attractive",        () -> new BlockItem(BlockReg.EXPLOSIVES_ATTRACTIVE.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_FRAGMENTATION     = ITEMS.register("explosives_fragmentation",     () -> new BlockItem(BlockReg.EXPLOSIVES_FRAGMENTATION.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_CONTAGIOUS        = ITEMS.register("explosives_contagious",        () -> new BlockItem(BlockReg.EXPLOSIVES_CONTAGIOUS.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_SONIC             = ITEMS.register("explosives_sonic",             () -> new BlockItem(BlockReg.EXPLOSIVES_SONIC.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_BREACHING         = ITEMS.register("explosives_breaching",         () -> new BlockItem(BlockReg.EXPLOSIVES_BREACHING.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_REJUVENATION      = ITEMS.register("explosives_rejuvenation",      () -> new BlockItem(BlockReg.EXPLOSIVES_REJUVENATION.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_THERMOBARIC       = ITEMS.register("explosives_thermobaric",       () -> new BlockItem(BlockReg.EXPLOSIVES_THERMOBARIC.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_NUCLEAR           = ITEMS.register("explosives_nuclear",           () -> new BlockItem(BlockReg.EXPLOSIVES_NUCLEAR.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_EMP               = ITEMS.register("explosives_emp",               () -> new BlockItem(BlockReg.EXPLOSIVES_EMP.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_EXOTHERMIC        = ITEMS.register("explosives_exothermic",        () -> new BlockItem(BlockReg.EXPLOSIVES_EXOTHERMIC.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_ENDOTHERMIC       = ITEMS.register("explosives_endothermic",       () -> new BlockItem(BlockReg.EXPLOSIVES_ENDOTHERMIC.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_ANTIGRAVITATIONAL = ITEMS.register("explosives_antigravitational", () -> new BlockItem(BlockReg.EXPLOSIVES_ANTIGRAVITATIONAL.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_ENDER             = ITEMS.register("explosives_ender",             () -> new BlockItem(BlockReg.EXPLOSIVES_ENDER.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_HYPERSONIC        = ITEMS.register("explosives_hypersonic",        () -> new BlockItem(BlockReg.EXPLOSIVES_HYPERSONIC.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_ANTIMATTER        = ITEMS.register("explosives_antimatter",        () -> new BlockItem(BlockReg.EXPLOSIVES_ANTIMATTER.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_REDMATTER         = ITEMS.register("explosives_redmatter",         () -> new BlockItem(BlockReg.EXPLOSIVES_REDMATTER.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));
    public static final RegistryObject<Item> EXPLOSIVES_NIGHTMARE         = ITEMS.register("explosives_nightmare",         () -> new BlockItem(BlockReg.EXPLOSIVES_NIGHTMARE.get(), new Item.Properties().group(ICBM.CREATIVE_TAB)));

    /*
    @ObjectHolder(ICBMConstants.PREFIX + "antidote")
    public static Item itemAntidote;
    @ObjectHolder(ICBMConstants.PREFIX + "signal_disrupter")
    public static Item itemSignalDisrupter;
    @ObjectHolder(ICBMConstants.PREFIX + "tracker")
    public static Item itemTracker;
    @ObjectHolder(ICBMConstants.PREFIX + "missile")
    public static Item itemMissile;
    @ObjectHolder(ICBMConstants.PREFIX + "defuser")
    public static Item itemDefuser;
    @ObjectHolder(ICBMConstants.PREFIX + "radar_gun")
    public static Item itemRadarGun;
    @ObjectHolder(ICBMConstants.PREFIX + "remote_detonator")
    public static Item itemRemoteDetonator;
    @ObjectHolder(ICBMConstants.PREFIX + "laser_detonator")
    public static Item itemLaserDetonator;
    @ObjectHolder(ICBMConstants.PREFIX + "rocket_launcher")
    public static Item itemRocketLauncher;
    @ObjectHolder(ICBMConstants.PREFIX + "grenade")
    public static Item itemGrenade;
    @ObjectHolder(ICBMConstants.PREFIX + "bombcart")
    public static Item itemBombCart;
    @ObjectHolder(ICBMConstants.PREFIX + "sulfur_dust")
    public static Item itemSulfurDust;
    @ObjectHolder(ICBMConstants.PREFIX + "saltpeter")
    public static Item itemSaltpeterDust;
    @ObjectHolder(ICBMConstants.PREFIX + "poison_powder")
    public static Item itemPoisonPowder;
    @ObjectHolder(ICBMConstants.PREFIX + "battery")
    public static Item itemBattery;
    //TODO//@ObjectHolder(ICBMConstants.PREFIX + "ingot")
    //TODO//public static ItemCrafting itemIngot;
    //TODO//@ObjectHolder(ICBMConstants.PREFIX + "clump")
    //TODO//public static ItemCrafting itemIngotClump;
    //TODO//@ObjectHolder(ICBMConstants.PREFIX + "plate")
    //TODO//public static ItemCrafting itemPlate;
    //TODO//@ObjectHolder(ICBMConstants.PREFIX + "circuit")
    //TODO//public static ItemCrafting itemCircuit;
    //TODO//@ObjectHolder(ICBMConstants.PREFIX + "wire")
    //TODO//public static ItemCrafting itemWire;

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event) {

        //Items
        //TODO//event.getRegistry().register(new ItemGrenade().setName("grenade"));
        //TODO//event.getRegistry().register(new ItemBombCart().setName("bombcart"));
        //TODO//event.getRegistry().register(new ItemBase(new Item.Properties().group(ICBMClassic.CREATIVE_TAB)).setName("poisonPowder")); //TODO fix name _
        //TODO//Item sulfurItem = new ItemBase(new Item.Properties().group(ICBMClassic.CREATIVE_TAB)).setName("sulfurDust");
        //TODO//event.getRegistry().register(sulfurItem); //TODO fix name _
        //TODO//OreDictionary.registerOre("dustSulfur", sulfurItem);

        //TODO//Item saltpeterItem = new ItemBase(new Item.Properties().group(ICBMClassic.CREATIVE_TAB)).setName("saltpeter");
        //TODO//event.getRegistry().register(saltpeterItem);
        //TODO//OreDictionary.registerOre("dustSaltpeter", saltpeterItem);


        //TODO//event.getRegistry().register(new ItemAntidote().setName("antidote"));
        //TODO//event.getRegistry().register(new ItemSignalDisrupter());
        //TODO//event.getRegistry().register(new ItemTracker());
        //TODO//event.getRegistry().register(new ItemDefuser());
        //TODO//event.getRegistry().register(new ItemRadarGun());
        //TODO//event.getRegistry().register(new ItemRemoteDetonator());
        //TODO//event.getRegistry().register(new ItemLaserDetonator());
        //TODO//event.getRegistry().register(new ItemRocketLauncher());
        //TODO//event.getRegistry().register(new ItemMissile());

        //Block items
        event.getRegistry().register(new BlockItem(BlockReg.GLASS_PRESSURE_PLATE.get(), new Item.Properties()).setRegistryName(BlockReg.GLASS_PRESSURE_PLATE.get().getRegistryName()));
        event.getRegistry().register(new BlockItem(BlockReg.GLASS_BUTTON.get(), new Item.Properties()).setRegistryName(BlockReg.GLASS_BUTTON.get().getRegistryName()));
        //TODO//event.getRegistry().register(new BlockItemSubTypes(BlockReg.SPIKES.get()));
        event.getRegistry().register(new BlockItem(BlockReg.CONCRETE.get(), new Item.Properties().group(ICBMClassic.CREATIVE_TAB)).setRegistryName(BlockReg.CONCRETE.get().getRegistryName()));
        event.getRegistry().register(new BlockItem(BlockReg.CONCRETE_COMPACT.get(), new Item.Properties().group(ICBMClassic.CREATIVE_TAB)).setRegistryName(BlockReg.CONCRETE_COMPACT.get().getRegistryName()));
        event.getRegistry().register(new BlockItem(BlockReg.CONCRETE_REINFORCED.get(), new Item.Properties().group(ICBMClassic.CREATIVE_TAB)).setRegistryName(BlockReg.CONCRETE_REINFORCED.get().getRegistryName()));
        event.getRegistry().register(new BlockItem(BlockReg.REINFORCED_GLASS.get(), new Item.Properties()).setRegistryName(BlockReg.REINFORCED_GLASS.get().getRegistryName()));
        //TODO//event.getRegistry().register(new BlockItemExplosive(BlockReg.EXPLOSIVES.get(), new Item.Properties()).setRegistryName(BlockReg.EXPLOSIVES.get().getRegistryName()));
        //TODO//event.getRegistry().register(new BlockItem(BlockReg.EMP_TOWER.get(), new Item.Properties()).setRegistryName(BlockReg.EMP_TOWER.get().getRegistryName()));
        //TODO//event.getRegistry().register(new BlockItem(BlockReg.RADAR_STATION.get(), new Item.Properties()).setRegistryName(BlockReg.RADAR_STATION.get().getRegistryName()));
        //TODO//event.getRegistry().register(new BlockItemSubTypes(BlockReg.LAUNCHER_FRAME.get()));
        //TODO//event.getRegistry().register(new BlockItemRotatedMultiTile(BlockReg.LAUNCHER_BASE, e -> TileLauncherBase.getLayoutOfMultiBlock(e)));
        //TODO//event.getRegistry().register(new BlockItemSubTypes(BlockReg.LAUNCHER_SCREEN.get()));
        //TODO//event.getRegistry().register(new BlockItem(BlockReg.CRUISE_LAUNCHER.get(), new Item.Properties()).setRegistryName(BlockReg.CRUISE_LAUNCHER.get().getRegistryName()));

        //Crafting resources
        if (ConfigItems.ENABLE_CRAFTING_ITEMS) {

            if (ConfigItems.ENABLE_INGOTS_ITEMS) {
                event.getRegistry().register(new ItemCrafting("ingot", "steel", "copper"));
                event.getRegistry().register(new ItemCrafting("clump", "steel"));
            }
            if (ConfigItems.ENABLE_PLATES_ITEMS)
                event.getRegistry().register(new ItemCrafting("plate", "steel", "iron"));
            if (ConfigItems.ENABLE_CIRCUIT_ITEMS)
                event.getRegistry().register(new ItemCrafting("circuit", "basic", "advanced", "elite"));
            if (ConfigItems.ENABLE_WIRES_ITEMS)
                event.getRegistry().register(new ItemCrafting("wire", "copper", "gold"));

        }

        //Optional items
        if (ConfigItems.ENABLE_BATTERY)
            event.getRegistry().register(new ItemBattery());

    }

    */

}
