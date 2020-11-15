package icbm.classic.content.blast;

import icbm.classic.api.explosion.IBlastInit;
import icbm.classic.api.explosion.IBlastTickable;
import icbm.classic.lib.transform.vector.Location;
import icbm.classic.lib.transform.vector.Pos;
import icbm.classic.ICBMClassic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.List;

public class BlastEnderman extends Blast implements IBlastTickable {

    public static final String NBT_LOCATION = "teleport_target";
    public int duration = 20 * 8;
    private Pos teleportTarget;

    @Override
    public IBlastInit setCustomData(@Nonnull CompoundNBT customData) {
        if (customData != null && customData.contains(NBT_LOCATION)) {
            teleportTarget = new Pos(customData.getCompound(NBT_LOCATION));
            //TODO load world ID
            //TODO data fixer, previous data was store raw as xyz
        }
        return this;
    }

    @Override
    public boolean doExplode(int callCount) {
        if (this.world().isRemote) {

            int r = (int) (this.getBlastRadius() - ((double) this.callCount / (double) this.duration) * this.getBlastRadius());

            for (int x = -r; x < r; x++) {
                for (int z = -r; z < r; z++) {
                    for (int y = -r; y < r; y++) {

                        Location targetPosition = location.add(new Pos(x, y, z)); //TODO replace with mutable blockpos

                        double distance = targetPosition.distance(location);

                        if (distance < r && distance > r - 1) {

                            if (!targetPosition.getBlock(world()).isAir(targetPosition.getBlockState(world()), world(), targetPosition.toBlockPos()))
                                continue;

                            if (this.world().rand.nextFloat() < Math.max(0.001 * r, 0.01)) {

                                float velX = (float) ((targetPosition.x() - location.x()) * 0.6);
                                float velY = (float) ((targetPosition.y() - location.y()) * 0.6);
                                float velZ = (float) ((targetPosition.z() - location.z()) * 0.6);

                                world.addParticle(ParticleTypes.PORTAL, targetPosition.x(), targetPosition.y(), targetPosition.z(), velX, velY, velZ);

                            }

                        }

                    }
                }
            }

        }

        int radius = (int) this.getBlastRadius();
        AxisAlignedBB bounds = new AxisAlignedBB(location.x() - radius, location.y() - radius, location.z() - radius, location.x() + radius, location.y() + radius, location.z() + radius);
        List<Entity> allEntities = world().getEntitiesWithinAABB(Entity.class, bounds);
        boolean explosionCreated = false;

        for (Entity entity : allEntities) {
            if (entity != this.controller) {

                double xDifference = entity.getPosX() - location.x();
                double yDifference = entity.getPosY() - location.y();
                double zDifference = entity.getPosZ() - location.z();

                int r = (int) this.getBlastRadius();
                if (xDifference < 0)
                    r = (int) -this.getBlastRadius();

                entity.setMotion(entity.getMotion().add(-((r - xDifference) * Math.abs(xDifference) * 0.0006), 0 ,0));

                r = (int) this.getBlastRadius();
                if (entity.getPosY() > location.y())
                    r = (int) -this.getBlastRadius();
                entity.setMotion(entity.getMotion().add(0, (r - yDifference) * Math.abs(yDifference) * 0.0011, 0));

                r = (int) this.getBlastRadius();
                if (zDifference < 0)
                    r = (int) -this.getBlastRadius();

                entity.setMotion(entity.getMotion().add(0, 0, -((r - zDifference) * Math.abs(zDifference) * 0.0006)));

                if (new Pos(entity.getPosX(), entity.getPosY(), entity.getPosZ()).distance(location) < 4) {

                    if (!explosionCreated && callCount % 5 == 0) {
                        world().addParticle(ParticleTypes.EXPLOSION, entity.getPosX(), entity.getPosY(), entity.getPosZ(), 0.0D, 0.0D, 0.0D);
                        explosionCreated = true;
                    }

                    try {
                        /** If a target doesn't exist, search for a random one within 100 block
                         * range. */
                        if (this.teleportTarget == null) {

                            int checkY = (int) Math.floor(this.controller.getPosY());
                            int checkX = this.world().rand.nextInt(300) - 150 + (int) this.controller.getPosX();
                            int checkZ = this.world().rand.nextInt(300) - 150 + (int) this.controller.getPosZ();

                            //Look for space with air gap
                            BlockPos pos;
                            BlockPos pos2;
                            do {
                                pos = new BlockPos(checkX, checkY, checkZ);
                                pos2 = pos.up();
                                checkY++;
                            } while (this.world().isAirBlock(pos) && !this.world().isAirBlock(pos2) && checkY < 254);

                            this.teleportTarget = new Pos(checkX, checkY, checkZ);

                        }

                        this.world().playSound(null, entity.getPosX(), entity.getPosY(), entity.getPosZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1.0F, 1.0F);

                        if (entity instanceof ServerPlayerEntity)
                            ((ServerPlayerEntity) entity).connection.setPlayerLocation(this.teleportTarget.x() + 0.5, this.teleportTarget.y() + 0.5, this.teleportTarget.z() + 0.5, entity.rotationYaw, entity.rotationPitch);
                        else
                            entity.setPosition(this.teleportTarget.x() + 0.5, this.teleportTarget.y() + 0.5, this.teleportTarget.z() + 0.5);

                    } catch (Exception e) {
                        ICBMClassic.logger().error("Failed to teleport entity to the End.", e);
                    }

                }

            }
        }

        this.world().playSound(null, this.location.x(), this.location.y(), this.location.z(), SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 2F, world().rand.nextFloat() * 0.4F + 0.8F);

        return this.callCount > this.duration;

    }

    @Override
    public void onBlastCompleted() {

        super.onBlastCompleted();

        if (!this.world().isRemote) {
            for (int i = 0; i < 8; i++) /* TODO check for safe location to spawn */ {
                EndermanEntity enderman = new EndermanEntity(EntityType.ENDERMAN, world());
                enderman.setPosition(this.location.x(), this.location.y(), this.location.z());
                this.world().addEntity(enderman);
            }
        }

    }

    @Override
    public float getBlastRadius() {
        return 20;
    }

    @Override
    public boolean isMovable() {
        return true;
    }

}
