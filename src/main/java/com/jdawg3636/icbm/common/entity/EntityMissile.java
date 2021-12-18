package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.common.block.multiblock.IMissileLaunchApparatus;
import com.jdawg3636.icbm.common.event.AbstractBlastEvent;
import com.jdawg3636.icbm.common.reg.SoundEventReg;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
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
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

public class EntityMissile extends Entity {

    public enum MissileSourceType {

        LAUNCHER_PLATFORM(AbstractBlastEvent.Type.PLATFORM_MISSILE),
        CRUISE_LAUNCHER(AbstractBlastEvent.Type.CRUISE_MISSILE),
        ROCKET_LAUNCHER(AbstractBlastEvent.Type.HANDHELD_ROCKET);

        MissileSourceType(final AbstractBlastEvent.Type blastType) {
            this.blastType = blastType;
        }

        private AbstractBlastEvent.Type blastType;

        public AbstractBlastEvent.Type getResultantBlastType() {
            return blastType;
        }

    }

    public enum MissileLaunchPhase {
        STATIONARY,
        STATIONARY_ACTIVATED, // Generate Particles/Sounds while still on the platform
        LAUNCHED
    }

    public static final DataParameter<Integer>  MISSILE_SOURCE_TYPE  = EntityDataManager.defineId(EntityMissile.class, DataSerializers.INT);
    public static final DataParameter<Integer>  MISSILE_LAUNCH_PHASE = EntityDataManager.defineId(EntityMissile.class, DataSerializers.INT);
    public static final DataParameter<BlockPos> SOURCE_POS           = EntityDataManager.defineId(EntityMissile.class, DataSerializers.BLOCK_POS);
    public static final DataParameter<BlockPos> DEST_POS             = EntityDataManager.defineId(EntityMissile.class, DataSerializers.BLOCK_POS);
    public static final DataParameter<Float>    PEAK_HEIGHT          = EntityDataManager.defineId(EntityMissile.class, DataSerializers.FLOAT);
    public static final DataParameter<Integer>  TOTAL_FLIGHT_TICKS   = EntityDataManager.defineId(EntityMissile.class, DataSerializers.INT);

    public AbstractBlastEvent.BlastEventProvider blastEventProvider;
    public RegistryObject<Item> missileItem;
    public MissileSourceType missileSourceType = MissileSourceType.LAUNCHER_PLATFORM;
    public MissileLaunchPhase missileLaunchPhase = MissileLaunchPhase.STATIONARY;
    public BlockPos sourcePos = BlockPos.ZERO;
    public BlockPos destPos = BlockPos.ZERO.offset(100, 0, 0);
    public double peakHeight = 256;
    public int totalFlightTicks = 300;
    public Function<Integer, Vector3d> pathFunction = null;
    public Function<Vector3d, Vector3d> gradientFunction = null;

    public int ticksSinceLaunch = 0;
    public boolean shouldExplode = false;

    public EntityMissile(EntityType<?> entityTypeIn, World worldIn, AbstractBlastEvent.BlastEventProvider blastEventProvider, RegistryObject<Item> missileItem) {
        this(
                entityTypeIn,
                worldIn,
                blastEventProvider,
                missileItem,
                MissileSourceType.LAUNCHER_PLATFORM,
                MissileLaunchPhase.STATIONARY,
                BlockPos.ZERO,
                BlockPos.ZERO.offset(100, 0, 0),
                worldIn.getMaxBuildHeight(),
                3000
        );
        yRot = -90F;
    }

    public EntityMissile(EntityType<?> entityTypeIn, World worldIn, AbstractBlastEvent.BlastEventProvider blastEventProvider, RegistryObject<Item> missileItem, MissileSourceType missileSourceType, MissileLaunchPhase missileLaunchPhase, BlockPos sourcePos, BlockPos destPos, double peakHeight, int totalFlightTicks) {
        super(entityTypeIn, worldIn);
        this.blastEventProvider = blastEventProvider;
        this.missileItem = missileItem;
        this.missileSourceType = missileSourceType;
        this.missileLaunchPhase = missileLaunchPhase;
        this.sourcePos = sourcePos;
        this.destPos = destPos;
        this.peakHeight = peakHeight;
        this.totalFlightTicks = totalFlightTicks;
    }

    public void updatePathFunctions() {
        Tuple<Function<Integer, Vector3d>, Function<Vector3d, Vector3d>> pathFunctions = calculatePathFunctions();
        this.pathFunction = pathFunctions.getA();
        this.gradientFunction = pathFunctions.getB();
    }

    /**
     * @return Returns two functions, one for Ticks -> Position and another for Position -> Euler Rotations.
     */
    public Tuple<Function<Integer, Vector3d>, Function<Vector3d, Vector3d>> calculatePathFunctions() {
        if (missileSourceType == MissileSourceType.LAUNCHER_PLATFORM) {
            return calculatePathFunctionsParabola();
        }
        else {
            return calculatePathFunctionsLine();
        }
    }

    public Tuple<Function<Integer, Vector3d>, Function<Vector3d, Vector3d>> calculatePathFunctionsLine() {

        // Precalculating the 'm' in y = m(x - x1) + y1
        final double deltaY = destPos.getY() - sourcePos.getY();
        final double deltaX = destPos.getX() - sourcePos.getX();
        final double deltaZ = destPos.getZ() - sourcePos.getZ();
        final double horizDistance = Math.sqrt(Math.abs(deltaX * deltaX + deltaZ * deltaZ));
        final double slope = Math.abs(deltaY/horizDistance);

        // Precalculating Rotation About the Y axis
        double rotY = Math.toDegrees(Math.atan(deltaZ != 0 ? Math.abs(deltaX) / Math.abs(deltaZ) : 90));
        if(deltaX > 0) rotY -= 2 * rotY;
        if(deltaZ < 0) rotY += 2 * (90 - rotY);
        final double finalRotY = rotY;

        // Precalculating Rotation About the X axis
        double rotX = Math.toDegrees(Math.atan(slope));
        if(deltaY > 0) rotX *= -1;
        final double finalRotX = rotX;

        return new Tuple<>(

                // Takes Ticks, Returns Position
                ticks -> new Vector3d(deltaX,deltaY,deltaZ).scale(totalFlightTicks != 0 ? ((double)ticks)/totalFlightTicks : 0).add(Vector3d.atCenterOf(sourcePos)),

                // Takes Position, Returns Euler Rotations
                position -> new Vector3d(finalRotX, finalRotY, 0D)

        );

    }

    public Tuple<Function<Integer, Vector3d>, Function<Vector3d, Vector3d>> calculatePathFunctionsParabola() {

        double[] parabolaXCoefficients = generateParabola(sourcePos.getX(), sourcePos.getY(), destPos.getX(), destPos.getY(), peakHeight);
        double[] parabolaZCoefficients = generateParabola(sourcePos.getZ(), sourcePos.getY(), destPos.getZ(), destPos.getY(), peakHeight);

        // Precalculating Rotation About the Y axis, doesn't change during flight.
        double rotY = Math.toDegrees(Math.atan(Math.abs((double)(destPos.getX() - sourcePos.getX())) / Math.abs(destPos.getZ() - sourcePos.getZ())));
        if(destPos.getX() > sourcePos.getX()) rotY -= 2 * rotY;
        if(destPos.getZ() < sourcePos.getZ()) rotY += 2 * (90 - rotY);
        final double finalRotY = rotY;

        // Precalculating a Multiple for Rotation About the X axis, direction of travel is not known at runtime so may need to multiply by -1
        // Need to calculate for both X and Z as the choice of which to use for calculating Y is made at runtime
        final int rotXMultipleFromX = destPos.getX() >= sourcePos.getX() ? -1 : 1;
        final int rotXMultipleFromZ = destPos.getZ() >= sourcePos.getZ() ? -1 : 1;

        //System.out.printf("[ICBM DEBUG] Updated Path Function for X: %fx^2 + %fx + %f\n", parabolaXCoefficients[0], parabolaXCoefficients[1], parabolaXCoefficients[2]);
        //System.out.printf("[ICBM DEBUG] Updated Path Function for Z: %fz^2 + %fz + %f\n", parabolaZCoefficients[0], parabolaZCoefficients[1], parabolaZCoefficients[2]);

        return new Tuple<>(

                // Takes Ticks, Returns Position
                new Function<Integer, Vector3d>() {

                    final BlockPos funcSourcePos = sourcePos;
                    final BlockPos funcDestPos = destPos;
                    final double[] funcParabolaXCoefficients = parabolaXCoefficients;
                    final double[] funcParabolaZCoefficients = parabolaZCoefficients;
                    final int funcTotalFlightTicks = totalFlightTicks != 0 ? totalFlightTicks : 1;

                    @Override
                    public Vector3d apply(Integer ticks) {

                        double x = funcSourcePos.getX() + ((double) (funcDestPos.getX() - funcSourcePos.getX()) * ticks / funcTotalFlightTicks);
                        double z = funcSourcePos.getZ() + ((double) (funcDestPos.getZ() - funcSourcePos.getZ()) * ticks / funcTotalFlightTicks);

                        double yFromX = funcParabolaXCoefficients[0] * x * x + funcParabolaXCoefficients[1] * x + funcParabolaXCoefficients[2];
                        double yFromZ = funcParabolaZCoefficients[0] * z * z + funcParabolaZCoefficients[1] * z + funcParabolaZCoefficients[2];
                        double y = !Double.isNaN(funcParabolaXCoefficients[0]) ? yFromX : yFromZ;

                        return new Vector3d(x, y, z);

                    }

                },

                // Takes Position, Returns Euler Rotations
                new Function<Vector3d, Vector3d>() {

                    final double[] funcParabolaXCoefficients = parabolaXCoefficients;
                    final double[] funcParabolaZCoefficients = parabolaZCoefficients;
                    final double funcRotY = finalRotY;

                    @Override
                    public Vector3d apply(Vector3d position) {

                        double rotXFromX = rotXMultipleFromX * Math.toDegrees(Math.atan(2d * funcParabolaXCoefficients[0] * position.x + funcParabolaXCoefficients[1]));
                        double rotXFromZ = rotXMultipleFromZ * Math.toDegrees(Math.atan(2d * funcParabolaZCoefficients[0] * position.z + funcParabolaZCoefficients[1]));

                        Vector3d toReturn = new Vector3d((!Double.isNaN(funcParabolaXCoefficients[0]) ? rotXFromX : rotXFromZ), funcRotY, 0D);
                        //System.out.printf("[ICBM DEBUG] Position = (%s, %s)\n", position.x, position.z);
                        //System.out.printf("[ICBM DEBUG] Applying Rotation (%s, %s)\n", toReturn.x(), toReturn.y());
                        return toReturn;

                    }

                }

        );
    }

    /**
     * @return Vector3d containing (in order) the a, b, and c coefficients for the standard form of the calculated parabola (y = ax^2 + bx + c)
     */
    public static double[] generateParabola(double sourceHoriz, double sourceHeight, double targetHoriz, double targetHeight, double peakHeightIn) {

        // Using Gaussian Elimination
        // ax^2 + bx + c = y
        // Somewhat inverted as x^2, x, and 1 are the known coefficients while a, b, c are being solved for.

        // Includes logic to ensure that that the First Equation (x1Squared * a + x1 * b + x1Const * c = y1) has the
        // highest x value (and therefore highest x^2 value) as otherwise a value of 0 could cause serious problems

        // Coefficients for 'c'
        double x1Const = 1;
        double x2Const = 1;
        double x3Const = 1;

        // Coefficients for 'b'
        double x1 = (sourceHoriz > targetHoriz) ? sourceHoriz : targetHoriz;
        double x2 = (sourceHoriz + targetHoriz) / 2d;
        double x3 = (sourceHoriz < targetHoriz) ? sourceHoriz : targetHoriz;

        // Coefficients for 'a'
        double x1Squared = x1 * x1;
        double x2Squared = x2 * x2;
        double x3Squared = x3 * x3;

        // Right Side of Equations
        double y1 = (sourceHoriz > targetHoriz) ? sourceHeight : targetHeight;
        double y2 = peakHeightIn;
        double y3 = (sourceHoriz < targetHoriz) ? sourceHeight : targetHeight;

        // Reused Temp Var for Scaling Process
        double multiple;

        // Scale Equation 2 using Equation 1
        multiple    = -x2Squared/x1Squared;
        x2Squared   += multiple * x1Squared;
        x2          += multiple * x1;
        x2Const     += multiple * x1Const;
        y2          += multiple * y1;

        // Scale Equation 3 using Equation 1
        multiple    = -x3Squared/x1Squared;
        x3Squared   += multiple * x1Squared;
        x3          += multiple * x1;
        x3Const     += multiple * x1Const;
        y3          += multiple * y1;

        // Scale Equation 3 using Equation 2
        multiple    = -x3/x2;
        x3Squared   += multiple * x2Squared;
        x3          += multiple * x2;
        x3Const     += multiple * x2Const;
        y3          += multiple * y2;

        // Final Solve
        double c =  y3 / x3Const;
        double b = (y2 - x2Const * c) / x2;
        double a = (y1 - x1Const * c - x1 * b) / x1Squared;

        // Package and Return
        return new double[]{a,b,c};

    }

    @Override
    public void tick() {
        super.tick();
        switch(missileLaunchPhase) {

            case STATIONARY:
                break;

            case STATIONARY_ACTIVATED:
                if(!level.isClientSide()) spawnParticles(0,0,0);
                break;

            case LAUNCHED:

                ticksSinceLaunch++;

                if(!level.isClientSide()) {

                    if(shouldExplode) explode();

                    // TODO make timeout user-configurable
                    if(missileSourceType != MissileSourceType.LAUNCHER_PLATFORM && ticksSinceLaunch > 100) {
                        explode();
                        break;
                    }

                    if(getY() < 0) { //todo: switch this to a variable for MC 1.17+
                        kill(); // Don't Explode, Just Disappear.
                        break;
                    }

                    if ((ticksSinceLaunch-1) % 40 == 0) {
                        this.level.playSound((PlayerEntity) null, getX(), getY(), getZ(), SoundEventReg.EFFECT_MISSILE_FLIGHT.get(), SoundCategory.BLOCKS, 2.5F, 1.0F);
                    }

                    // TODO: if y > 255, then despawn missile and switch to simulation

                    Vector3d newPos = pathFunction.apply(ticksSinceLaunch);
                    Vector3d newRot = gradientFunction.apply(newPos);
                    //System.out.printf("Moving Missile from (%s, %s, %s) to (%s, %s, %s)\n", getX(), getY(), getZ(), newPos.x, newPos.y, newPos.z);
                    this.setDeltaMovement(newPos.x-getX() + 0.5D, newPos.y-getY() + 0.5D, newPos.z-getZ() + 0.5D);
                    this.move(MoverType.SELF, this.getDeltaMovement());
                    this.setRot((float)newRot.y, (float)newRot.x);

                } else {
                    Vector3d newPos = pathFunction.apply(ticksSinceLaunch);
                    spawnParticles(getX() - newPos.x(), getY() - newPos.y(), getZ() - newPos.z());
                }

                break;

        }
    }

    @Override
    public Vector3d collide(Vector3d destination) {
        Vector3d result = collideFiltered(destination);
        if(!MathHelper.equal(destination.x, result.x) || !MathHelper.equal(destination.z, result.z) || !MathHelper.equal(destination.y, result.y)) shouldExplode = true;
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
        Stream<VoxelShape> stream1 = this.level.getEntityCollisions(this, axisalignedbb.expandTowards(pVec), (p_233561_0_) -> {
            return true;
        });
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

    public void explode() {
        if(!level.isClientSide()) {
            AbstractBlastEvent.fire(blastEventProvider, missileSourceType.getResultantBlastType(), (ServerWorld) level, blockPosition(), getDirection());
            this.kill();
        }
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(MISSILE_SOURCE_TYPE, MissileSourceType.LAUNCHER_PLATFORM.ordinal());
        entityData.define(MISSILE_LAUNCH_PHASE, MissileLaunchPhase.STATIONARY.ordinal());
        entityData.define(SOURCE_POS, BlockPos.ZERO);
        entityData.define(DEST_POS, BlockPos.ZERO.offset(100, 0, 0));
        entityData.define(PEAK_HEIGHT, 256F);
        entityData.define(TOTAL_FLIGHT_TICKS, 300);
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {

        compound.putInt("MissileSourceType", missileSourceType.ordinal());
        compound.putInt("MissileLaunchPhase", missileLaunchPhase.ordinal());

        compound.putInt("SourcePosX", sourcePos.getX());
        compound.putInt("SourcePosY", sourcePos.getY());
        compound.putInt("SourcePosZ", sourcePos.getZ());

        compound.putInt("DestPosX", destPos.getX());
        compound.putInt("DestPosY", destPos.getY());
        compound.putInt("DestPosZ", destPos.getZ());

        compound.putFloat("PeakHeight", (float)peakHeight);
        compound.putInt("TotalFlightTicks", totalFlightTicks);

    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {

        entityData.set(MISSILE_SOURCE_TYPE, compound.getInt("MissileSourceType"));
        entityData.set(MISSILE_LAUNCH_PHASE, compound.getInt("MissileLaunchPhase"));

        int sourcePosX = compound.getInt("SourcePosX");
        int sourcePosY = compound.getInt("SourcePosY");
        int sourcePosZ = compound.getInt("SourcePosZ");
        entityData.set(SOURCE_POS, new BlockPos(sourcePosX, sourcePosY, sourcePosZ));

        int destPosX = compound.getInt("DestPosX");
        int destPosY = compound.getInt("DestPosY");
        int destPosZ = compound.getInt("DestPosZ");
        entityData.set(DEST_POS, new BlockPos(destPosX, destPosY, destPosZ));

        entityData.set(PEAK_HEIGHT, compound.getFloat("PeakHeight"));
        entityData.set(TOTAL_FLIGHT_TICKS, compound.getInt("TotalFlightTicks"));

        updatePathFunctions();

    }

    @Override
    public void onSyncedDataUpdated(DataParameter<?> dataParameter) {
        if(MISSILE_SOURCE_TYPE.equals(dataParameter))  missileSourceType  = MissileSourceType.values()[entityData.get(MISSILE_SOURCE_TYPE)];
        if(MISSILE_LAUNCH_PHASE.equals(dataParameter)) missileLaunchPhase = MissileLaunchPhase.values()[entityData.get(MISSILE_LAUNCH_PHASE)];
        if(SOURCE_POS.equals(dataParameter))           sourcePos          = entityData.get(SOURCE_POS);
        if(DEST_POS.equals(dataParameter))             destPos            = entityData.get(DEST_POS);
        if(PEAK_HEIGHT.equals(dataParameter))          peakHeight         = entityData.get(PEAK_HEIGHT);
        if(TOTAL_FLIGHT_TICKS.equals(dataParameter))   totalFlightTicks   = entityData.get(TOTAL_FLIGHT_TICKS);
        if(level.isClientSide) {
            //System.out.print("[ICBM DEBUG] Syncing Missile Save Data!");
            //System.out.println(" Side = " + ((level.isClientSide) ? "Client" : "Server"));
            updatePathFunctions();
        }
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
        return missileItem.get().getDefaultInstance();
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
        if (!this.isVehicle() && !player.isSecondaryUseActive() && !missileSourceType.equals(MissileSourceType.ROCKET_LAUNCHER)) {
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
    public void setRot(float yRot, float xRot) {
        super.setRot(yRot, xRot);
    }

}
