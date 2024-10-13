package com.jdawg3636.icbm.common.block.radar_station;

import com.jdawg3636.icbm.common.block.emp_tower.TileEMPTower;
import com.jdawg3636.icbm.common.capability.ICBMCapabilities;
import com.jdawg3636.icbm.common.capability.missiledirector.IMissileDirectorCapability;
import com.jdawg3636.icbm.common.capability.missiledirector.LogicalMissile;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * I am directly extending {@link TileEMPTower} since it already has logic for spinning in a circle a storing a "radius"
 * field synced over the network and saved as NBT data. Doing this is, of course, a bad idea, since a radar station is
 * not in fact an EMP tower, and the EMP tower could later implement functionality that is NOT shared by the radar station
 * and would need to be explicitly overridden here to not inherit. I'm doing it anyway though, so get off my ass <3
 */
public class TileRadarStation extends TileEMPTower implements ITickableTileEntity {

    public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("gui.icbm.radar_station");

    public UUID capabilityListenerUUID; // this variable is deliberately not serialized
    public Set<UUID> triggeringLogicalMissiles = new HashSet<>();

    public TileRadarStation(TileEntityType<?> tileEntityType) {
        super(tileEntityType, DEFAULT_NAME);
    }

    @Override
    public void tick() {
        super.tickSpinnyThing();
        this.tryConsumeEnergy(1_000);
    }

    public void onMissileUpdated(UUID missileID, LogicalMissile logicalMissile) {
        assert level != null;
        double distance = Math.sqrt(getPosOfTileEntity().above().distSqr(logicalMissile.blockPosition()));
        if(!logicalMissile.removed && distance < this.getEMPRadius()) {
            triggeringLogicalMissiles.add(missileID);
        }
        else {
            triggeringLogicalMissiles.remove(missileID);
        }
        BlockPos pos = getPosOfTileEntity();
        BlockState state = level.getBlockState(pos);
        this.level.updateNeighborsAt(pos, state.getBlock());
    }

    public int getRedstoneStrength() {
        if(triggeringLogicalMissiles.isEmpty()) return 0;
        if(energyStorageLazyOptional.orElse(null).getEnergyStored() <= 0) return 0;
        return 15;
    }

    @Override
    public void setLevelAndPosition(World level, BlockPos worldPosition) {
        super.setLevelAndPosition(level, worldPosition);
        getMissileDirector().ifPresent(md -> this.capabilityListenerUUID = md.registerListener(this::onMissileUpdated));
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        getMissileDirector().ifPresent(md -> md.removeListener(this.capabilityListenerUUID));
    }

    @Override
    public String getRadiusSliderText() {
        return "gui.icbm.radar_station.range";
    }

    private LazyOptional<IMissileDirectorCapability> getMissileDirector() {
        return level != null ? level.getCapability(ICBMCapabilities.MISSILE_DIRECTOR_CAPABILITY) : LazyOptional.empty();
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        this.triggeringLogicalMissiles = new HashSet<>();
        // Deserialize "triggeringLogicalMissiles" ListNBT tag into this.triggeringLogicalMissiles
        Optional.ofNullable(tag.get("triggeringLogicalMissiles")).map(ListNBT.class::cast).ifPresent(list -> list.forEach(inbt -> this.triggeringLogicalMissiles.add(NBTUtil.loadUUID(inbt))));
        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        ListNBT triggeringLogicalMissilesNBT = new ListNBT();
        triggeringLogicalMissiles.stream().map(NBTUtil::createUUID).forEach(triggeringLogicalMissilesNBT::add);
        tag.put("triggeringLogicalMissiles", triggeringLogicalMissilesNBT);
        return super.save(tag);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox().expandTowards(0, 1, 0).inflate(1, 0, 1);
    }

}
