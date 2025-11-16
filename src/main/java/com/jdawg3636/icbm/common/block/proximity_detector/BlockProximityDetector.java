package com.jdawg3636.icbm.common.block.proximity_detector;

import com.jdawg3636.icbm.common.block.machine.AbstractBlockMachineTile;
import com.jdawg3636.icbm.common.block.particle_accelerator.ContainerParticleAccelerator;
import com.jdawg3636.icbm.common.block.particle_accelerator.TileParticleAccelerator;
import com.jdawg3636.icbm.common.reg.ContainerReg;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockProximityDetector extends AbstractBlockMachineTile {

    public BlockProximityDetector(RegistryObject<TileEntityType<? extends TileEntity>> tileEntityType) {
        super(tileEntityType, false);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState blockState, World level, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult hit) {
        if(level.isClientSide()) {
            return ActionResultType.SUCCESS;
        }
        TileEntity tileEntity = level.getBlockEntity(blockPos);
        if(tileEntity instanceof TileParticleAccelerator) {
            INamedContainerProvider containerProvider = new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return new TranslationTextComponent("gui.icbm.particle_accelerator");
                }

                @Override
                public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    return new ContainerParticleAccelerator(ContainerReg.PARTICLE_ACCELERATOR.get(), i, level, blockPos, playerInventory);
                }
            };
            NetworkHooks.openGui((ServerPlayerEntity) playerEntity, containerProvider, tileEntity.getBlockPos());
            return ActionResultType.CONSUME;
        }
        else {
            return super.use(blockState, level, blockPos, playerEntity, hand, hit);
        }
    }

    // Non-deprecated call is IForgeBlock::canConnectRedstone, which just redirects to this.
    @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }

    @Override
    public int getSignal(BlockState blockState, IBlockReader level, BlockPos blockPos, Direction p_180656_4_) {
        TileEntity tileEntity = level.getBlockEntity(blockPos);
        if(tileEntity instanceof TileProximityDetector) {
            return ((TileProximityDetector)tileEntity).getSignal(blockState, level, blockPos, p_180656_4_);
        }
        return 0;
    }

}
