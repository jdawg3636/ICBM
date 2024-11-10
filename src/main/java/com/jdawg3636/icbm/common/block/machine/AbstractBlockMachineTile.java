package com.jdawg3636.icbm.common.block.machine;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkHooks;

public class AbstractBlockMachineTile extends AbstractBlockMachine {

    public final RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType;

    public AbstractBlockMachineTile(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        this(tileEntityType, true);
    }

    public AbstractBlockMachineTile(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType, boolean waterloggable) {
        this(getMultiblockMachineBlockProperties(), tileEntityType, waterloggable);
    }

    public AbstractBlockMachineTile(AbstractBlock.Properties properties, RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType, boolean waterloggable) {
        super(properties, waterloggable);
        this.tileEntityType = tileEntityType;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return hasTileEntity(state) ? tileEntityType.get().create() : null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState originalState, World level, BlockPos blockPos, BlockState newState, boolean flag) {
        if(originalState.getBlock() != newState.getBlock()) {
            TileEntity tileEntity = level.getBlockEntity(blockPos);
            if (tileEntity instanceof TileMachine) ((TileMachine) tileEntity).onBlockDestroyed();
        }
        super.onRemove(originalState, level, blockPos, newState, flag);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        TileEntity tileEntity = world.getBlockEntity(pos);
        if(!world.isClientSide() && tileEntity instanceof TileMachine) {
            NetworkHooks.openGui((ServerPlayerEntity) player, (TileMachine)tileEntity, tileEntity.getBlockPos());
            SUpdateTileEntityPacket supdatetileentitypacket = tileEntity.getUpdatePacket();
            if (supdatetileentitypacket != null) ((ServerPlayerEntity)player).connection.send(supdatetileentitypacket);
        }
        return ActionResultType.SUCCESS;
    }

}
