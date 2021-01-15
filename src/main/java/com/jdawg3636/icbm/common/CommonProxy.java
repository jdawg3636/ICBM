package com.jdawg3636.icbm.common;

import com.jdawg3636.icbm.common.network.ICBMPacketHandler;
import com.jdawg3636.icbm.common.network.PacketBlastEvent;

public class CommonProxy {

    public void onClientSetupEvent() {
    }

    public void onCommonSetupEvent() {
        // Register Messages/Packets for SimpleImpl Networking
        // Based on Networking.registerMessages() from https://github.com/McJty/YouTubeModding14/commit/3030c7affd9b34897c631c89a3197892c17d2468
        int index = 0;
        ICBMPacketHandler.INSTANCE.registerMessage(index++, PacketBlastEvent.class, PacketBlastEvent::toBytes, PacketBlastEvent::new, PacketBlastEvent::handle);
    }

}
