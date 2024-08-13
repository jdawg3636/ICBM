package com.jdawg3636.icbm.common.entity;

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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

public class EntityMissile extends Entity {

    public static final DataParameter<Integer>  MISSILE_SOURCE_TYPE  = EntityDataManager.defineId(EntityMissile.class, DataSerializers.INT);
    public static final DataParameter<Integer>  MISSILE_LAUNCH_PHASE = EntityDataManager.defineId(EntityMissile.class, DataSerializers.INT);

    private UUID simulatedMissileUUID = null;

    public EntityMissile(EntityType<?> entityTypeIn, World worldIn, RegistryObject<BlastEventRegistryEntry> blastEventProvider, RegistryObject<Item> missileItem) {
        this(entityTypeIn, worldIn, uuid1 -> Optional.of(worldIn).filter(ServerWorld.class::isInstance).map(ServerWorld.class::cast).map(serverWorld -> new LogicalMissile(
            blastEventProvider,
            missileItem,
            MissileSourceType.LAUNCHER_PLATFORM,
            MissileLaunchPhase.STATIONARY,
            BlockPos.ZERO,
            BlockPos.ZERO.offset(100, 0, 0),
            worldIn.getMaxBuildHeight(),
            3000,
            Optional.of(uuid1),
            serverWorld
        )), Optional.empty());
    }

    public EntityMissile(EntityType<?> entityTypeIn, World worldIn, Function<UUID, Optional<LogicalMissile>> logicalMissileConstructor, Optional<UUID> logicalUUID) {
        super(entityTypeIn, worldIn);
        // Construct and register logical missile
        this.getMissileDirector().ifPresent(md -> logicalMissileConstructor.apply(this.uuid).ifPresent(lm -> this.simulatedMissileUUID = md.registerMissile(lm, logicalUUID)));
        yRot = -90F;
    }

    public LazyOptional<IMissileDirectorCapability> getMissileDirector() {
        return level.getCapability(ICBMCapabilities.MISSILE_DIRECTOR_CAPABILITY);
    }

    public Optional<LogicalMissile> getLogicalMissile() {
        return this.getMissileDirector().resolve().flatMap(md -> md.lookupLogicalMissile(this.simulatedMissileUUID));
    }

    public void launchMissile() {
        // Set launch phase (this will cause the tick function to simulate flight)
        entityData.set(MISSILE_LAUNCH_PHASE, MissileLaunchPhase.LAUNCHED.ordinal());
    }

    public void addEntityToLevel(Vector3d initialPosition, Vector3d initialRotation) {
        this.setPos(initialPosition.x, initialPosition.y, initialPosition.z);
        this.setRot((float)initialRotation.y, (float)initialRotation.x);
        this.addEntityToLevel();
    }

    public void addEntityToLevel() {
        // todo chunk loading
//        ForgeChunkManager.forceChunk()
        level.addFreshEntity(this);
    }

    public MissileSourceType getMissileSourceType() {
        return this.getLogicalMissile().map(lm -> lm.missileSourceType).orElse(MissileSourceType.LAUNCHER_PLATFORM);
    }

    public MissileLaunchPhase getMissileLaunchPhase() {
        return this.getLogicalMissile().map(lm -> lm.missileLaunchPhase).orElse(MissileLaunchPhase.STATIONARY);
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
            Vector3d viewVector = getViewVector(0F);
            spawnParticles(-viewVector.x, -viewVector.y, -viewVector.z);
        }
    }

    @Override
    public Vector3d collide(Vector3d destination) {
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
            simulatedMissileUUID = compound.getUUID("SimulatedMissileUUID");
        }
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(MISSILE_SOURCE_TYPE, MissileSourceType.LAUNCHER_PLATFORM.ordinal());
        entityData.define(MISSILE_LAUNCH_PHASE, MissileLaunchPhase.STATIONARY.ordinal());
    }

    @Override
    public void onSyncedDataUpdated(DataParameter<?> dataParameter) {
        getLogicalMissile().ifPresent(lm -> {
            if(MISSILE_SOURCE_TYPE.equals(dataParameter)) {
                lm.missileSourceType = MissileSourceType.values()[entityData.get(MISSILE_SOURCE_TYPE)];
            }
            if(MISSILE_LAUNCH_PHASE.equals(dataParameter)) {
                lm.missileLaunchPhase = MissileLaunchPhase.values()[entityData.get(MISSILE_LAUNCH_PHASE)];
            }
        });
    }

    public void updateMissileData(BlockPos sourcePos, BlockPos destPos, Float peakHeight, Integer totalFlightTicks, MissileSourceType missileSourceType) {
        this.getLogicalMissile().ifPresent(lm -> lm.updateMissileData(sourcePos, destPos, peakHeight, totalFlightTicks, missileSourceType));
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
        return this.getLogicalMissile().map(lm -> lm.missileItem).orElse(ItemReg.MISSILE_CONVENTIONAL).get().getDefaultInstance();
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

    public boolean isAttackable() {
        return false;
    }

    @Override
    protected boolean canRide(Entity entityIn) {
        return true;
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
        this.getLogicalMissile().ifPresent(lm -> {
            lm.yRot = yRot;
            lm.xRot = xRot;
        });
    }

    public void updatePuppetToMatchLogical() {
        this.getLogicalMissile().ifPresent(lm -> {
            this.setPos(lm.x, lm.y, lm.z);
            this.setRot(lm.yRot, lm.xRot);
            lm.setMissileSourceType(lm.missileSourceType.ordinal());
            lm.setMissileLaunchPhase(lm.missileLaunchPhase.ordinal());
        });
    }

    @Override
    public void kill() {
        if(!this.removed && level != null && !level.isClientSide()) getMissileDirector().ifPresent(md -> md.deleteMissile(simulatedMissileUUID));
        super.kill();
    }

    public void killPuppet() {
        if(!this.removed && level != null && !level.isClientSide()) getLogicalMissile().ifPresent(lm -> lm.puppetEntityUUID = Optional.empty());
        super.kill();
    }

}
