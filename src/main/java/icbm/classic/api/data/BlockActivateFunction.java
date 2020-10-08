package icbm.classic.api.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Dark(DarkGuardsman, Robert) on 1/7/19.
 */
@FunctionalInterface
public interface BlockActivateFunction {
    boolean onActivate(World world, BlockPos pos, PlayerEntity entityPlayer, Hand hand, Direction facing, float hitX, float hitY, float hitZ);
}
