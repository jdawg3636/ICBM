package com.jdawg3636.icbm.common.block.coal_generator;

import com.jdawg3636.icbm.common.block.machine.AbstractBlockMachineTile;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

public class BlockCoalGenerator extends AbstractBlockMachineTile {

    public BlockCoalGenerator(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        this(tileEntityType, false);
    }

    public BlockCoalGenerator(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType, boolean waterloggable) {
        super(tileEntityType, waterloggable);
    }

    public BlockCoalGenerator(AbstractBlock.Properties properties, RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        super(properties, tileEntityType, false);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState blockState, World level, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult hit) {
        // TODO
        return super.use(blockState, level, blockPos, playerEntity, hand, hit);
    }

}
