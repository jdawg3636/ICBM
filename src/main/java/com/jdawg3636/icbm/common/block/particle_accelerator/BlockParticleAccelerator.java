package com.jdawg3636.icbm.common.block.particle_accelerator;

import com.jdawg3636.icbm.common.block.machine.AbstractBlockMachineTile;
import net.minecraft.block.AbstractBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

public class BlockParticleAccelerator extends AbstractBlockMachineTile {

    public BlockParticleAccelerator(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        this(tileEntityType, false);
    }

    public BlockParticleAccelerator(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType, boolean waterloggable) {
        super(tileEntityType, waterloggable);
    }

    public BlockParticleAccelerator(AbstractBlock.Properties properties, RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType, boolean waterloggable) {
        super(properties, tileEntityType, waterloggable);
    }

}
