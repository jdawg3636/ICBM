package icbm.classic.content.entity;

import icbm.classic.content.reg.EntityReg;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityExplosivesCondensed extends TNTEntity {

    public EntityExplosivesCondensed(EntityType<? extends EntityExplosivesCondensed> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityExplosivesCondensed(World worldIn, double x, double y, double z, @Nullable LivingEntity igniter) {
        this(EntityReg.EXPLOSIVES_CONDENSED.get(), worldIn);
        this.setPosition(x, y, z);
        double d0 = worldIn.rand.nextDouble() * (double)((float)Math.PI * 2F);
        this.setMotion(-Math.sin(d0) * 0.02D, (double)0.2F, -Math.cos(d0) * 0.02D);
        this.setFuse(80);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.tntPlacedBy = igniter;
    }

}
