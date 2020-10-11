package icbm.classic.content.blast;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class BlastMutation extends Blast {

    @Override
    public boolean doExplode(int callCount) {

        if (!this.world().isRemote) {

            AxisAlignedBB bounds = new AxisAlignedBB(location.x() - this.getBlastRadius(), location.y() - this.getBlastRadius(), location.z() - this.getBlastRadius(), location.x() + this.getBlastRadius(), location.y() + this.getBlastRadius(), location.z() + this.getBlastRadius());
            List<LivingEntity> entitiesNearby = world().getEntitiesWithinAABB(LivingEntity.class, bounds);

            for (LivingEntity entity : entitiesNearby) {

                if (entity instanceof PigEntity) {
                    ZombifiedPiglinEntity newEntity = new ZombifiedPiglinEntity(EntityType.field_233592_ba_, world());
                    newEntity.preventEntitySpawning = true;
                    newEntity.setPosition(entity.getPosX(), entity.getPosY(), entity.getPosZ());
                    entity.remove();
                    world().addEntity(newEntity);
                }
                else if (entity instanceof VillagerEntity) {
                    ZombieVillagerEntity newEntity = new ZombieVillagerEntity(EntityType.ZOMBIE_VILLAGER, world());
                    newEntity.preventEntitySpawning = true;
                    newEntity.setPosition(entity.getPosX(), entity.getPosY(), entity.getPosZ());
                    newEntity.setVillagerData(((VillagerEntity) entity).getVillagerData());
                    entity.remove();
                    world().addEntity(newEntity);
                }

            }

        }

        return false;

    }

    @Override //disable the sound for this explosive
    protected void playExplodeSound() {}

}
