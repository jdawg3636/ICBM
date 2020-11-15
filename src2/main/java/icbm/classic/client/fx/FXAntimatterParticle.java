package icbm.classic.client.fx;

import icbm.classic.lib.transform.vector.Pos;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FXAntimatterParticle extends SpriteTexturedParticle {

    public FXAntimatterParticle(ClientWorld par1World, Pos position, double par8, double par10, double par12, double distance) {
        this(par1World, position, par8, par10, par12, 1.0F, distance);
    }

    public FXAntimatterParticle(ClientWorld par1World, Pos position, double par8, double par10, double par12, float par14, double distance) {
        super(par1World, position.x(), position.y(), position.z(), 0.0D, 0.0D, 0.0D);
        this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.motionX += par8;
        this.motionY += par10;
        this.motionZ += par12;
        this.particleRed = this.particleGreen = this.particleBlue = (float) (Math.random() * 0.30000001192092896D);
        this.particleScale *= 0.75F;
        this.particleScale *= par14;
        this.maxAge = (int) (10D / (Math.random() * 0.8D + 0.2D));
        this.maxAge = (int) (this.maxAge * par14);
    }

    public int getBrightnessForRender(float p_189214_1_) {
        return 240;
    }

    @Override
    public IParticleRenderType getRenderType() {
        // Not sure on this, guessing same type used in the superclass of other ICBM particles (10/31/2020)
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    /** Called to update the entity's position/logic. */
    @Override
    public void tick() {

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.age++ >= this.maxAge)
            this.setExpired();

    }

}
