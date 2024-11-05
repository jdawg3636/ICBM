package com.jdawg3636.icbm.common.block.radar_station;

import com.jdawg3636.icbm.common.block.emp_tower.TEREMPTower;
import com.jdawg3636.icbm.common.listener.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.common.util.LazyOptional;

public class TERRadarStation extends TEREMPTower {

    public TERRadarStation(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(
                tileEntityRendererDispatcher,
                LazyOptional.of(() -> Minecraft.getInstance().getModelManager().getModel(ClientProxy.MODEL_RADAR_STATION_DYNAMIC)),
                LazyOptional.empty(),
                LazyOptional.of(() -> Minecraft.getInstance().getModelManager().getModel(ClientProxy.MODEL_RADAR_STATION_STATIC))
        );
    }

}
