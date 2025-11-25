package com.jdawg3636.icbm.common.block.launcher_control_panel;

import com.jdawg3636.icbm.common.item.ItemSignalDisruptor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITileLaunchControlPanel {

    public static enum MissileRemoteLaunchResult {
        SUCCESSFUL, UNSUPPORTED, WRONG_FREQUENCY, DISRUPTED, FAILED_LAUNCH
    }

    void setTargetX(double targetX);

    void setTargetZ(double targetZ);

    void setTargetY(double targetY);

    void setRadioFrequency(int radioFrequency);

    double getTargetX();

    double getTargetZ();

    double getTargetY();

    int getRadioFrequency();

    // Have to use different name, otherwise implementation gets remapped and the interface doesn't, causing java.lang.AbstractMethodError at runtime
    World deobf$getLevel();

    // Have to use different name, otherwise implementation gets remapped and the interface doesn't, causing java.lang.AbstractMethodError at runtime
    BlockPos deobf$getBlockPos();

    boolean launchMissile();

    // Don't want to use an abstract class so can't mark this as protected.
    default MissileRemoteLaunchResult launchMissileUsingFrequency(int frequency) {
        // Check Supported
        if(this.getRadioFrequency() == 0) {
            return MissileRemoteLaunchResult.UNSUPPORTED;
        }
        // Check Frequency
        if(frequency != getRadioFrequency()) {
            return MissileRemoteLaunchResult.WRONG_FREQUENCY;
        }
        // Check for Signal Disruptors
        final int disruptionRadius = 50; // TODO make this radius configurable
        final boolean isBeingDisrupted = !deobf$getLevel().getEntitiesOfClass(PlayerEntity.class, new AxisAlignedBB(deobf$getBlockPos()).inflate(disruptionRadius), entity -> {
            boolean targetIsValid = !entity.isSpectator();
            boolean signalIsBlocked = ItemSignalDisruptor.getFrequenciesDisruptedByPlayer((PlayerEntity) entity).contains(this.getRadioFrequency());
            return targetIsValid && signalIsBlocked;
        }).isEmpty();
        if(isBeingDisrupted) {
            return MissileRemoteLaunchResult.DISRUPTED;
        }
        // Launch missile
        boolean successful = this.launchMissile();
        return successful ? MissileRemoteLaunchResult.SUCCESSFUL : MissileRemoteLaunchResult.FAILED_LAUNCH;
    }

}
