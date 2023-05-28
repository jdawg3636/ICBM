package com.jdawg3636.icbm;

import com.jdawg3636.icbm.common.config.ICBMConfig;
import com.jdawg3636.icbm.common.listener.ClientProxy;
import com.jdawg3636.icbm.common.listener.CommonProxy;
import com.jdawg3636.icbm.common.reg.ItemReg;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.DistExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ICBMReference {

    public static final String MODID = "icbm";

    public static final String CONFIG_FILE_PREFIX = "intercontinentalballisticredux";
    public static final ICBMConfig.Client CLIENT_CONFIG = new ICBMConfig.Client();
    public static final ICBMConfig.Common COMMON_CONFIG = new ICBMConfig.Common();
    public static final ICBMConfig.Server SERVER_CONFIG = new ICBMConfig.Server();

    public static final ItemGroup CREATIVE_TAB = new ItemGroup(ICBMReference.MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemReg.EXPLOSIVES_ANTIMATTER.get());
        }
    };

    private static final Logger logger = LogManager.getLogger(ICBMReference.MODID);

    private static final CommonProxy distProxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public static Logger logger() {
        return logger;
    }

    public static CommonProxy distProxy() {
        return distProxy;
    }

    @CapabilityInject(IEnergyStorage.class)
    public static Capability<IEnergyStorage> FORGE_ENERGY_CAPABILITY;

    public static void broadcastToChat(IWorldReader level, String message) {
        if (level instanceof ServerWorld) {
            ((ServerWorld) level).players().forEach(
                (ServerPlayerEntity serverPlayer) -> serverPlayer.sendMessage(new StringTextComponent(message), Util.NIL_UUID)
            );
        }
    }

    public static void broadcastToChat(IWorldReader level, String message, Object... messageArgs) {
        String completeMessage = message;
        try {
            completeMessage = String.format(message, messageArgs);
        } catch (Exception ignored) {}
        broadcastToChat(level, completeMessage);
    }

}
