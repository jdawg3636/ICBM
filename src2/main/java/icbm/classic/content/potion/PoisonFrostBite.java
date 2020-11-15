package icbm.classic.content.potion;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;

public class PoisonFrostBite extends CustomPotion {

    public static Potion INSTANCE;

    public PoisonFrostBite(boolean isBadEffect, int color, int id, String name) {
        super(isBadEffect, color, id, name);
        this.setIconIndex(6, 0);
    }

    @Override
    public void performEffect(LivingEntity entity, int amplifier) {

        if (entity.isBurning())
            entity.extinguish();

        if (!(entity instanceof PlayerEntity) || !((PlayerEntity) entity).isCreative()) {

            if (entity instanceof PlayerEntity)
                ((PlayerEntity) entity).addExhaustion(3F * (amplifier + 1));

            // Check to see if it's on ice
            if (entity.world.getBlockState(new BlockPos(entity.getPositionVec())).getBlock() == Blocks.ICE)
                entity.attackEntityFrom(DamageSource.MAGIC, 2);

        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {

        if (duration % 20 == 0)
            return true;

        return false;

    }

}
