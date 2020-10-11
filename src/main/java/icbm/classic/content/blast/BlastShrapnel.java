package icbm.classic.content.blast;

import icbm.classic.content.entity.EntityFragments;
import net.minecraft.util.math.MathHelper;

public class BlastShrapnel extends Blast {

    private boolean isExplosive = false;
    private boolean isAnvil = false;

    public BlastShrapnel(){}

    public BlastShrapnel setFlaming() {
        this.causesFire = true; //TODO convert to factory
        return this;
    }

    public BlastShrapnel setExplosive() {
        this.isExplosive = true; //TODO convert to factory
        return this;
    }

    public BlastShrapnel setAnvil() {
        this.isAnvil = true; //TODO convert to factory
        return this;
    }

    @Override
    public boolean doExplode(int callCount) {

        if (!world().isRemote) {

            float amountToRotate = 360 / this.getBlastRadius();

            for (int i = 0; i < this.getBlastRadius(); i++) {

                // Try to do a 360 explosion on all 6 faces of the cube.
                float rotationYaw = 0.0F + amountToRotate * i;

                for (int ii = 0; ii < this.getBlastRadius(); ii++) {

                    //TODO convert to factory
                    EntityFragments arrow = new EntityFragments(world(), location.x(), location.y(), location.z(), this.isExplosive, this.isAnvil);

                    if (this.causesFire) {
                        arrow.arrowCritical = true;
                        arrow.setFire(100);
                    }

                    float rotationPitch = 0.0F + amountToRotate * ii;

                    arrow.setLocationAndAngles(
                            location.x(),
                            location.y(),
                            location.z(),
                            rotationYaw,
                            rotationPitch
                    );

                    arrow.setPosition(
                            arrow.getPosX() - (MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * 0.16F),
                            arrow.getPosY() - 0.10000000149011612D,
                            arrow.getPosZ() - (MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * 0.16F)
                    );

                    //arrow.yOffset = 0.0F;

                    arrow.setMotion(
                            -MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI),
                            MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI),
                            -MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI)
                    );

                    arrow.setArrowHeading(
                            arrow.getMotion().getX() * world().rand.nextFloat(),
                            arrow.getMotion().getY() * world().rand.nextFloat(),
                            arrow.getMotion().getZ() * world().rand.nextFloat(), 0.5f + (0.7f * world().rand.nextFloat()), 1.0F
                    );

                    world().addEntity(arrow);

                }

            }

        }

        return false;

    }

}
