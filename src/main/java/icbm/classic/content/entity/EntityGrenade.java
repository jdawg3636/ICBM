package icbm.classic.content.entity;

import icbm.classic.api.ICBMClassicAPI;
import icbm.classic.lib.NBTConstants;
import icbm.classic.lib.capability.ex.CapabilityExplosiveEntity;
import icbm.classic.lib.explosive.ExplosiveHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class EntityGrenade extends Entity implements IEntityAdditionalSpawnData
{
    /** Entity that created the grenade and set it into motion */
    private LivingEntity thrower;

    /** Explosive capability */
    public final CapabilityExplosiveEntity explosive = new CapabilityExplosiveEntity(this);

    // TODO redo entity registration, reference net.minecraft.entity.EntityType
    public EntityGrenade(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    /**
     * Sets the explosive stack
     *
     * @param stack - explosive stack
     * @return this
     */
    public EntityGrenade setItemStack(ItemStack stack) {
        explosive.setStack(stack);
        return this;
    }

    /**
     * Sets the throwing entity
     *
     * @param thrower - entity that threw the grenade
     * @return this
     */
    public EntityGrenade setThrower(LivingEntity thrower) {
        this.thrower = thrower;
        return this;
    }

    public LivingEntity getThrower() {
        return thrower;
    }

    /**
     * Sets the aim and position based on the throwing entity
     *
     * @return this
     */
    public EntityGrenade aimFromThrower() {
        this.setLocationAndAngles(thrower.getPosX(), thrower.getPosY() + thrower.getEyeHeight(), thrower.getPosZ(), thrower.rotationYaw, thrower.rotationPitch);

        //Set position
        final float horizontalOffset = 0.16F;
        this.setPosition(
            getPosX() - MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * horizontalOffset,
            getPosY() - 0.10000000149011612D,
            getPosZ() - MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * horizontalOffset
        );

        return this;
    }

    /**
     * Spawns the grenade into the game world
     *
     * @return this
     */
    public EntityGrenade spawn() {
        world.addEntity(this);
        return this;
    }

    /**
     * Sets the motion of the grenade
     *
     * @param energy - energy to scale the motion
     * @return this
     */
    public EntityGrenade setThrowMotion(float energy) {
        //Set velocity
        final float powerScale = 0.4F;
        this.setMotion(
                -MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI) * powerScale,
                -MathHelper.sin((this.rotationPitch) / 180.0F * (float) Math.PI) * powerScale,
                +MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI) * powerScale
        );
        this.setThrowableHeading(this.getMotion().getX(), this.getMotion().getY(), this.getMotion().getZ(), 1.8f * energy, 1.0F); //TODO see what this 1.8 is and change to be 1 * energy
        return this;
    }

    @Override
    public ITextComponent getName() {
        return new StringTextComponent( "icbm.grenade." + explosive.getExplosiveData().getRegistryName());
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeCompoundTag(explosive.serializeNBT());
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        explosive.deserializeNBT(Optional.ofNullable(additionalData.readCompoundTag()).orElseGet(CompoundNBT::new));
    }

    /**
     * Sets the velocity of the grenade
     *
     * @param vx     - x component velocity vector
     * @param vy     - y component velocity vector
     * @param vz     - z component velocity vector
     * @param scale  - amount to scale the vector by
     * @param random - amount to randomize the vector
     */
    public void setThrowableHeading(double vx, double vy, double vz, float scale, float random) {
        //normalize
        float power = MathHelper.sqrt(vx * vx + vy * vy + vz * vz);
        vx /= power;
        vy /= power;
        vz /= power;

        //Randomize
        vx += this.rand.nextGaussian() * 0.007499999832361937D * random;
        vy += this.rand.nextGaussian() * 0.007499999832361937D * random;
        vz += this.rand.nextGaussian() * 0.007499999832361937D * random;

        //Scale
        vx *= scale;
        vy *= scale;
        vz *= scale;

        //Apply
        setVelocity(vx, vy, vz);
    }

    /** Sets the velocity to the args. Args: x, y, z */
    @Override
    public void setVelocity(double vx, double vy, double vz) {
        this.setMotion(vx, vy, vz);

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float var7 = MathHelper.sqrt(vx * vx + vz * vz);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(vx, vz) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(vy, var7) * 180.0D / Math.PI);
        }
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for
     * spiders and wolves to prevent them from trampling crops
     */
    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    protected void entityInit()
    {
    }

    /** Called to update the entity's position/logic. */
    @Override
    public void onUpdate()
    {
        this.lastTickPosX = this.getPosX();
        this.lastTickPosY = this.getPosY();
        this.lastTickPosZ = this.getPosZ();
        super.onUpdate();

        this.move(MoverType.SELF, this.getMotion());

        final float horizontalMag = MathHelper.sqrt(this.getMotion().getX() * this.getMotion().getX() + this.getMotion().getZ() * this.getMotion().getZ());
        this.rotationYaw = (float) (Math.atan2(this.getMotion().getX(), this.getMotion().getZ()) * 180.0D / Math.PI);

        for (this.rotationPitch = (float) (Math.atan2(this.getMotion().getY(), horizontalMag) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
        {
            ;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
        {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F)
        {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
        {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
        float var17 = 0.98F;
        float gravity = 0.03F;

        if (this.isInWater()) {
            for (int var7 = 0; var7 < 4; ++var7) {
                float var19 = 0.25F;
                this.world.addParticle(ParticleTypes.BUBBLE, this.getPosX() - this.getMotion().getX() * var19, this.getPosY() - this.getMotion().getY() * var19, this.getPosZ() - this.getMotion().getZ() * var19, this.getMotion().getX(), this.getMotion().getY(), this.getMotion().getZ());
            }

            var17 = 0.8F;
        }

        this.setMotion(this.getMotion().getX() * var17, this.getMotion().getY() * var17, this.getMotion().getZ() * var17);

        if (this.onGround) {
            this.setMotion(this.getMotion().getX() * 0.5, this.getMotion().getY() * 0.5, this.getMotion().getZ() * 0.5);
        }
        else {
            this.setMotion(this.getMotion().getX(), this.getMotion().getY() - gravity, this.getMotion().getZ());
            //this.pushOutOfBlocks(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D, this.posZ);
        }

        tickFuse();
    }

    /** Ticks the fuse */
    protected void tickFuse()
    {
        if (this.ticksExisted > ICBMClassicAPI.EX_GRENADE_REGISTRY.getFuseTime(this, explosive.getExplosiveData().getRegistryID()))
        {
            triggerExplosion();
        }
        else
        {
            ICBMClassicAPI.EX_GRENADE_REGISTRY.tickFuse(this, explosive.getExplosiveData().getRegistryID(), ticksExisted);
        }
    }

    /** Triggers the explosion of the grenade */
    protected void triggerExplosion()
    {
        this.world.addParticle(ParticleTypes.EXPLOSION, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
        ExplosiveHandler.createExplosion(this, this.world, this.getPosX(), this.getPosY() + 0.3f, this.getPosZ(), explosive.getExplosiveData().getRegistryID(), 1, explosive.getCustomBlastData());
        this.remove();
    }

    @Override
    public boolean handleWaterMovement()
    {
        return this.world.handleMaterialAcceleration(this.getBoundingBox(), Material.WATER, this);
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return true;
    }

    @Override
    public boolean canBePushed()
    {
        return true;
    }

    @Override
    protected void readEntityFromNBT(CompoundNBT nbt) {
        if (nbt.contains(NBTConstants.EXPLOSIVE)) {
            explosive.deserializeNBT(nbt.getCompound(NBTConstants.EXPLOSIVE));
        }
    }

    @Override
    protected void writeEntityToNBT(CompoundNBT nbt) {
        nbt.put(NBTConstants.EXPLOSIVE, explosive.serializeNBT());
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        if (capability == ICBMClassicAPI.EXPLOSIVE_CAPABILITY) {
            // Official documentation is not yet updated, porting file-by-file for the time being so hoping this works
            // https://forums.minecraftforge.net/topic/69813-lazyoptional/
            return LazyOptional.of(() -> ICBMClassicAPI.EXPLOSIVE_CAPABILITY).cast();
        }
        return null;
    }

}