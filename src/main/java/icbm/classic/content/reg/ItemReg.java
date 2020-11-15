package icbm.classic.content.reg;

import icbm.classic.ICBMClassic;
import icbm.classic.ICBMConstants;
//TODO//import icbm.classic.config.ConfigItems;
//TODO//import icbm.classic.content.blocks.explosive.BlockItemExplosive;
//TODO//import icbm.classic.content.blocks.launcher.base.TileLauncherBase;
//TODO//import icbm.classic.content.items.ItemAntidote;
//TODO//import icbm.classic.content.items.ItemBattery;
//TODO//import icbm.classic.content.items.ItemBombCart;
//TODO//import icbm.classic.content.items.ItemCrafting;
//TODO//import icbm.classic.content.items.ItemDefuser;
//TODO//import icbm.classic.content.items.ItemGrenade;
//TODO//import icbm.classic.content.items.ItemLaserDetonator;
//TODO//import icbm.classic.content.items.ItemMissile;
//TODO//import icbm.classic.content.items.ItemRadarGun;
//TODO//import icbm.classic.content.items.ItemRemoteDetonator;
//TODO//import icbm.classic.content.items.ItemRocketLauncher;
//TODO//import icbm.classic.content.items.ItemSignalDisrupter;
//TODO//import icbm.classic.content.items.ItemTracker;
//TODO//import icbm.classic.prefab.item.ItemBase;
//TODO//import icbm.classic.prefab.item.BlockItemRotatedMultiTile;
//TODO//import icbm.classic.prefab.item.BlockItemSubTypes;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Created by Dark(DarkGuardsman, Robert) on 1/7/19.
 */
@Mod.EventBusSubscriber(modid = ICBMConstants.DOMAIN)
public class ItemReg {

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
    public static void registerItems(RegistryEvent.Register<Item> event) {

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
        //TODO//event.getRegistry().register(new BlockItemSubTypes(BlockReg.CONCRETE.get()));
        event.getRegistry().register(new BlockItem(BlockReg.REINFORCED_GLASS.get(), new Item.Properties()).setRegistryName(BlockReg.REINFORCED_GLASS.get().getRegistryName()));
        //TODO//event.getRegistry().register(new BlockItemExplosive(BlockReg.EXPLOSIVES.get(), new Item.Properties()).setRegistryName(BlockReg.EXPLOSIVES.get().getRegistryName()));
        //TODO//event.getRegistry().register(new BlockItem(BlockReg.EMP_TOWER.get(), new Item.Properties()).setRegistryName(BlockReg.EMP_TOWER.get().getRegistryName()));
        //TODO//event.getRegistry().register(new BlockItem(BlockReg.RADAR_STATION.get(), new Item.Properties()).setRegistryName(BlockReg.RADAR_STATION.get().getRegistryName()));
        //TODO//event.getRegistry().register(new BlockItemSubTypes(BlockReg.LAUNCHER_FRAME.get()));
        //TODO//event.getRegistry().register(new BlockItemRotatedMultiTile(BlockReg.LAUNCHER_BASE, e -> TileLauncherBase.getLayoutOfMultiBlock(e)));
        //TODO//event.getRegistry().register(new BlockItemSubTypes(BlockReg.LAUNCHER_SCREEN.get()));
        //TODO//event.getRegistry().register(new BlockItem(BlockReg.CRUISE_LAUNCHER.get(), new Item.Properties()).setRegistryName(BlockReg.CRUISE_LAUNCHER.get().getRegistryName()));

        /* TODO
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

        */

    }

}
