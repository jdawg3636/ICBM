package icbm.classic.api.data;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

/**
 * Created by Dark(DarkGuardsman, Robert) on 1/7/19.
 */
@FunctionalInterface
public interface EntityInteractionFunction {
    boolean onInteraction(Entity entity, PlayerEntity player, Hand hand);
}
