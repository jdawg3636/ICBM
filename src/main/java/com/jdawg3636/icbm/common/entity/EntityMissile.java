package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.common.block.multiblock.IMissileLaunchApparatus;
import com.jdawg3636.icbm.common.capability.ICBMCapabilities;
import com.jdawg3636.icbm.common.capability.missiledirector.IMissileDirectorCapability;
import com.jdawg3636.icbm.common.capability.missiledirector.LogicalMissile;
import com.jdawg3636.icbm.common.capability.missiledirector.MissileLaunchPhase;
import com.jdawg3636.icbm.common.capability.missiledirector.MissileSourceType;
import com.jdawg3636.icbm.common.event.AbstractBlastEvent;
import com.jdawg3636.icbm.common.reg.ItemReg;
import net.minecraft.block.BlockState;
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
import java.util.function.Function;
import java.util.stream.Stream;

public class EntityMissile extends Entity {

    public static final DataParameter<Integer>  MISSILE_SOURCE_TYPE  = EntityDataManager.defineId(EntityMissile.class, DataSerializers.INT);
    public static final DataParameter<Integer>  MISSILE_LAUNCH_PHASE = EntityDataManager.defineId(EntityMissile.class, DataSerializers.INT);

    private Optional<LogicalMissile> logicalMissile = Optional.empty();

    public EntityMissile(EntityType<?> entityTypeIn, World worldIn, AbstractBlastEvent.BlastEventProvider blastEventProvider, RegistryObject<Item> missileItem) {
        super(entityTypeIn, worldIn);
        this.logicalMissile = Optional.of(new LogicalMissile(
                blastEventProvider,
                missileItem,
                MissileSourceType.LAUNCHER_PLATFORM,
                MissileLaunchPhase.STATIONARY,
                BlockPos.ZERO,
                BlockPos.ZERO.offset(100, 0, 0),
                worldIn.getMaxBuildHeight(),
                3000,
                Optional.of(this)
        ));
        yRot = -90F;
    }

    public LazyOptional<IMissileDirectorCapability> getMissileDirector() {
        return level.getCapability(ICBMCapabilities.MISSILE_DIRECTOR_CAPABILITY);
    }

    public void launchMissile() {
        // Set launch phase (this will cause the tick function to simulate flight)
        // TODO: Swap to simulation
        entityData.set(MISSILE_LAUNCH_PHASE, MissileLaunchPhase.LAUNCHED.ordinal());
    }

    public void addEntityToLevel(Vector3d initialPosition, Vector3d initialRotation) {
        this.setPos(initialPosition.x, initialPosition.y, initialPosition.z);
        this.setRot((float)initialRotation.y, (float)initialRotation.x);
        level.addFreshEntity(this);
    }

    public MissileSourceType getMissileSourceType() {
        return this.logicalMissile.map(lm -> lm.missileSourceType).orElse(MissileSourceType.LAUNCHER_PLATFORM);
    }

    public MissileLaunchPhase getMissileLaunchPhase() {
        return this.logicalMissile.map(lm -> lm.missileLaunchPhase).orElse(MissileLaunchPhase.STATIONARY);
    }

    public Function<Integer, Vector3d> getPathFunction() {
        return this.logicalMissile.map(lm -> lm.pathFunction).orElse(i -> new Vector3d(0,0,0));
    }

    public Function<Vector3d, Vector3d> getGradientFunction() {
        return this.logicalMissile.map(lm -> lm.gradientFunction).orElse(pos -> new Vector3d(0,0,0));
    }

    @Override
    public boolean save(CompoundNBT pCompound) {
        // TODO: verify that this works. Intent is to prevent the missile from being saved to disk when it leaves loaded chunks.
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.level instanceof ServerWorld) {
            // TODO: Remove this once using simulation capability
            this.logicalMissile.ifPresent(lm -> lm.tick((ServerWorld) level));
        }
        else if(this.logicalMissile.map(lm -> lm.missileLaunchPhase).orElse(MissileLaunchPhase.STATIONARY) == MissileLaunchPhase.LAUNCHED) {
            Vector3d viewVector = getViewVector(0F);
            spawnParticles(-viewVector.x, -viewVector.y, -viewVector.z);
        }
    }

    @Override
    public Vector3d collide(Vector3d destination) {
        Vector3d result = collideFiltered(destination);
        if(!MathHelper.equal(destination.x, result.x) || !MathHelper.equal(destination.z, result.z) || !MathHelper.equal(destination.y, result.y)) {
            this.logicalMissile.ifPresent(lm -> lm.shouldExplode = true);
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
        logicalMissile.ifPresent(lm -> lm.save(compound));
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {
        logicalMissile.ifPresent(lm -> lm.load(compound));
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(MISSILE_SOURCE_TYPE, MissileSourceType.LAUNCHER_PLATFORM.ordinal());
        entityData.define(MISSILE_LAUNCH_PHASE, MissileLaunchPhase.STATIONARY.ordinal());
    }

    @Override
    public void onSyncedDataUpdated(DataParameter<?> dataParameter) {
        logicalMissile.ifPresent(lm -> {
            if(MISSILE_SOURCE_TYPE.equals(dataParameter)) {
                lm.missileSourceType = MissileSourceType.values()[entityData.get(MISSILE_SOURCE_TYPE)];
            }
            if(MISSILE_LAUNCH_PHASE.equals(dataParameter)) {
                lm.missileLaunchPhase = MissileLaunchPhase.values()[entityData.get(MISSILE_LAUNCH_PHASE)];
            }
        });
    }

    public void updateMissileData(BlockPos sourcePos, BlockPos destPos, Float peakHeight, Integer totalFlightTicks, MissileSourceType missileSourceType) {
        this.logicalMissile.ifPresent(lm -> lm.updateMissileData(sourcePos, destPos, peakHeight, totalFlightTicks, missileSourceType));
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
        return this.logicalMissile.map(lm -> lm.missileItem).orElse(ItemReg.MISSILE_CONVENTIONAL).get().getDefaultInstance();
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
        if (!this.isVehicle() && !player.isSecondaryUseActive() && !this.logicalMissile.map(lm -> lm.missileSourceType).orElse(MissileSourceType.LAUNCHER_PLATFORM).equals(MissileSourceType.ROCKET_LAUNCHER)) {
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
        if(logicalMissile != null) {
            this.logicalMissile.ifPresent(lm -> {
                lm.x = x;
                lm.y = y;
                lm.z = z;
            });
        }
    }

    @Override
    public void setRot(float yRot, float xRot) {
        super.setRot(yRot, xRot);
        this.logicalMissile.ifPresent(lm -> {
            lm.yRot = yRot;
            lm.xRot = xRot;
        });
    }

    @Override
    public void kill() {
        if(level != null && !level.isClientSide()) getMissileDirector().ifPresent(md -> md.deleteMissile(this));
        super.kill();
    }

}
