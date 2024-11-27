package com.jdawg3636.icbm.common.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.RisingParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

// Based on net.minecraft.client.particle.SmokeParticle
public class ColoredSmokeParticle extends RisingParticle {

    public boolean fullbright = false;

    public ColoredSmokeParticle(ClientWorld level, double posX, double posY, double posZ, double speedXOffset, double speedYOffset, double speedZOffset, float quadSizeMultiplier, IAnimatedSprite sprites) {
        this(level, posX, posY, posZ, 0.1F, 0.1F, 0.1F, speedXOffset, speedYOffset, speedZOffset, quadSizeMultiplier, sprites, 0.3F, 8, 0.004D, false);
    }

    // Provided speed multipliers and offsets are relative to a randomly-generated base speed. As of MC 1.16.5, this base speed is +/- 0.4F on each axis.
    public ColoredSmokeParticle(ClientWorld level, double posX, double posY, double posZ, float speedXMultiplier, float speedYMultiplier, float speedZMultiplier, double speedXOffset, double speedYOffset, double speedZOffset, float quadSizeMultiplier, IAnimatedSprite sprites, float maxColorIntensity, int oneFifthOfMaximumLifetimeInTicks, double fallSpeed, boolean hasPhysics) {
        super(level, posX, posY, posZ, speedXMultiplier, speedYMultiplier, speedZMultiplier, speedXOffset, speedYOffset, speedZOffset, quadSizeMultiplier, sprites, maxColorIntensity, oneFifthOfMaximumLifetimeInTicks, fallSpeed, hasPhysics);
    }

    protected int getLightColor(float pPartialTick) {
        return this.fullbright ? 15728880 : super.getLightColor(pPartialTick);
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

        public Particle createParticle(BasicParticleType basicParticleType, ClientWorld level, double posX, double posY, double posZ, double speedXOffset, double speedYOffset, double speedZOffset) {
            Particle particle =  new ColoredSmokeParticle(level, posX, posY, posZ, speedXOffset, speedYOffset, speedZOffset, 1.0F, this.sprites);
            particle.scale(10F);
            particle.setColor(colorRed, colorGreen, colorBlue);
            return particle;
        }

    }

    public static class FactoryRadioactive extends Factory {

        public FactoryRadioactive(IAnimatedSprite sprites, int colorRed, int colorGreen, int colorBlue) {
            super(sprites, colorRed, colorGreen, colorBlue);
        }

        @Override
        public Particle createParticle(BasicParticleType basicParticleType, ClientWorld level, double posX, double posY, double posZ, double speedXOffset, double speedYOffset, double speedZOffset) {
            Particle particle = super.createParticle(basicParticleType, level, posX, posY, posZ, speedXOffset, speedYOffset, speedZOffset);
            ((ColoredSmokeParticle)particle).fallSpeed = 0;
            ((ColoredSmokeParticle)particle).fullbright = true;
            particle.scale(0.1F);
            return particle;
        }

    }

}
