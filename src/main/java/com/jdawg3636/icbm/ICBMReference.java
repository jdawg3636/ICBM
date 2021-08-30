package com.jdawg3636.icbm;

import com.jdawg3636.icbm.common.listener.ClientProxy;
import com.jdawg3636.icbm.common.listener.CommonProxy;
import com.jdawg3636.icbm.common.reg.ItemReg;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.DistExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ICBMReference {

    public static final String MODID = "icbm";

    public static CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    protected static Logger logger = LogManager.getLogger(ICBMReference.MODID);
    public static Logger logger() { return logger; }

    public static final ItemGroup CREATIVE_TAB = new ItemGroup(ICBMReference.MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemReg.EXPLOSIVES_ANTIMATTER.get());
        }
    };

}
