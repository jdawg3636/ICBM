package icbm.classic.content.reg;

import icbm.classic.ICBMClassic;
import icbm.classic.ICBMConstants;
import icbm.classic.config.ConfigItems;
import icbm.classic.content.blocks.explosive.BlockItemExplosive;
import icbm.classic.content.blocks.launcher.base.TileLauncherBase;
import icbm.classic.content.items.ItemAntidote;
import icbm.classic.content.items.ItemBattery;
import icbm.classic.content.items.ItemBombCart;
import icbm.classic.content.items.ItemCrafting;
import icbm.classic.content.items.ItemDefuser;
import icbm.classic.content.items.ItemGrenade;
import icbm.classic.content.items.ItemLaserDetonator;
import icbm.classic.content.items.ItemMissile;
import icbm.classic.content.items.ItemRadarGun;
import icbm.classic.content.items.ItemRemoteDetonator;
import icbm.classic.content.items.ItemRocketLauncher;
import icbm.classic.content.items.ItemSignalDisrupter;
import icbm.classic.content.items.ItemTracker;
import icbm.classic.prefab.item.ItemBase;
import icbm.classic.prefab.item.BlockItemRotatedMultiTile;
import icbm.classic.prefab.item.BlockItemSubTypes;
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
    @ObjectHolder(ICBMConstants.PREFIX + "signalDisrupter")
    public static Item itemSignalDisrupter;
    @ObjectHolder(ICBMConstants.PREFIX + "tracker")
    public static Item itemTracker;
    @ObjectHolder(ICBMConstants.PREFIX + "missile")
    public static Item itemMissile;
    @ObjectHolder(ICBMConstants.PREFIX + "defuser")
    public static Item itemDefuser;
    @ObjectHolder(ICBMConstants.PREFIX + "radarGun")
    public static Item itemRadarGun;
    @ObjectHolder(ICBMConstants.PREFIX + "remoteDetonator")
    public static Item itemRemoteDetonator;
    @ObjectHolder(ICBMConstants.PREFIX + "laserDetonator")
    public static Item itemLaserDetonator;
    @ObjectHolder(ICBMConstants.PREFIX + "rocketLauncher")
    public static Item itemRocketLauncher;
    @ObjectHolder(ICBMConstants.PREFIX + "grenade")
    public static Item itemGrenade;
    @ObjectHolder(ICBMConstants.PREFIX + "bombcart")
    public static Item itemBombCart;
    @ObjectHolder(ICBMConstants.PREFIX + "sulfurDust")
    public static Item itemSulfurDust;
    @ObjectHolder(ICBMConstants.PREFIX + "saltpeter")
    public static Item itemSaltpeterDust;
    @ObjectHolder(ICBMConstants.PREFIX + "poisonPowder")
    public static Item itemPoisonPowder;
    @ObjectHolder(ICBMConstants.PREFIX + "battery")
    public static Item itemBattery;
    @ObjectHolder(ICBMConstants.PREFIX + "ingot")
    public static ItemCrafting itemIngot;
    @ObjectHolder(ICBMConstants.PREFIX + "clump")
    public static ItemCrafting itemIngotClump;
    @ObjectHolder(ICBMConstants.PREFIX + "plate")
    public static ItemCrafting itemPlate;
    @ObjectHolder(ICBMConstants.PREFIX + "circuit")
    public static ItemCrafting itemCircuit;
    @ObjectHolder(ICBMConstants.PREFIX + "wire")
    public static ItemCrafting itemWire;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {

        //Items
        event.getRegistry().register(new ItemGrenade().setName("grenade"));
        event.getRegistry().register(new ItemBombCart().setName("bombcart"));
        event.getRegistry().register(new ItemBase(new Item.Properties().group(ICBMClassic.CREATIVE_TAB)).setName("poisonPowder")); //TODO fix name _
        Item sulfurItem = new ItemBase(new Item.Properties().group(ICBMClassic.CREATIVE_TAB)).setName("sulfurDust");
        event.getRegistry().register(sulfurItem); //TODO fix name _
        OreDictionary.registerOre("dustSulfur", sulfurItem);

        Item saltpeterItem = new ItemBase(new Item.Properties().group(ICBMClassic.CREATIVE_TAB)).setName("saltpeter");
        event.getRegistry().register(saltpeterItem);
        OreDictionary.registerOre("dustSaltpeter", saltpeterItem);


        event.getRegistry().register(new ItemAntidote().setName("antidote"));
        event.getRegistry().register(new ItemSignalDisrupter());
        event.getRegistry().register(new ItemTracker());
        event.getRegistry().register(new ItemDefuser());
        event.getRegistry().register(new ItemRadarGun());
        event.getRegistry().register(new ItemRemoteDetonator());
        event.getRegistry().register(new ItemLaserDetonator());
        event.getRegistry().register(new ItemRocketLauncher());
        event.getRegistry().register(new ItemMissile());

        //Block items
        event.getRegistry().register(new BlockItem(BlockReg.GLASS_PRESSURE_PLATE.get(), new Item.Properties()).setRegistryName(BlockReg.GLASS_PRESSURE_PLATE.get().getRegistryName()));
        event.getRegistry().register(new BlockItem(BlockReg.GLASS_BUTTON.get(), new Item.Properties()).setRegistryName(BlockReg.GLASS_BUTTON.get().getRegistryName()));
        event.getRegistry().register(new BlockItemSubTypes(BlockReg.SPIKES.get()));
        event.getRegistry().register(new BlockItemSubTypes(BlockReg.CONCRETE.get()));
        event.getRegistry().register(new BlockItem(BlockReg.REINFORCED_GLASS.get(), new Item.Properties()).setRegistryName(BlockReg.REINFORCED_GLASS.get().getRegistryName()));
        event.getRegistry().register(new BlockItemExplosive(BlockReg.EXPLOSIVES.get(), new Item.Properties()).setRegistryName(BlockReg.EXPLOSIVES.get().getRegistryName()));
        event.getRegistry().register(new BlockItem(BlockReg.EMP_TOWER.get(), new Item.Properties()).setRegistryName(BlockReg.EMP_TOWER.get().getRegistryName()));
        event.getRegistry().register(new BlockItem(BlockReg.RADAR_STATION.get(), new Item.Properties()).setRegistryName(BlockReg.RADAR_STATION.get().getRegistryName()));
        event.getRegistry().register(new BlockItemSubTypes(BlockReg.LAUNCHER_FRAME.get()));
        event.getRegistry().register(new BlockItemRotatedMultiTile(BlockReg.LAUNCHER_BASE, e -> TileLauncherBase.getLayoutOfMultiBlock(e)));
        event.getRegistry().register(new BlockItemSubTypes(BlockReg.LAUNCHER_SCREEN.get()));
        event.getRegistry().register(new BlockItem(BlockReg.CRUISE_LAUNCHER.get(), new Item.Properties()).setRegistryName(BlockReg.CRUISE_LAUNCHER.get().getRegistryName()));

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

}
