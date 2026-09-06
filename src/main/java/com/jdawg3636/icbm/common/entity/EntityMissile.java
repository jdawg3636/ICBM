package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.multiblock.IMissileLaunchApparatus;
import com.jdawg3636.icbm.common.capability.ICBMCapabilities;
import com.jdawg3636.icbm.common.capability.missiledirector.IMissileDirectorCapability;
import com.jdawg3636.icbm.common.capability.missiledirector.LogicalMissile;
import com.jdawg3636.icbm.common.capability.missiledirector.MissileLaunchPhase;
import com.jdawg3636.icbm.common.capability.missiledirector.MissileSourceType;
import com.jdawg3636.icbm.common.event.BlastEventRegistryEntry;
import com.jdawg3636.icbm.common.reg.ItemReg;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ReuseableStream;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

public class EntityMissile extends Entity {

    public static final DataParameter<ItemStack> MISSILE_ITEM         = EntityDataManager.defineId(EntityMissile.class, DataSerializers.ITEM_STACK);
    public static final DataParameter<Integer>   MISSILE_SOURCE_TYPE  = EntityDataManager.defineId(EntityMissile.class, DataSerializers.INT);
    public static final DataParameter<Integer>   MISSILE_LAUNCH_PHASE = EntityDataManager.defineId(EntityMissile.class, DataSerializers.INT);
    // Vanilla movement packets encode angles as bytes (1.40625 degrees per step).
    private static final DataParameter<Float> MISSILE_YAW = EntityDataManager.defineId(EntityMissile.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> MISSILE_PITCH = EntityDataManager.defineId(EntityMissile.class, DataSerializers.FLOAT);

    private UUID simulatedMissileUUID = null;
    public Optional<ChunkPos> forcedChunkPos = Optional.empty();

    private int lerpSteps;
    private double lerpY;
    private double lerpX;
    private double lerpZ;

    @FunctionalInterface
    public interface Constructor<T extends EntityMissile> {
        T construct(EntityType<?> entityTypeIn, World worldIn, RegistryObject<BlastEventRegistryEntry> blastEventProvider, RegistryObject<Item> missileItem);
    }

    @FunctionalInterface
    public interface LogicalMissileProvider {
        Optional<LogicalMissile> apply(UUID puppetEntityUUID, EntityMissile cachedPuppetEntity, Optional<UUID> logicalMissileUUID);
    }

    public EntityMissile(EntityType<?> entityTypeIn, World worldIn, RegistryObject<BlastEventRegistryEntry> blastEventProvider, RegistryObject<Item> missileItem) {
        this(entityTypeIn, worldIn, (UUID puppetEntityUUID, EntityMissile cachedPuppetEntity, Optional<UUID> logicalMissileUUID) -> Optional.of(worldIn).filter(ServerWorld.class::isInstance).map(ServerWorld.class::cast).map(serverWorld -> new LogicalMissile(
            blastEventProvider,
            missileItem,
            MissileSourceType.LAUNCHER_PLATFORM,
            MissileLaunchPhase.STATIONARY,
            BlockPos.ZERO,
            BlockPos.ZERO.offset(100, 0, 0),
            (double)worldIn.getMaxBuildHeight(),
            3000,
            Optional.of(puppetEntityUUID),
            Optional.of(cachedPuppetEntity),
            serverWorld,
            logicalMissileUUID
        )), Optional.empty());
    }

    public EntityMissile(EntityType<?> entityTypeIn, World worldIn, LogicalMissileProvider logicalMissileGetter, Optional<UUID> logicalUUID) {
        super(entityTypeIn, worldIn);
        yRot = -90F;
        // Get logical missile and set puppet UUID to point to this entity.
        // If this getter is a constructor, then it will have the side effect of registering the logical missile with the missile director capability.
        Optional<LogicalMissile> logicalMissile = logicalMissileGetter.apply(this.uuid, this, logicalUUID);
        this.simulatedMissileUUID = logicalMissile.map(lm -> lm.getLogicalUUID().orElseThrow(RuntimeException::new)).orElse(null);
        this.updatePuppetToMatchLogical();
    }

    public LazyOptional<IMissileDirectorCapability> getMissileDirector() {
        return level.getCapability(ICBMCapabilities.MISSILE_DIRECTOR_CAPABILITY);
    }

    public Optional<LogicalMissile> getLogicalMissile() {
        return this.getMissileDirector().resolve().flatMap(md -> md.lookupLogicalMissile(this.simulatedMissileUUID));
    }

    public boolean launchMissile() {
        // Set launch phase (this will cause the tick function to simulate flight)
        this.getLogicalMissile().ifPresent(logicalMissile -> logicalMissile.setMissileLaunchPhase(MissileLaunchPhase.LAUNCHED.ordinal()));
        return true;
    }

    public void addEntityToLevel(Vector3d initialPosition, Vector3d initialRotation) {
        this.setPos(initialPosition.x, initialPosition.y, initialPosition.z);
        this.setRot((float)initialRotation.y, (float)initialRotation.x);
        this.addEntityToLevel();
    }

    public void addEntityToLevel() {
        level.addFreshEntity(this);
    }

    public MissileSourceType getMissileSourceType() {
        return MissileSourceType.values()[this.getEntityData().get(MISSILE_SOURCE_TYPE)];
    }

    public MissileLaunchPhase getMissileLaunchPhase() {
        return MissileLaunchPhase.values()[this.getEntityData().get(MISSILE_LAUNCH_PHASE)];
    }

    public Function<Integer, Vector3d> getPathFunction() {
        return this.getLogicalMissile().map(lm -> lm.pathFunction).orElse(i -> new Vector3d(0,0,0));
    }

    public Function<Vector3d, Vector3d> getGradientFunction() {
        level.setBlock(new BlockPos(0,0,0), Blocks.AIR.defaultBlockState(), 0);
        return this.getLogicalMissile().map(lm -> lm.gradientFunction).orElse(pos -> new Vector3d(0,0,0));
    }

    @Override
    public void tick() {
        super.tick();
        if(level.isClientSide() && this.getMissileLaunchPhase() == MissileLaunchPhase.LAUNCHED) {
            tickLerp();
            // Read precise rotation every tick, independently of position-only packets.
            // Keep yaw continuous across +/-180 so frame interpolation takes the short path.
            this.yRot += MathHelper.wrapDegrees(entityData.get(MISSILE_YAW) - this.yRot);
            this.xRot = entityData.get(MISSILE_PITCH);
            Vector3d viewVector = getViewVector(0F);
            spawnParticles(-viewVector.x, -viewVector.y, -viewVector.z);
        }
        if(level instanceof ServerWorld) {
            // Get current chunk pos
            ChunkPos currentChunkPos = new ChunkPos(this.blockPosition());
            // If forced chunk is missing OR not equal to current chunk, un-force it (if present) and switch to forcing current chunk
            if(forcedChunkPos.map(fc -> !fc.equals(currentChunkPos)).orElse(true)) {
                forcedChunkPos.ifPresent(fc -> ForgeChunkManager.forceChunk((ServerWorld) this.level, ICBMReference.MODID, this, fc.x, fc.z, false, true));
                forcedChunkPos = Optional.of(currentChunkPos);
                forcedChunkPos.ifPresent(fc -> ForgeChunkManager.forceChunk((ServerWorld) this.level, ICBMReference.MODID, this, fc.x, fc.z, true, true));
            }
        }
    }

    // Copied from vanilla BoatEntity, MC 1.16.5
    private void tickLerp() {
        if (this.lerpSteps > 0) {
            double d0 = this.getX() + (this.lerpX - this.getX()) / (double)this.lerpSteps;
            double d1 = this.getY() + (this.lerpY - this.getY()) / (double)this.lerpSteps;
            double d2 = this.getZ() + (this.lerpZ - this.getZ()) / (double)this.lerpSteps;
            --this.lerpSteps;
            this.setPos(d0, d1, d2);
        }
    }

    // Can't override (private), so using Mixin to do it.
    public Vector3d collideOverride(Vector3d destination) {
        Vector3d result = collideFiltered(destination);
        if(!MathHelper.equal(destination.x, result.x) || !MathHelper.equal(destination.z, result.z) || !MathHelper.equal(destination.y, result.y)) {
            this.getLogicalMissile().ifPresent(lm -> lm.shouldExplode = true);
        }
        return result;
    }

    /**
     * Modified version of vanilla collide()
     * Swap calls to collideBoundingBoxHeuristically() with calls to collideBoundingBoxFiltered()
     */
    public Vector3d collideFiltered(Vector3d pVec) {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        ISelectionContext iselectioncontext = ISelectionContext.of(this);
        VoxelShape voxelshape = this.level.getWorldBorder().getCollisionShape();
        Stream<VoxelShape> stream = VoxelShapes.joinIsNotEmpty(voxelshape, VoxelShapes.create(axisalignedbb.deflate(1.0E-7D)), IBooleanFunction.AND) ? Stream.empty() : Stream.of(voxelshape);
        Stream<VoxelShape> stream1 = this.level.getEntityCollisions(this, axisalignedbb.expandTowards(pVec), (Entity entity) -> true);
        ReuseableStream<VoxelShape> reuseablestream = new ReuseableStream<>(Stream.concat(stream1, stream));
        Vector3d vector3d = pVec.lengthSqr() == 0.0D ? pVec : collideBoundingBoxFiltered(this, pVec, axisalignedbb, this.level, iselectioncontext, reuseablestream);
        boolean flag = pVec.x != vector3d.x;
        boolean flag1 = pVec.y != vector3d.y;
        boolean flag2 = pVec.z != vector3d.z;
        boolean flag3 = this.onGround || flag1 && pVec.y < 0.0D;
        if (this.maxUpStep > 0.0F && flag3 && (flag || flag2)) {
            Vector3d vector3d1 = collideBoundingBoxFiltered(this, new Vector3d(pVec.x, (double)this.maxUpStep, pVec.z), axisalignedbb, this.level, iselectioncontext, reuseablestream);
            Vector3d vector3d2 = collideBoundingBoxFiltered(this, new Vector3d(0.0D, (double)this.maxUpStep, 0.0D), axisalignedbb.expandTowards(pVec.x, 0.0D, pVec.z), this.level, iselectioncontext, reuseablestream);
            if (vector3d2.y < (double)this.maxUpStep) {
                Vector3d vector3d3 = collideBoundingBoxFiltered(this, new Vector3d(pVec.x, 0.0D, pVec.z), axisalignedbb.move(vector3d2), this.level, iselectioncontext, reuseablestream).add(vector3d2);
                if (getHorizontalDistanceSqr(vector3d3) > getHorizontalDistanceSqr(vector3d1)) {
                    vector3d1 = vector3d3;
                }
            }

            if (getHorizontalDistanceSqr(vector3d1) > getHorizontalDistanceSqr(vector3d)) {
                return vector3d1.add(collideBoundingBoxHeuristically(this, new Vector3d(0.0D, -vector3d1.y + pVec.y, 0.0D), axisalignedbb.move(vector3d1), this.level, iselectioncontext, reuseablestream));
            }
        }

        return vector3d;
    }

    /**
     * Modified version of vanilla collideBoundingBoxHeuristically()
     * Filters Block Collisions to omit blocks which implement IMissileLaunchApparatus
     */
    public static Vector3d collideBoundingBoxFiltered(@Nullable Entity pEntity, Vector3d pVec, AxisAlignedBB pCollisionBox, World pLevel, ISelectionContext pContext, ReuseableStream<VoxelShape> pPotentialHits) {
        boolean flag = pVec.x == 0.0D;
        boolean flag1 = pVec.y == 0.0D;
        boolean flag2 = pVec.z == 0.0D;
        if ((!flag || !flag1) && (!flag || !flag2) && (!flag1 || !flag2)) {
            ReuseableStream<VoxelShape> reuseablestream = new ReuseableStream<>(Stream.concat(pPotentialHits.getStream(), pLevel.getBlockCollisions(pEntity, pCollisionBox.expandTowards(pVec), (BlockState blockState, BlockPos blockPos) -> !(blockState.getBlock() instanceof IMissileLaunchApparatus))));
            return collideBoundingBoxLegacy(pVec, pCollisionBox, reuseablestream);
        } else {
            return collideBoundingBox(pVec, pCollisionBox, pLevel, pContext, pPotentialHits);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
        if(simulatedMissileUUID != null) {
            compound.putUUID("SimulatedMissileUUID", simulatedMissileUUID);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {
        if(compound.contains("SimulatedMissileUUID")) {
            UUID simulatedMissileUUID = compound.getUUID("SimulatedMissileUUID");
            if (!Objects.equals(this.simulatedMissileUUID, simulatedMissileUUID)) {
                // Constructor always makes a new LogicalMissile. If we deserialize and realize one already existed, then we need to drop the old one. Not a perfect fix, but GC should clean it up.
                this.getMissileDirector().ifPresent(missileDirector -> missileDirector.unregisterMissile(this.simulatedMissileUUID));
                this.simulatedMissileUUID = simulatedMissileUUID;
                this.updatePuppetToMatchLogical();
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(MISSILE_ITEM, ItemReg.MISSILE_MODULE.get().getDefaultInstance());
        entityData.define(MISSILE_SOURCE_TYPE, MissileSourceType.LAUNCHER_PLATFORM.ordinal());
        entityData.define(MISSILE_LAUNCH_PHASE, MissileLaunchPhase.STATIONARY.ordinal());
        entityData.define(MISSILE_YAW, 0F);
        entityData.define(MISSILE_PITCH, 0F);
    }

    public void updateMissileData(BlockPos sourcePos, BlockPos destPos, Float peakHeight, Integer totalFlightTicks, MissileSourceType missileSourceType) {
        this.getLogicalMissile().ifPresent(lm -> lm.updateMissileData(sourcePos, destPos, peakHeight, totalFlightTicks, missileSourceType, Optional.of(this)));
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return this.getEntityData().get(MISSILE_ITEM).copy();
    }

    public void spawnParticles(double motionX, double motionY, double motionZ) {
        if(this.level.isClientSide()) {
            Random random = new Random();
            for(int i = 0; i < 4; i++)     this.level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, false, this.getX(), this.getY(), this.getZ(), random.nextDouble()/1.5 - 0.3333 + motionX, random.nextDouble()/1.5 - 0.3333 + motionY, random.nextDouble()/1.5 - 0.3333 + motionZ);
            for(int i = 0; i < 4; i++)     this.level.addParticle(ParticleTypes.CLOUD,               false, this.getX(), this.getY(), this.getZ(), random.nextDouble()/1.5 - 0.3333 + motionX, random.nextDouble()/1.5 - 0.3333 + motionY, random.nextDouble()/1.5 - 0.3333 + motionZ);
            if(random.nextFloat() < 0.25f) this.level.addParticle(ParticleTypes.EXPLOSION,           true,  this.getX() + (3 * random.nextDouble() - 1.5), this.getY()- 1.5 * random.nextDouble(), this.getZ() + (3 * random.nextDouble() - 1.5), random.nextDouble()/1.5 - 0.3333 + motionX, random.nextDouble()/-1.5 - 0.3333 + motionY, random.nextDouble()/1.5 - 0.3333 + motionZ);
        }
    }

    @Override
    protected boolean isMovementNoisy() {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    protected boolean canRide(Entity entityIn) {
        return true;
    }

    @Override
    public double getPassengersRidingOffset() {
        return Math.sin(Math.toRadians(Math.max(0, -this.xRot))) * (getBbHeight() - 1);
    }

    @Override
    public final ActionResultType interact(PlayerEntity player, Hand hand) {
        if (!this.isVehicle() && !player.isSecondaryUseActive() && !this.getMissileSourceType().equals(MissileSourceType.ROCKET_LAUNCHER)) {
            if (!this.level.isClientSide) {
                player.startRiding(this);
            }
            return ActionResultType.sidedSuccess(this.level.isClientSide);
        }
        else {
            return ActionResultType.PASS;
        }
    }

    @Override
    public void setPosRaw(double x, double y, double z) {
        super.setPosRaw(x, y, z);
        this.getLogicalMissile().ifPresent(lm -> {
            lm.x = x;
            lm.y = y;
            lm.z = z;
        });
    }

    @Override
    public void setRot(float yRot, float xRot) {
        super.setRot(yRot, xRot);
        if (!level.isClientSide()) {
            entityData.set(MISSILE_YAW, this.yRot);
            entityData.set(MISSILE_PITCH, this.xRot);
        }
        this.getLogicalMissile().ifPresent(lm -> {
            lm.yRot = yRot;
            lm.xRot = xRot;
        });
    }

    // Position interpolation from vanilla BoatEntity, MC 1.16.5.
    // Rotation comes from full-precision entity data, never the packet's byte angles.
    @OnlyIn(Dist.CLIENT)
    @Override
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int posRotationIncrements, boolean teleport) {
        this.lerpX = x;
        this.lerpY = y;
        this.lerpZ = z;
        this.lerpSteps = 10;
    }

    public void updatePuppetToMatchLogical() {
        this.getLogicalMissile().ifPresent(lm -> {
            this.setPos(lm.x, lm.y, lm.z);
            this.setRot(lm.yRot, lm.xRot);
            this.getEntityData().set(MISSILE_ITEM, lm.missileItem.get().getDefaultInstance());
            this.getEntityData().set(MISSILE_SOURCE_TYPE, lm.missileSourceType.ordinal());
            this.getEntityData().set(MISSILE_LAUNCH_PHASE, lm.missileLaunchPhase.ordinal());
        });
    }

    public void unforceChunk() {
        if(this.level instanceof ServerWorld) forcedChunkPos.ifPresent(fc -> ForgeChunkManager.forceChunk((ServerWorld) this.level, ICBMReference.MODID, this, fc.x, fc.z, false, true));
    }

    @Override
    public void kill() {
        if(!this.removed && level != null && !level.isClientSide()) getMissileDirector().ifPresent(md -> md.deleteMissile(simulatedMissileUUID));
        unforceChunk(); // This call is likely redundant since the LogicalMissile will invoke killPuppet, which already un-forces the chunk, but better safe than sorry.
        super.kill();
    }

    public void killPuppet() {
        if(!this.removed && level != null && !level.isClientSide()) getLogicalMissile().ifPresent(lm -> {
            lm.puppetEntityUUID = Optional.empty();
            lm.cachedPuppetEntity = Optional.empty();
        });
        unforceChunk();
        super.kill();
    }

}
