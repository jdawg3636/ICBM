package com.jdawg3636.icbm;

import com.jdawg3636.icbm.common.config.ICBMConfig;
import com.jdawg3636.icbm.common.listener.ClientProxy;
import com.jdawg3636.icbm.common.listener.CommonProxy;
import com.jdawg3636.icbm.common.reg.ItemReg;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.DistExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;

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

    public static final CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public static Logger logger() {
        return logger;
    }

    public static MessageDigest SHA1_INSTANCE;

    static {
        try {
            SHA1_INSTANCE = MessageDigest.getInstance("SHA-1");
        } catch (Exception ignored) {}
    }

}
