package com.jdawg3636.icbm.common.block.multiblock;

import com.jdawg3636.icbm.common.block.machine.TileMachine;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.INameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class TileMultiblockPassthrough extends TileEntity implements INamedContainerProvider, INameable {

    public TileMultiblockPassthrough(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public Optional<TileMachine> getRealTileEntity() {
        if(level == null) return Optional.empty();
        BlockPos blockPos = getBlockPos();
        BlockState blockState = getBlockState();
        if(!(blockState.getBlock() instanceof AbstractBlockMulti)) {
            return Optional.empty();
        }
        BlockPos realTileBlockPos = ((AbstractBlockMulti)blockState.getBlock()).getMultiblockCenter(level, blockPos, blockState);
        TileEntity realTileEntity = level.getBlockEntity(realTileBlockPos);
        if(realTileEntity instanceof TileMachine) {
            return Optional.of((TileMachine) realTileEntity);
        }
        return Optional.empty();
    }

    @Override
    public ITextComponent getName() {
        return getRealTileEntity().map(TileMachine::getName).orElse(new StringTextComponent("Multiblock Passthrough"));
    }

    @Override
    public boolean hasCustomName() {
        return getRealTileEntity().map(TileMachine::hasCustomName).orElse(INameable.super.hasCustomName());
    }

    @Override
    public ITextComponent getDisplayName() {
        return getRealTileEntity().map(TileMachine::getDisplayName).orElse(INameable.super.getDisplayName());
    }

    @Nullable
    @Override
    public ITextComponent getCustomName() {
        return getRealTileEntity().map(TileMachine::getCustomName).orElse(INameable.super.getCustomName());
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return getRealTileEntity().map(tileMachine -> tileMachine.createMenu(i, playerInventory, playerEntity)).orElse(null);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return getRealTileEntity().map(tileMachine -> tileMachine.getCapabilityViaProxy(cap, side, getBlockState())).orElse(LazyOptional.empty());
    }

}
