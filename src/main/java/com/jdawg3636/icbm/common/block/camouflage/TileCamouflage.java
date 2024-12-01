package com.jdawg3636.icbm.common.block.camouflage;

import com.jdawg3636.icbm.common.reg.BlockReg;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.Optional;

public class TileCamouflage extends TileEntity {

    public static final ModelProperty<BlockState> APPEARANCE = new ModelProperty<>();
    public static final ModelProperty<ArrayList<Direction>> TRANSPARENT_SIDES = new ModelProperty<>();
    public static final ModelProperty<TileCamouflage> TILE_ENTITY = new ModelProperty<>();

    private BlockState appearance;
    private ArrayList<Direction> transparentSides = new ArrayList<>();

    public TileCamouflage(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public void setAppearance(BlockState appearance) {
        this.appearance = appearance;
        setChanged();
    }

    public BlockState getAppearanceNoNull() {
        return appearance == null ? BlockReg.CAMOUFLAGE.get().defaultBlockState() : appearance;
    }

    public BlockState getAppearanceNoSelf() {
        return Optional.ofNullable(appearance)
                .filter(appearance -> !(appearance.getBlock() instanceof BlockCamouflage))
                .orElse(null);
    }

    public void toggleSideTransparency(Direction direction) {
        if(transparentSides.contains(direction)) transparentSides.remove(direction);
        else transparentSides.add(direction);
        setChanged();
    }

    public boolean isSideTransparent(Direction direction) {
        return transparentSides.contains(direction);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if(level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
            level.getChunkSource().getLightEngine().checkBlock(getBlockPos());
        }
    }

    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder()
            .withInitial(APPEARANCE, appearance)
            .withInitial(TRANSPARENT_SIDES, transparentSides)
            .withInitial(TILE_ENTITY, this)
            .build();
    }

    public void load(BlockState blockState, CompoundNBT compoundNBT) {

        super.load(blockState, compoundNBT);

        if(compoundNBT.contains("appearance")) {
            this.setAppearance(NBTUtil.readBlockState(compoundNBT.getCompound("appearance")));
        }
        else {
            this.setAppearance(null);
        }

        this.transparentSides = new ArrayList<>();
        int transparentSidesPacked = compoundNBT.getInt("transparent_sides");
        for(Direction potentialDirection : Direction.values()) {
            int bit = 1 << potentialDirection.ordinal();
            if((transparentSidesPacked | bit) == transparentSidesPacked) {
                this.toggleSideTransparency(potentialDirection);
            }
        }

    }

    public CompoundNBT save(CompoundNBT compoundNBT) {

        CompoundNBT result = super.save(compoundNBT);

        if(this.appearance != null) {
            result.put("appearance", NBTUtil.writeBlockState(this.appearance));
        }

        int transparentSidesPacked = 0;
        for(Direction direction : this.transparentSides) {
            transparentSidesPacked = transparentSidesPacked | (1 << direction.ordinal());
        }
        result.putInt("transparent_sides", transparentSidesPacked);

        return result;

    }

    @Override
    public CompoundNBT getUpdateTag() {
        return save(new CompoundNBT());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        handleUpdateTag(getBlockState(), packet.getTag());
        ModelDataManager.requestModelDataRefresh(this);
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
        level.getChunkSource().getLightEngine().checkBlock(getBlockPos());
    }

}
