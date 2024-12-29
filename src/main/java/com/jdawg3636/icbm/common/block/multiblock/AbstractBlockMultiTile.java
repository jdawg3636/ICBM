package com.jdawg3636.icbm.common.block.multiblock;

import com.jdawg3636.icbm.common.block.machine.TileMachine;
import com.jdawg3636.icbm.common.reg.TileReg;
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

import java.util.stream.Stream;

public abstract class AbstractBlockMultiTile extends AbstractBlockMulti {

    public final RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType;

    public AbstractBlockMultiTile(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        this(tileEntityType, true);
    }

    public AbstractBlockMultiTile(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType, boolean waterloggable) {
        this(getMultiblockMachineBlockProperties(), tileEntityType, waterloggable);
    }

    public AbstractBlockMultiTile(AbstractBlock.Properties properties, RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType, boolean waterloggable) {
        super(properties, waterloggable);
        this.tileEntityType = tileEntityType;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return this.isRootOfMultiblock(state) || Stream.of(getMutiblockOffsetsWhichHavePassthroughTileEntity()).map(vec -> doesStateMatchPosition(state, vec)).reduce(Boolean::logicalOr).orElse(false);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if(hasTileEntity(state)) {
            if(this.isRootOfMultiblock(state)) {
                return tileEntityType.get().create();
            }
            else {
                return TileReg.MULTIBLOCK_PASSTHROUGH.get().create();
            }
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if (!world.isClientSide) {
            TileEntity tileEntity = world.getBlockEntity(getMultiblockCenter(world, pos, state));
            if (tileEntity != null) {
                onUseMultiblock(tileEntity, state, world, pos, player, hand, trace);
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        return ActionResultType.SUCCESS;
    }

    public void onUseMultiblock(TileEntity tileEntity, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if(!world.isClientSide() && tileEntity instanceof TileMachine) {
            NetworkHooks.openGui((ServerPlayerEntity) player, (TileMachine)tileEntity, tileEntity.getBlockPos());
            SUpdateTileEntityPacket supdatetileentitypacket = tileEntity.getUpdatePacket();
            if (supdatetileentitypacket != null) ((ServerPlayerEntity)player).connection.send(supdatetileentitypacket);
        }
    }

    @Override
    public void destroyMultiblock(World worldIn, BlockPos pos, BlockState sourceState) {
        BlockPos rootPos = getMultiblockCenter(worldIn, pos, sourceState);
        TileEntity tileEntity = worldIn.getBlockEntity(rootPos);
        if(tileEntity instanceof TileMachine) ((TileMachine)tileEntity).onBlockDestroyed();
        super.destroyMultiblock(worldIn, pos, sourceState);
    }

}
