package icbm.classic.client.fx;

import icbm.classic.lib.transform.vector.Pos;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ParticleSmokeICBM extends SmokeParticle {

    public ParticleSmokeICBM(ClientWorld worldIn, Pos pos, double vx, double vy, double vz, float scale, IAnimatedSprite sprite) {
        super(worldIn, pos.x(), pos.y(), pos.z(), vx, vy, vz, scale, sprite);
    }

    public ParticleSmokeICBM setAge(int age) {
        this.maxAge = age;
        return this;
    }

    public ParticleSmokeICBM setColor(float r, float g, float b, boolean addColorVariant) {

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

}