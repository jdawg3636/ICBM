package com.jdawg3636.icbm.common.event;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.particle_accelerator.TileParticleAccelerator;
import com.jdawg3636.icbm.common.recipe.ParticleAcceleratorRecipe;
import com.jdawg3636.icbm.common.reg.ICBMRecipeTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;

import java.util.Optional;

public class EventBlastAcceleratingParticle extends AbstractBlastEvent {

    // NOTE! The ExplosionCause::valueOf method is used for the deserialization of particle accelerator .json recipes,
    // so renaming any of these constants will break all recipes that use them. Don't do that <3
    public enum ExplosionCause {
        ELECTROMAGNETICALLY_UNSEALED,
        MAXIMUM_SPEED,
        COLLISION_WITH_ENTITY,
        COLLISION_WITH_BLOCK,
        DESTROYED_BY_OTHER_PARTICLE
    }

    private final ExplosionCause explosionCause;
    private final float particleSpeed;
    private final Optional<Float> otherParticleSpeed;
    private final TileParticleAccelerator particleAccelerator;

    public EventBlastAcceleratingParticle(BlockPos blastPosition, ServerWorld blastWorld, AbstractBlastEvent.Type blastType, Direction blastDirection, ExplosionCause explosionCause, float particleSpeed, Optional<Float> otherParticleSpeed, TileParticleAccelerator particleAccelerator) {
        super(blastPosition, blastWorld, blastType, blastDirection);
        this.explosionCause = explosionCause;
        this.particleSpeed = particleSpeed;
        this.otherParticleSpeed = otherParticleSpeed;
        this.particleAccelerator = particleAccelerator;
    }

    public static boolean fire(BlockPos blastPosition, ServerWorld blastWorld, ExplosionCause explosionCause, float particleSpeed, Optional<Float> otherParticleSpeed, TileParticleAccelerator particleAccelerator) {
        AbstractBlastEvent blastEvent = new EventBlastAcceleratingParticle(blastPosition, blastWorld, Type.EXPLOSIVES, Direction.DOWN, explosionCause, particleSpeed, otherParticleSpeed, particleAccelerator);
        boolean eventCancelled = MinecraftForge.EVENT_BUS.post(blastEvent);
        return !eventCancelled && blastEvent.executeBlast();
    }

    public ExplosionCause getExplosionCause() {
        return explosionCause;
    }

    public float getParticleSpeed() {
        return particleSpeed;
    }

    public Optional<Float> getOtherParticleSpeed() {
        return otherParticleSpeed;
    }

    public TileParticleAccelerator getParticleAccelerator() {
        return particleAccelerator;
    }

    @Override
    public boolean executeBlast() {
        ICBMReference.logger().info(String.format("Detonating particle @ %s with cause %s", getBlastPosition(), getExplosionCause()));
        Optional<ParticleAcceleratorRecipe> recipeOptional = ParticleAcceleratorRecipe.getRecipeFor(ICBMRecipeTypes.PARTICLE_ACCELERATOR, getExplosionCause(), getBlastWorld());
        recipeOptional.ifPresent((recipe)->{
            float maxParticleSpeed = Math.max(getParticleSpeed(), getOtherParticleSpeed().orElse(Float.MIN_VALUE));
            if(maxParticleSpeed >= ICBMReference.COMMON_CONFIG.getParticleAcceleratorSpeedRequiredToGenerateAntimatter() * recipe.getSpeedPercentRequired() && getBlastWorld().getRandom().nextDouble() <= recipe.getDropChance()) {
                getParticleAccelerator().tryProduceResult(recipe.getResultItem());
            }
        });
        ICBMBlastEventUtil.doVanillaExplosion(getBlastWorld(), getBlastPosition(), 1F);
        return true;
    }

}
