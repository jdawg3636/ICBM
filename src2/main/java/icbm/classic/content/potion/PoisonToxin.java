package icbm.classic.content.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;

public class PoisonToxin extends CustomPotion {

    public static Potion INSTANCE;

    public PoisonToxin(boolean isBadEffect, int color, int id, String name) {
        super(isBadEffect, color, id, name);
        this.setIconIndex(6, 0);
    }

    @Override
    public void performEffect(LivingEntity par1EntityLiving, int amplifier) {
        if (!(par1EntityLiving instanceof ZombieEntity) && !(par1EntityLiving instanceof ZombifiedPiglinEntity))
            par1EntityLiving.attackEntityFrom(DamageSource.MAGIC, 1);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % (20 * 2) == 0;
    }

}
