package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.common.capability.ICBMCapabilities;
import com.jdawg3636.icbm.common.capability.missiledirector.MissileLaunchPhase;
import com.jdawg3636.icbm.common.reg.ItemReg;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class EventBlastAntiballistic extends AbstractBlastEvent {

    public static final double ANTIBALLISTIC_BLAST_RADIUS = 20;

    public EventBlastAntiballistic(BlockPos blastPosition, ServerWorld blastWorld, Type blastType, Direction blastDirection) {
        super(blastPosition, blastWorld, blastType, blastDirection);
    }

    @Override
    public boolean executeBlast() {
        ICBMBlastEventUtil.doBlastSoundAndParticles(this);
        this.getBlastWorld().getCapability(ICBMCapabilities.MISSILE_DIRECTOR_CAPABILITY).ifPresent(md -> {
            md.getLogicalMissiles().values().stream()
                .filter(lm -> lm.missileLaunchPhase == MissileLaunchPhase.LAUNCHED && !lm.missileItem.equals(ItemReg.MISSILE_ANTIBALLISTIC))
                .collect(Collectors.toCollection(ArrayList::new)) // Need this to avoid ConcurrentModificationException
                .forEach(lm -> {
                    double deltaX = getBlastPosition().getX() - lm.x;
                    double deltaZ = getBlastPosition().getZ() - lm.z;
                    double horizDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
                    if(horizDistance < ANTIBALLISTIC_BLAST_RADIUS) {
                        md.deleteMissile(lm);
                    }
                });
        });
        return true;
    }

}
