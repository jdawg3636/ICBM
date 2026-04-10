package com.jdawg3636.icbm.common.block.emp_tower;

import com.jdawg3636.icbm.common.block.multiblock_spinning.TERMultiblockSpinning;
import com.jdawg3636.icbm.common.listener.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.common.util.LazyOptional;

public class TEREMPTower extends TERMultiblockSpinning<TileEMPTower> {

    public TEREMPTower(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(
            tileEntityRendererDispatcher,
            LazyOptional.of(() -> Minecraft.getInstance().getModelManager().getModel(ClientProxy.MODEL_EMP_TOWER_CLOCKWISE)),
            LazyOptional.of(() -> Minecraft.getInstance().getModelManager().getModel(ClientProxy.MODEL_EMP_TOWER_COUNTERCLOCKWISE)),
            LazyOptional.of(() -> Minecraft.getInstance().getModelManager().getModel(ClientProxy.MODEL_EMP_TOWER_STATIC))
        );
    }

}
