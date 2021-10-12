package com.jdawg3636.icbm.common.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.RisingParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

// Based on net.minecraft.client.particle.SmokeParticle
public class ColoredSmokeParticle extends RisingParticle {

    public ColoredSmokeParticle(ClientWorld p_i232425_1_, double p_i232425_2_, double p_i232425_4_, double p_i232425_6_, double p_i232425_8_, double p_i232425_10_, double p_i232425_12_, float p_i232425_14_, IAnimatedSprite p_i232425_15_) {
        super(p_i232425_1_, p_i232425_2_, p_i232425_4_, p_i232425_6_, 0.1F, 0.1F, 0.1F, p_i232425_8_, p_i232425_10_, p_i232425_12_, p_i232425_14_, p_i232425_15_, 0.3F, 8, 0.004D, true);
    }

    public static class Factory implements IParticleFactory<BasicParticleType> {

        private final IAnimatedSprite sprites;

        private final float colorRed;
        private final float colorGreen;
        private final float colorBlue;

        public Factory(IAnimatedSprite sprites, int colorRed, int colorGreen, int colorBlue) {
            this.sprites    = sprites;
            this.colorRed   = colorRed   / (float)0xFF;
            this.colorGreen = colorGreen / (float)0xFF;
            this.colorBlue  = colorBlue  / (float)0xFF;
        }

        public Particle createParticle(BasicParticleType p_199234_1_, ClientWorld p_199234_2_, double p_199234_3_, double p_199234_5_, double p_199234_7_, double p_199234_9_, double p_199234_11_, double p_199234_13_) {
            Particle particle =  new ColoredSmokeParticle(p_199234_2_, p_199234_3_, p_199234_5_, p_199234_7_, p_199234_9_, p_199234_11_, p_199234_13_, 1.0F, this.sprites);
            particle.scale(10F);
            particle.setColor(colorRed, colorGreen, colorBlue);
            return particle;
        }

    }

}
