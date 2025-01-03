package com.jdawg3636.icbm.common.block.machine;

import com.jdawg3636.icbm.ICBMReference;
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
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.function.Supplier;

public class TileMachine extends TileEntity implements INamedContainerProvider, INameable {

    private ITextComponent name;
    private final ITextComponent defaultName;
    private final Supplier<ContainerType<? extends AbstractContainerMachine>> containerType;
    private final AbstractContainerMachine.IConstructor containerConstructor;
    private final ICBMItemStackHandler itemHandler;
    public final LazyOptional<IItemHandler> itemHandlerLazyOptional;
    private final ICBMEnergyStorage energyStorage;
    public final LazyOptional<IEnergyStorage> energyStorageLazyOptional;
    public final ArrayList<LazyOptional<FluidTank>> fluidTanks;

    public TileMachine(TileEntityType<?> tileEntityTypeIn, Supplier<ContainerType<? extends AbstractContainerMachine>> containerType, AbstractContainerMachine.IConstructor containerConstructor) {
        this(tileEntityTypeIn, containerType, containerConstructor, 0, 0, 0, 0, new ArrayList<>(), new TranslationTextComponent("gui." + ICBMReference.MODID + ".unspecialized_machine"));
    }

    public TileMachine(TileEntityType<?> tileEntityTypeIn, Supplier<ContainerType<? extends AbstractContainerMachine>> containerType, AbstractContainerMachine.IConstructor containerConstructor, int inventorySize, int forgeEnergyCapacity, int forgeEnergyPerTickIn, int forgeEnergyPerTickOut, ArrayList<LazyOptional<FluidTank>> fluidTanks, ITextComponent defaultName) {
        super(tileEntityTypeIn);
        this.containerType = containerType;
        this.containerConstructor = containerConstructor;

        if(inventorySize > 0) {
            itemHandler = createItemStackHandler(inventorySize);
            itemHandlerLazyOptional = LazyOptional.of(() -> itemHandler);
        }
        else {
            itemHandler = null;
            itemHandlerLazyOptional = LazyOptional.empty();
        }

        if(forgeEnergyCapacity > 0 || forgeEnergyPerTickIn > 0 || forgeEnergyPerTickOut > 0) {
            this.energyStorage = new ICBMEnergyStorage().setCapacity(forgeEnergyCapacity, false).setMaxReceive(forgeEnergyPerTickIn).setMaxExtract(forgeEnergyPerTickOut).setCallbackOnChanged(this::setChanged);
            this.energyStorageLazyOptional = LazyOptional.of(() -> energyStorage);
        }
        else {
            this.energyStorage = null;
            this.energyStorageLazyOptional = LazyOptional.empty();
        }

        this.fluidTanks = fluidTanks;

        this.defaultName = defaultName;

    }

    /*
    * Note! This must be called by the block. This is done by default in AbstractBlockMachineTile and AbstractBlockMultiTile.
    * */
    public void onBlockDestroyed() {
        if(level != null && itemHandler != null) {
            itemHandler.setBeingDestroyed();
            for(int i = 0; i < itemHandler.getSlots(); ++i) {
                ItemStack contents = itemHandler.getStackInSlot(i);
                itemHandler.setStackInSlot(i, ItemStack.EMPTY);
                InventoryHelper.dropItemStack(level, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), contents);
            }
        }
    }

    protected void onInventorySlotChanged(int slot, boolean isBeingDestroyed) {
        if(!isBeingDestroyed) {
            setChanged();
        }
    }

    public boolean isInventoryItemValid(int slot, ItemStack stack) {
        return true;
    }

    private ICBMItemStackHandler createItemStackHandler(int inventorySize) {
        return new ICBMItemStackHandler(inventorySize, this::onInventorySlotChanged, this::isInventoryItemValid);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(itemHandlerLazyOptional.isPresent() && cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) return itemHandlerLazyOptional.cast();
        if(energyStorageLazyOptional.isPresent() && cap.equals(CapabilityEnergy.ENERGY)) return energyStorageLazyOptional.cast();
        if(!fluidTanks.isEmpty() && cap.equals(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)) return fluidTanks.get(0).cast(); // TODO: if a future machine requires it, support multiple tanks.
        return LazyOptional.empty();
    }

    @Nonnull
    public <T> LazyOptional<T> getCapabilityViaProxy(@Nonnull Capability<T> cap, @Nullable Direction side, BlockState proxyState) {
        return getCapability(cap, side);
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
        if(tag.contains("fluids") && !fluidTanks.isEmpty()) {
            CompoundNBT fluidsNBT = tag.getCompound("fluids");
            for(String key : fluidsNBT.getAllKeys()) {
                int i = -1;
                try {
                    i = Integer.parseInt(key);
                } catch (NumberFormatException exception) {
                    ICBMReference.logger().debug("Invalid key for fluid tank: {}", key);
                }
                if(i == -1) continue;
                if(i < fluidTanks.size()) {
                    CompoundNBT currentFluidNBT = fluidsNBT.getCompound(key);
                    fluidTanks.get(i).ifPresent(fluidTank -> fluidTank.readFromNBT(currentFluidNBT));
                }
            }
        }
        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        if (this.name != null) tag.putString("CustomName", ITextComponent.Serializer.toJson(this.name));
        if(itemHandlerLazyOptional.isPresent()) tag.put("inv", itemHandler.serializeNBT());
        if(energyStorageLazyOptional.isPresent()) tag.put("energy", energyStorage.serializeNBT());
        final CompoundNBT fluidsNBT = new CompoundNBT();
        for(int i = 0; i < fluidTanks.size(); ++i) {
            final int finalI = i;
            fluidTanks.get(i).ifPresent(fluidTank -> {
                CompoundNBT currentFluidNBT = new CompoundNBT();
                fluidTank.writeToNBT(currentFluidNBT);
                fluidsNBT.put("" + finalI, currentFluidNBT);
            });
        }
        if(!fluidTanks.isEmpty()) tag.put("fluids", fluidsNBT);
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
        return ICBMReference.getTileEntityUpdateDistance();
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
        return containerConstructor.construct(getContainerType(), i, this.getLevel(), getPosOfTileEntity(), playerInventory);
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

    public int sendEnergy(int maxAmount, Direction[] directions) {
        int amountUnsent = maxAmount;
        for(Direction direction : directions) {
            amountUnsent -= sendEnergy(amountUnsent, direction);
        }
        return maxAmount - amountUnsent;
    }

    public int sendEnergy(int maxAmount, Direction direction) {
        if(level == null) return 0;
        TileEntity blockEntity = level.getBlockEntity(getBlockPos().relative(direction));
        if(blockEntity != null) {
            LazyOptional<IEnergyStorage> neighborCapOptional = blockEntity.getCapability(ICBMReference.FORGE_ENERGY_CAPABILITY, direction.getOpposite());
            if(neighborCapOptional.isPresent()) {
                IEnergyStorage neighborCap = neighborCapOptional.orElse(null);
                return neighborCap.receiveEnergy(maxAmount, false);
            }
        }
        return 0;
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

    public int tryReceiveEnergy(IEnergyStorage source, int energyToReceive) {
        if(!energyStorageLazyOptional.isPresent() || !energyStorage.canReceive()) return 0;
        energyToReceive = Math.min(Math.min(energyToReceive, energyStorage.getMaxReceive()), energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored());
        int energyExtracted = source.extractEnergy(energyToReceive, false);
        int energyReceived = energyStorage.receiveEnergy(energyExtracted, false);
        if(energyExtracted != energyReceived) {
            ICBMReference.logger().warn(String.format("(This is a bug, please report to mod author!) Error in energy reception logic for %s! Should have received %s, but instead received %s.", this, energyExtracted, energyReceived));
        }
        return energyReceived;
    }

    public void setChanged() {
        super.setChanged();
        this.updateNearbyClients();
    }

    public void updateNearbyClients() {
        if(level != null && !level.isClientSide()) {
            SUpdateTileEntityPacket updatePacket = this.getUpdatePacket();
            if (updatePacket != null && level.getServer() != null) {
                level.getServer().getPlayerList().broadcast(null, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), ICBMReference.getTileEntityUpdateDistance(), level.dimension(), updatePacket);
            }
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        if(level != null && level.isClientSide()) {
            handleUpdateTag(getBlockState(), pkt.getTag());
            ICBMReference.distProxy().updateScreenMachine(pkt.getPos());
        }
    }

    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        if(level == null || level.getBlockEntity(worldPosition) == null) return null;
        return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
    }

}
