package com.jdawg3636.icbm.common.block.machine;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.launcher_platform.ContainerLauncherPlatform;
import com.jdawg3636.icbm.common.block.multiblock.AbstractBlockMultiTile;
import com.jdawg3636.icbm.common.capability.energystorage.ICBMEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.INameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class TileMachine extends TileEntity implements INamedContainerProvider, INameable {

    private ITextComponent name;
    private final ITextComponent defaultName;
    private final Supplier<ContainerType<? extends AbstractContainerMachine>> containerType;
    private final ItemStackHandler itemHandler;
    public final LazyOptional<IItemHandler> itemHandlerLazyOptional;
    private final ICBMEnergyStorage energyStorage;
    public final LazyOptional<IEnergyStorage> energyStorageLazyOptional;

    public TileMachine(TileEntityType<?> tileEntityTypeIn, Supplier<ContainerType<? extends AbstractContainerMachine>> containerType) {
        this(tileEntityTypeIn, containerType, 0, 0, 0, 0, new TranslationTextComponent("gui.icbm.unspecialized_machine"));
    }

    public TileMachine(TileEntityType<?> tileEntityTypeIn, Supplier<ContainerType<? extends AbstractContainerMachine>> containerType, int inventorySize, int forgeEnergyCapacity, int forgeEnergyPerTickIn, int forgeEnergyPerTickOut, ITextComponent defaultName) {
        super(tileEntityTypeIn);
        this.containerType = containerType;
        if(inventorySize > 0) {
            itemHandler = createHandler(inventorySize);
            itemHandlerLazyOptional = LazyOptional.of(() -> itemHandler);
        }
        else {
            itemHandler = null;
            itemHandlerLazyOptional = LazyOptional.empty();
        }
        this.energyStorage = new ICBMEnergyStorage().setCapacity(forgeEnergyCapacity, false).setMaxReceive(forgeEnergyPerTickIn).setMaxExtract(forgeEnergyPerTickOut).setCallbackOnChanged(this::setChanged);
        this.energyStorageLazyOptional = LazyOptional.of(() -> energyStorage);
        this.defaultName = defaultName;
    }

    /*
    * Note! This must be called by the block. This is done by default in AbstractBlockMachineTile.
    * */
    public void onBlockDestroyed() {
        if(level != null && itemHandler != null) {
            for(int i = 0; i < itemHandler.getSlots(); ++i) {
                ItemStack contents = itemHandler.getStackInSlot(0);
                itemHandler.setStackInSlot(0, ItemStack.EMPTY);
                InventoryHelper.dropItemStack(level, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), contents);
            }
        }
    }

    protected void onInventorySlotChanged(int slot) {
        setChanged();
    }

    public boolean isInventoryItemValid(int slot, ItemStack stack) {
        return true;
    }

    private ItemStackHandler createHandler(int inventorySize) {
        return new ICBMItemStackHandler(inventorySize, this::onInventorySlotChanged, this::isInventoryItemValid);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(itemHandlerLazyOptional.isPresent() && cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) return itemHandlerLazyOptional.cast();
        if(energyStorageLazyOptional.isPresent() && cap.equals(CapabilityEnergy.ENERGY)) return energyStorageLazyOptional.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        // NOTE: This has the effect of sending **ALL** of the NBT data to the client, which could exploited by hacked clients
        // to expose otherwise hidden information to players AND risks sending excessive (likely irrelevant) data.
        // This should be overridden for machines containing sensitive data (or just large data that doesn't need synchronized).
        CompoundNBT tag = super.getUpdateTag();
        return save(tag);
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        if(tag.contains("CustomName", 8)) this.name = ITextComponent.Serializer.fromJson(tag.getString("CustomName"));
        if(tag.contains("inv") && itemHandlerLazyOptional.isPresent()) itemHandler.deserializeNBT(tag.getCompound("inv"));
        if(tag.contains("energy") && energyStorageLazyOptional.isPresent()) energyStorage.deserializeNBT(tag.getCompound("energy"));
        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        if (this.name != null) tag.putString("CustomName", ITextComponent.Serializer.toJson(this.name));
        if(itemHandlerLazyOptional.isPresent()) tag.put("inv", itemHandler.serializeNBT());
        if(energyStorageLazyOptional.isPresent()) tag.put("energy", energyStorage.serializeNBT());
        return super.save(tag);
    }

    // ICBM Machines are non-ticking by default.
    // Subclasses must implement ITickableTileEntity and override tick() to call tickMachine()
    public void tickMachine() {
        if(level != null && !level.isClientSide()) {
            tickIngestEnergy();
        }
    }

    public void tickIngestEnergy() {
        // Assumes that side has already been checked - should only ever be called on the logical server
        assert level != null && !level.isClientSide();
        energyStorageLazyOptional.ifPresent(energyStorageUncast -> {
            if(energyStorageUncast instanceof ICBMEnergyStorage) {
                ICBMEnergyStorage energyStorage = (ICBMEnergyStorage)energyStorageUncast;
                int remainingCapacity = energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored();
                int amountIngested = ingestEnergy(Math.min(remainingCapacity, energyStorage.getMaxReceive()), Direction.values());
                energyStorage.receiveEnergy(amountIngested, false);
            }
        });
    }

    @Override
    public double getViewDistance() {
        return ICBMReference.distProxy().getTileEntityUpdateDistance();
    }

    public ContainerType<? extends AbstractContainerMachine> getContainerType() {
        return this.containerType.get();
    }

    public void setCustomName(ITextComponent pName) {
        this.name = pName;
    }

    @Override
    public ITextComponent getName() {
        return this.name != null ? this.name : this.getDefaultName();
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.getName();
    }

    @Override
    @Nullable
    public ITextComponent getCustomName() {
        return this.name;
    }

    protected ITextComponent getDefaultName() {
        return this.defaultName;
    }

    public BlockPos getPosOfTileEntity() {
        BlockState blockState = this.level != null ? this.level.getBlockState(this.worldPosition) : Blocks.AIR.defaultBlockState();
        if (blockState.getBlock() instanceof AbstractBlockMultiTile) {
            return ((AbstractBlockMultiTile)blockState.getBlock()).getMultiblockCenter(level, this.worldPosition, blockState);
        }
        else {
            return this.worldPosition;
        }
    }

    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ContainerLauncherPlatform(getContainerType(), i, this.getLevel(), getPosOfTileEntity(), playerInventory);
    }

    public boolean redstoneSignalPresent() {
        assert level != null;
        return level.hasNeighborSignal(getBlockPos());
    }

    public int ingestEnergy(int maxAmount, Direction[] directions) {
        if(level == null) return 0;
        int totalRequesting = maxAmount;
        for(int i = 0; totalRequesting > 0 && i < directions.length; ++i) {
            Direction direction = directions[i];
            TileEntity blockEntity = level.getBlockEntity(getBlockPos().relative(direction));
            if(blockEntity != null) {
                LazyOptional<IEnergyStorage> neighborCapOptional = blockEntity.getCapability(ICBMReference.FORGE_ENERGY_CAPABILITY, direction.getOpposite());
                if(neighborCapOptional.isPresent()) {
                    IEnergyStorage neighborCap = neighborCapOptional.orElse(null);
                    totalRequesting -= neighborCap.extractEnergy(totalRequesting, false);
                }
            }
        }
        return maxAmount - totalRequesting;
    }

    public boolean tryConsumeEnergy(int energyToConsume) {
        ICBMEnergyStorage energyStorage = (ICBMEnergyStorage)energyStorageLazyOptional.orElse(null);
        int energyWouldBeConsumed = energyStorage.extractEnergyUnchecked(energyToConsume, true);
        if(energyWouldBeConsumed >= energyToConsume) {
            energyStorage.extractEnergyUnchecked(energyToConsume, false);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        if(level != null && level.isClientSide()) {
            handleUpdateTag(getBlockState(), pkt.getTag());
            ICBMReference.distProxy().updateScreenMachine();
        }
    }

    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
    }

}
