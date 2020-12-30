package icbm;

import icbm.client.ClientProxy;
import icbm.content.event.ICBMEvents;
import icbm.content.reg.BlockReg;
import icbm.content.reg.EntityReg;
import icbm.content.reg.ItemReg;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ICBMReference.MODID)
@Mod.EventBusSubscriber
public final class ICBM {

    public static CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    protected static Logger logger = LogManager.getLogger(ICBMReference.MODID);
    public static Logger logger() { return logger; }

    public static final ItemGroup CREATIVE_TAB = new ItemGroup(ICBMReference.MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ItemReg.EXPLOSIVES_CONDENSED.get());
        }
    };

    public ICBM() {

        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetupEvent);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetupEvent);
        MinecraftForge.EVENT_BUS.register(ICBMEvents.class);

        BlockReg.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BlockReg.TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        EntityReg.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ItemReg.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

    }

    public void onClientSetupEvent(final FMLClientSetupEvent event) {
        proxy.onClientSetupEvent();
    }

    public void onCommonSetupEvent(final FMLCommonSetupEvent event) {
        proxy.onCommonSetupEvent();
    }

}
