package icbm.classic.client.fx;

import icbm.classic.lib.transform.vector.Pos;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Same as normal smoke, but doesn't move upwards on its own
 */
@OnlyIn(Dist.CLIENT)
public class ParticleAirICBM extends SmokeParticle {

    public ParticleAirICBM(ClientWorld worldIn, Pos pos, double vx, double vy, double vz, float scale, IAnimatedSprite sprite) {
        super(worldIn, pos.x(), pos.y(), pos.z(), vx, vy, vz, scale, sprite);
    }

    public ParticleAirICBM setAge(int age) {
        this.maxAge = age;
        return this;
    }

    public ParticleAirICBM setColor(float r, float g, float b, boolean addColorVariant) {

        this.particleRed = r;
        this.particleGreen = g;
        this.particleBlue = b;

        if (addColorVariant) {
            float colorVariant = (float) (Math.random() * 0.90000001192092896D);
            this.particleRed *= colorVariant;
            this.particleBlue *= colorVariant;
            this.particleGreen *= colorVariant;
        }

        return this;

    }

    @Override
    public void tick() /* same code as in vanilla particle, but the vertical velocity acceleration is set to 0 */ {

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.age++ >= this.maxAge) {
            this.setExpired();
        } else {
            this.selectSpriteWithAge(this.field_239175_a_);
            //this.motionY += this.field_239176_b_;
            this.move(this.motionX, this.motionY, this.motionZ);
            if (this.posY == this.prevPosY) {
                this.motionX *= 1.1D;
                this.motionZ *= 1.1D;
            }

            this.motionX *= 0.9599999785423279D;
            this.motionY *= 0.9599999785423279D;
            this.motionZ *= 0.9599999785423279D;
            if (this.onGround) {
                this.motionX *= 0.699999988079071D;
                this.motionZ *= 0.699999988079071D;
            }

        }

    }

}