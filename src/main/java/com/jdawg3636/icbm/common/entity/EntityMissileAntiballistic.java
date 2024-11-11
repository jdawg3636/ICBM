package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.common.capability.missiledirector.LogicalMissile;
import com.jdawg3636.icbm.common.capability.missiledirector.MissileLaunchPhase;
import com.jdawg3636.icbm.common.event.BlastEventRegistryEntry;
import com.jdawg3636.icbm.common.reg.ItemReg;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class EntityMissileAntiballistic extends EntityMissile {

    // todo make these configurable
    public static final double ANTIBALLISTIC_RANGE_LIMIT = 150;
    public static final double ANTIBALLISTIC_SPEED_RATIO = 2;

    public EntityMissileAntiballistic(EntityType<?> entityTypeIn, World worldIn, RegistryObject<BlastEventRegistryEntry> blastEventProvider, RegistryObject<Item> missileItem) {
        super(entityTypeIn, worldIn, blastEventProvider, missileItem);
    }

    public EntityMissileAntiballistic(EntityType<?> entityTypeIn, World worldIn, Function<UUID, Optional<LogicalMissile>> logicalMissileConstructor, RegistryObject<Item> missileItem, Optional<UUID> logicalUUID) {
        super(entityTypeIn, worldIn, logicalMissileConstructor, missileItem, logicalUUID);
    }

    @Override
    public boolean launchMissile() {
        AtomicBoolean launched = new AtomicBoolean(false);
        getLogicalMissile().ifPresent(currentLM -> {
            getMissileDirector().ifPresent(md -> {
                md.getLogicalMissiles().entrySet().stream()
                    .filter(targetLM ->
                        (targetLM.getValue().missileLaunchPhase == MissileLaunchPhase.LAUNCHED)
                        && (currentLM.sourcePos.distSqr(targetLM.getValue().x, targetLM.getValue().y, targetLM.getValue().z, true) < ANTIBALLISTIC_RANGE_LIMIT * ANTIBALLISTIC_RANGE_LIMIT)
                        && (!targetLM.getValue().missileItem.equals(ItemReg.MISSILE_ANTIBALLISTIC))
                        && (!targetLM.getValue().equals(currentLM))
                    )
                    .findFirst().ifPresent(targetLM -> {
                        // Calculate new path for interception.
                        // Interceptor source, target source, and interception point must form an isosceles triangle.
                        // Missiles are assumed to travel 1 block per tick.
                        double horizDistance = Math.sqrt(currentLM.blockPosition().distSqr(targetLM.getValue().blockPosition()));
                        BlockPos destPos = new BlockPos(targetLM.getValue().pathFunction.apply((int)horizDistance));
                        float peakHeight = (float)(Math.max(currentLM.y, destPos.getY()) + 1);
                        this.updateMissileData(currentLM.sourcePos, destPos, peakHeight, (int)horizDistance, currentLM.missileSourceType);
                        // Launch
                        launched.set(super.launchMissile());
                    });
            });
        });
        return launched.get();
    }

}
