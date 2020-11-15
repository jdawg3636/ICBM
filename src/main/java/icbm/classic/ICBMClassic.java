package icbm.classic;

//TODO//import icbm.classic.api.EnumTier;
//TODO//import icbm.classic.api.ICBMClassicAPI;
import icbm.classic.client.ClientProxy;
import icbm.classic.content.reg.BlockReg;
//TODO//import icbm.classic.lib.NBTConstants;
//TODO//import icbm.classic.api.reg.events.ExplosiveRegistryEvent;
//TODO//import icbm.classic.api.reg.events.ExplosiveContentRegistryEvent;
//TODO//import icbm.classic.command.ICBMCommands;
//TODO//import icbm.classic.command.system.CommandEntryPoint;
//TODO//import icbm.classic.config.ConfigItems;
//TODO//import icbm.classic.config.ConfigThread;
//TODO//import icbm.classic.content.reg.ExplosiveInit;
//TODO//import icbm.classic.content.entity.missile.CapabilityMissile;
//TODO//import icbm.classic.content.items.behavior.BombCartDispenseBehavior;
//TODO//import icbm.classic.content.items.behavior.GrenadeDispenseBehavior;
//TODO//import icbm.classic.content.potion.ContagiousPoison;
//TODO//import icbm.classic.content.potion.PoisonContagion;
//TODO//import icbm.classic.content.potion.PoisonFrostBite;
//TODO//import icbm.classic.content.potion.PoisonToxin;
import icbm.classic.content.reg.ItemReg;
//TODO//import icbm.classic.lib.capability.emp.CapabilityEMP;
//TODO//import icbm.classic.lib.capability.ex.CapabilityExplosive;
//TODO//import icbm.classic.lib.energy.system.EnergySystem;
//TODO//import icbm.classic.lib.energy.system.EnergySystemFE;
//TODO//import icbm.classic.lib.explosive.reg.*;
//TODO//import icbm.classic.lib.network.netty.PacketManager;
//TODO//import icbm.classic.lib.radar.RadarRegistry;
//TODO//import icbm.classic.lib.radio.RadioRegistry;
//TODO//import icbm.classic.lib.thread.WorkerThreadManager;
//TODO//import icbm.classic.prefab.item.LootEntryItemStack;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

/**
 * Mod class for ICBM Classic, contains all loading code and references to objects crated by the mod.
 *
 * @author Dark(DarkGuardsman, Robert).
 * <p>
 * Orginal author and creator of the mod: Calclavia
 */
@Mod(ICBMConstants.DOMAIN)
@Mod.EventBusSubscriber
public final class ICBMClassic {

    public static final boolean runningAsDev = System.getProperty("development") != null && System.getProperty("development").equalsIgnoreCase("true");

    //@Mod.Instance(ICBMConstants.DOMAIN)
    //public static ICBMClassic INSTANCE;

    // Clever hack found in Mekanism source, not the proper way to do this.
    public static CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public static final int MAP_HEIGHT = 255;

    protected static Logger logger = LogManager.getLogger(ICBMConstants.DOMAIN);
    public static Logger logger() { return logger; }

    //TODO//public static final PacketManager packetHandler = new PacketManager(ICBMConstants.DOMAIN);

    //TODO//public static final ContagiousPoison poisonous_potion = new ContagiousPoison("Chemical", 0, false);
    //TODO//public static final ContagiousPoison contagios_potion = new ContagiousPoison("Contagious", 1, true);

    public static final ItemGroup CREATIVE_TAB = new ItemGroup(ICBMConstants.DOMAIN) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ItemReg.CONCRETE_REINFORCED.get());
        }
    };

    public ICBMClassic() {

        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetupEvent);

        BlockReg.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BlockReg.TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ItemReg.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

    }

    public void onClientSetupEvent(final FMLClientSetupEvent event) {
        proxy.setRenderLayers();
    }

    /*
    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        if (ConfigItems.ENABLE_CRAFTING_ITEMS) {

            //Steel clump -> Steel ingot
            if (ConfigItems.ENABLE_INGOTS_ITEMS)
                GameRegistry.addSmelting(new ItemStack(ItemReg.itemIngotClump, 1, 0), new ItemStack(ItemReg.itemIngot, 1, 0), 0.1f);

            //Fix for removing recipe of plate
            if (ConfigItems.ENABLE_PLATES_ITEMS) {
                GameRegistry.addSmelting(ItemReg.itemPlate.getStack("iron", 1), new ItemStack(Items.IRON_INGOT), 0f);
            }

        }
    }
    */

    // TODO replace all of this with modern technique https://mcforge.readthedocs.io/en/1.16.x/items/globallootmodifiers/
    @SubscribeEvent
    public void registerLoot(LootTableLoadEvent event) {
        ///setblock ~ ~ ~ minecraft:chest 0 replace {LootTable:"minecraft:chests/simple_dungeon"}
        /*
        final String VANILLA_LOOT_POOL_ID = "main";
        if (event.getName().equals(LootTables.CHESTS_ABANDONED_MINESHAFT) || event.getName().equals(LootTables.CHESTS_SIMPLE_DUNGEON)) {
            if (ConfigItems.ENABLE_LOOT_DROPS) {
                LootPool lootPool = event.getTable().getPool(VANILLA_LOOT_POOL_ID);
                if (lootPool != null && ConfigItems.ENABLE_CRAFTING_ITEMS) {

                    if (ConfigItems.ENABLE_INGOTS_ITEMS) {
                        lootPool.addEntry(new LootEntryItemStack(ICBMConstants.PREFIX + "ingot.copper", ItemReg.itemIngot.getStack("copper", 10), 15, 5));
                        lootPool.addEntry(new LootEntryItemStack(ICBMConstants.PREFIX + "ingot.steel", ItemReg.itemIngot.getStack("steel", 10), 20, 3));
                    }
                    if (ConfigItems.ENABLE_PLATES_ITEMS) {
                        lootPool.addEntry(new LootEntryItemStack(ICBMConstants.PREFIX + "plate.steel", ItemReg.itemPlate.getStack("steel", 5), 30, 3));
                    }
                    if (ConfigItems.ENABLE_WIRES_ITEMS) {
                        lootPool.addEntry(new LootEntryItemStack(ICBMConstants.PREFIX + "wire.copper", ItemReg.itemWire.getStack("copper", 20), 15, 5));
                        lootPool.addEntry(new LootEntryItemStack(ICBMConstants.PREFIX + "wire.gold", ItemReg.itemWire.getStack("gold", 15), 30, 3));
                    }
                    if (ConfigItems.ENABLE_CIRCUIT_ITEMS) {
                        lootPool.addEntry(new LootEntryItemStack(ICBMConstants.PREFIX + "circuit.basic", ItemReg.itemCircuit.getStack("basic", 15), 15, 5));
                        lootPool.addEntry(new LootEntryItemStack(ICBMConstants.PREFIX + "circuit.advanced", ItemReg.itemCircuit.getStack("advanced", 11), 30, 3));
                        lootPool.addEntry(new LootEntryItemStack(ICBMConstants.PREFIX + "circuit.elite", ItemReg.itemCircuit.getStack("elite", 8), 30, 3));
                    }

                }
            }
        }
        else if (event.getName().equals(LootTables.ENTITIES_CREEPER)) {
            if (ConfigItems.ENABLE_SULFUR_LOOT_DROPS) {
                LootPool lootPool = event.getTable().getPool(VANILLA_LOOT_POOL_ID);
                if (lootPool != null) {
                    lootPool.addEntry(new LootEntryItemStack(ICBMConstants.PREFIX + "sulfur", new ItemStack(ItemReg.itemSulfurDust, 10, 0), 2, 0));
                }
            }
        }
        */
    }

    /* //TODO Rework to match new Forge lifecycle
    @SubscribeEvent
    public void preInit(FMLPreInitializationEvent event) {
        //Verify that our nbt tag strings are distinct. If this fails then this will crash Minecraft!
        NBTConstants.ensureThatAllTagNamesAreDistinct();

        proxy.preInit();
        EnergySystem.register(new EnergySystemFE());

        //Register caps
        CapabilityEMP.register();
        CapabilityMissile.register();
        CapabilityExplosive.register();

        // TODO implement (new) data fixer system (net.minecraft.util.datafix)
        // https://www.reddit.com/r/feedthebeast/comments/7fbqfw/psa_modders_are_you_calling_datafixers_on_your/
        // https://github.com/sinkillerj/ProjectE/commit/46005c9054fe8386497856a2558d0bde3ee75dce#diff-cf9efddd30b4c8c205baed6f33906532R121

        MinecraftForge.EVENT_BUS.register(RadarRegistry.INSTANCE);
        MinecraftForge.EVENT_BUS.register(RadioRegistry.INSTANCE);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

        handleExRegistry(event.getModConfigurationDirectory());
    }
    */

    /* TODO
    private void handleExRegistry(File configMainFolder) {
        //Init registry
        final ExplosiveRegistry explosiveRegistry = new ExplosiveRegistry();
        ICBMClassicAPI.EXPLOSIVE_REGISTRY = explosiveRegistry;

        ICBMClassicAPI.EX_BLOCK_REGISTRY = new ExBlockContentReg();
        ICBMClassicAPI.EX_GRENADE_REGISTRY = new ExGrenadeContentReg();
        ICBMClassicAPI.EX_MINECART_REGISTRY = new ExMinecartContentReg();
        ICBMClassicAPI.EX_MISSILE_REGISTRY = new ExMissileContentReg();

        //Load data
        explosiveRegistry.loadReg(new File(configMainFolder, "icbmclassic/explosive_reg.json"));

        //Register default content types
        explosiveRegistry.registerContentRegistry(ICBMClassicAPI.EX_BLOCK_REGISTRY);
        explosiveRegistry.registerContentRegistry(ICBMClassicAPI.EX_GRENADE_REGISTRY);
        explosiveRegistry.registerContentRegistry(ICBMClassicAPI.EX_MISSILE_REGISTRY);
        explosiveRegistry.registerContentRegistry(ICBMClassicAPI.EX_MINECART_REGISTRY);

        //Fire registry events for content types
        //TODO//MinecraftForge.EVENT_BUS.post(new ExplosiveContentRegistryEvent(explosiveRegistry));

        //Lock content types, done to prevent errors with adding content
        explosiveRegistry.lockNewContentTypes();

        //Register internal first to reserve slots for backwards compatibility
        ExplosiveInit.init();

        //Fire registry event for explosives
        //TODO//MinecraftForge.EVENT_BUS.post(new ExplosiveRegistryEvent(explosiveRegistry));
        explosiveRegistry.lockNewExplosives();

        //Do default content types per explosive
        explosiveRegistry.getExplosives().stream().filter(ex -> ex.getTier() != EnumTier.NONE).forEach(ex -> ICBMClassicAPI.EX_BLOCK_REGISTRY.enableContent(ex.getRegistryName()));
        explosiveRegistry.getExplosives().stream().filter(ex -> ex.getTier() != EnumTier.NONE).forEach(ex -> ICBMClassicAPI.EX_MISSILE_REGISTRY.enableContent(ex.getRegistryName()));
        explosiveRegistry.getExplosives().stream().filter(ex -> ex.getTier() != EnumTier.NONE).forEach(ex -> ICBMClassicAPI.EX_MINECART_REGISTRY.enableContent(ex.getRegistryName()));
        explosiveRegistry.getExplosives().stream().filter(ex -> ex.getTier() == EnumTier.ONE).forEach(ex -> ICBMClassicAPI.EX_GRENADE_REGISTRY.enableContent(ex.getRegistryName()));
        //TODO configs to disable types per explosive
        //TODO mesh mapper to match model to state

        //Lock all registry, done to prevent errors in data generation for renders and content
        explosiveRegistry.completeLock();

        //Save registry, at this point everything should be registered
        explosiveRegistry.saveReg();
    }
    */

    /**
     * This is the first of four commonly called events during mod initialization.
     *
     * Called before {@link FMLClientSetupEvent} or {@link FMLDedicatedServerSetupEvent} during mod startup.
     *
     * Called after {@link net.minecraftforge.event.RegistryEvent.Register} events have been fired.
     *
     * Either register your listener using {@link net.minecraftforge.fml.AutomaticEventSubscriber} and
     * {@link net.minecraftforge.eventbus.api.SubscribeEvent} or
     * {@link net.minecraftforge.eventbus.api.IEventBus#addListener(Consumer)} in your constructor.
     *
     * Most non-specific mod setup will be performed here. Note that this is a parallel dispatched event - you cannot
     * interact with game state in this event.
     *
     * @see net.minecraftforge.fml.DeferredWorkQueue to enqueue work to run on the main game thread after this event has
     * completed dispatch
     */
    @SubscribeEvent
    public void init(FMLCommonSetupEvent event) {
        proxy.init();
        //TODO//packetHandler.init();

        //TODO//if (ConfigItems.ENABLE_CRAFTING_ITEMS) {
            //TODO//if (ConfigItems.ENABLE_INGOTS_ITEMS)
                //TODO//ItemReg.itemIngot.registerOreNames();

            //TODO//if (ConfigItems.ENABLE_PLATES_ITEMS)
                //TODO//ItemReg.itemPlate.registerOreNames("iron");

            //TODO//if (ConfigItems.ENABLE_CIRCUIT_ITEMS)
                //TODO//ItemReg.itemCircuit.registerOreNames();

            //TODO//if (ConfigItems.ENABLE_WIRES_ITEMS)
                //TODO//ItemReg.itemWire.registerOreNames();
        //TODO//}

        //TODO//OreDictionary.registerOre("dustSulfur", new ItemStack(ItemReg.itemSulfurDust));
        //TODO//OreDictionary.registerOre("dustSaltpeter", new ItemStack(ItemReg.itemSaltpeterDust));

        /** Potion Effects */ //TODO move to effect system
        //TODO//PoisonToxin.INSTANCE = Effects.POISON;//new PoisonToxin(true, 5149489, "toxin");
        //TODO//PoisonContagion.INSTANCE = Effects.POISON;//new PoisonContagion(false, 5149489, "virus");
        //TODO//PoisonFrostBite.INSTANCE = Effects.POISON;//new PoisonFrostBite(false, 5149489, "frostBite");

        /** Dispenser Handler */
        //TODO//if (ItemReg.itemGrenade != null)
        //TODO//    DispenserBlock.registerDispenseBehavior(ItemReg.itemGrenade, new GrenadeDispenseBehavior());

        //TODO//if (ItemReg.itemBombCart != null)
        //TODO//    DispenserBlock.registerDispenseBehavior(ItemReg.itemBombCart, new BombCartDispenseBehavior());

        proxy.init();
    }

    /*
    @SubscribeEvent
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }
    */

    @SubscribeEvent
    public void serverStarting(FMLServerStartingEvent event) {
        /*
        //Get command manager
        ICommandManager commandManager = FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager();
        ServerCommandManager serverCommandManager = ((ServerCommandManager) commandManager);

        //Setup commands
        ICBMCommands.init();

        //Register main command
        serverCommandManager.registerCommand(new CommandEntryPoint("icbm", ICBMCommands.ICBM_COMMAND));
        */

        //TODO//WorkerThreadManager.INSTANCE = new WorkerThreadManager(ConfigThread.THREAD_COUNT);
        //TODO//WorkerThreadManager.INSTANCE.startThreads();
    }

    @SubscribeEvent
    public void serverStopping(FMLServerStoppingEvent event) {
        //TODO//WorkerThreadManager.INSTANCE.killThreads();
    }

}
