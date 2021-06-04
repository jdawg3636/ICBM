package com.jdawg3636.icbm.common.entity;

import com.jdawg3636.icbm.common.event.BlastEvent;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
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
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Random;
import java.util.function.Function;

public class EntityMissile extends Entity {

    public enum MissileSourceType {

        LAUNCHER_PLATFORM(BlastEvent.Type.PLATFORM_MISSILE),
        CRUISE_LAUNCHER(BlastEvent.Type.CRUISE_MISSILE),
        ROCKET_LAUNCHER(BlastEvent.Type.HANDHELD_ROCKET);

        MissileSourceType(final BlastEvent.Type blastType) {
            this.blastType = blastType;
        }

        private BlastEvent.Type blastType;

        public BlastEvent.Type getResultantBlastType() {
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

    public BlastEvent.BlastEventProvider blastEventProvider;
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

    public EntityMissile(EntityType<?> entityTypeIn, World worldIn, BlastEvent.BlastEventProvider blastEventProvider, RegistryObject<Item> missileItem) {
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
    }

    public EntityMissile(EntityType<?> entityTypeIn, World worldIn, BlastEvent.BlastEventProvider blastEventProvider, RegistryObject<Item> missileItem, MissileSourceType missileSourceType, MissileLaunchPhase missileLaunchPhase, BlockPos sourcePos, BlockPos destPos, double peakHeight, int totalFlightTicks) {
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
        Tuple<Function<Integer, Vector3d>, Function<Vector3d, Vector3d>> pathFunctions = calculatePathFunctions(sourcePos, destPos, peakHeight, totalFlightTicks);
        this.pathFunction = pathFunctions.getA();
        this.gradientFunction = pathFunctions.getB();
    }

    // TODO: Take into account source/dest height
    /**
     * @return Function for Position, Function for Gradient
     */
    public static Tuple<Function<Integer, Vector3d>, Function<Vector3d, Vector3d>> calculatePathFunctions(BlockPos sourcePos, BlockPos destPos, double peakHeight, int totalFlightTicks) {

        double[] parabolaXCoefficients = generateParabola(sourcePos.getX(), sourcePos.getY(), destPos.getX(), destPos.getY(), peakHeight);
        double[] parabolaZCoefficients = generateParabola(sourcePos.getZ(), sourcePos.getY(), destPos.getZ(), destPos.getY(), peakHeight);

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
                        double y = funcParabolaXCoefficients[0] != 0d ? yFromX : yFromZ;

                        return new Vector3d(x, y, z);

                    }

                },

                // Takes Position, Returns Euler Rotations
                new Function<Vector3d, Vector3d>() {

                    final double[] funcParabolaXCoefficients = parabolaXCoefficients;
                    final double[] funcParabolaZCoefficients = parabolaZCoefficients;

                    @Override
                    public Vector3d apply(Vector3d position) {

                        double slopeX = 2d * funcParabolaZCoefficients[0] * position.z + funcParabolaZCoefficients[1];
                        double slopeZ = 2d * funcParabolaXCoefficients[0] * position.x + funcParabolaXCoefficients[1];
                        double slopeY = 0d;

                        return new Vector3d(slopeX, slopeY, slopeZ);

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
        // heighest x value (and therefore heighest x^2 value) as otherwise a value of 0 could cause serious problems

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

    //@Override
    //@OnlyIn(Dist.CLIENT)
    //public void lerpTo(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_) {
    //    this.lerpX = p_180426_1_;
    //    this.lerpY = p_180426_3_;
    //    this.lerpZ = p_180426_5_;
    //    this.lerpYRot = (double)p_180426_7_;
    //    this.lerpXRot = (double)p_180426_8_;
    //    this.lerpSteps = 10;
    //}
    //
    //// Copied from BoatEntity
    //private void tickLerp() {
    //    if (this.isControlledByLocalInstance()) {
    //        this.lerpSteps = 0;
    //        this.setPacketCoordinates(this.getX(), this.getY(), this.getZ());
    //    }
    //
    //    if (this.lerpSteps > 0) {
    //        double d0 = this.getX() + (this.lerpX - this.getX()) / (double)this.lerpSteps;
    //        double d1 = this.getY() + (this.lerpY - this.getY()) / (double)this.lerpSteps;
    //        double d2 = this.getZ() + (this.lerpZ - this.getZ()) / (double)this.lerpSteps;
    //        double d3 = MathHelper.wrapDegrees(this.lerpYRot - (double)this.yRot);
    //        this.yRot = (float)((double)this.yRot + d3 / (double)this.lerpSteps);
    //        this.xRot = (float)((double)this.xRot + (this.lerpXRot - (double)this.xRot) / (double)this.lerpSteps);
    //        --this.lerpSteps;
    //        this.setPos(d0, d1, d2);
    //        this.setRot(this.yRot, this.xRot);
    //    }
    //}

    @Override
    public void tick() {
        super.tick();
        switch(missileLaunchPhase) {

            case STATIONARY:
                break;

            case STATIONARY_ACTIVATED:
                spawnParticles();
                break;

            case LAUNCHED:

                ticksSinceLaunch++;

                spawnParticles();
                if(!level.isClientSide() && ProjectileHelper.getHitResult(this, (entity) -> entity instanceof EntityMissile).getType() != RayTraceResult.Type.MISS) explode();
                if(!level.isClientSide()) {
                    // Based on subsections of AbstractArrowEntity.tick()
                    BlockPos blockpos = this.blockPosition();
                    BlockState blockstate = this.level.getBlockState(blockpos);
                    if (!blockstate.isAir(this.level, blockpos)) {
                        VoxelShape voxelshape = blockstate.getCollisionShape(this.level, blockpos);
                        if (!voxelshape.isEmpty()) {
                            Vector3d vector3d1 = this.position();

                            for(AxisAlignedBB axisalignedbb : voxelshape.toAabbs()) {
                                if (axisalignedbb.move(blockpos).contains(vector3d1)) {
                                    explode();
                                    break;
                                }
                            }
                        }
                    }
                    if (ProjectileHelper.getEntityHitResult(this.level, this, position(), position().add(getDeltaMovement()), this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), (entity) -> entity instanceof EntityMissile) != null) {
                        explode();
                        break;
                    }
                    if(getY() <= 0) {
                        explode();
                        break;
                    }
                }

                // Debug Alternatives to Real Calculations
                //Vector3d newPos = new Vector3d(getX() + 1d/20d, getY() + 1d/20d, getZ() + 1d/20d);
                //this.setRot((float)Math.PI/2, 0);

                // Correct Position
                Vector3d newPos = pathFunction.apply(ticksSinceLaunch);

                // Correct Rotation
                Vector3d newRot = gradientFunction.apply(newPos);
                this.setRot((float)newRot.x, (float)newRot.z);

                System.out.printf("[ICBM DEBUG] [%s] Setting Position of Missile to %f, %f, %f\n", level.isClientSide() ? "Client" : "Server", newPos.x, newPos.y, newPos.z);
                this.teleportTo(newPos.x(), newPos.y(), newPos.z());

                break;
        }
    }

    // TODO Trigger Appropriate Blast Event
    public void explode() {
        level.explode(this, this.getX(), this.getY(), this.getZ(), 4.0F, Explosion.Mode.BREAK);
        if(!level.isClientSide()) {
            MinecraftForge.EVENT_BUS.post(
                    blastEventProvider.getBlastEvent(blockPosition(), (ServerWorld) level, missileSourceType.getResultantBlastType())
            );
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

        //System.out.print("[ICBM DEBUG] Reading Missile Save Data!");
        //System.out.println(" Side = " + ((level.isClientSide) ? "Client" : "Server"));

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

    public void spawnParticles() {
        if(this.level.isClientSide()) {
            Random random = new Random();
            for(int i = 0; i < 4; i++)     this.level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, false, this.getX(), this.getY(), this.getZ(), random.nextDouble()/1.5 - 0.3333, random.nextDouble()/-1.5 - 0.3333, random.nextDouble()/1.5 - 0.3333);
            for(int i = 0; i < 4; i++)     this.level.addParticle(ParticleTypes.CLOUD,               false, this.getX(), this.getY(), this.getZ(), random.nextDouble()/1.5 - 0.3333, random.nextDouble()/-1.5 - 0.3333, random.nextDouble()/1.5 - 0.3333);
            if(random.nextFloat() < 0.25f) this.level.addParticle(ParticleTypes.EXPLOSION,           true,  this.getX() + (3 * random.nextDouble() - 1.5), this.getY()- 1.5 * random.nextDouble(), this.getZ() + (3 * random.nextDouble() - 1.5), random.nextDouble()/1.5 - 0.3333, random.nextDouble()/-1.5 - 0.3333, random.nextDouble()/1.5 - 0.3333);
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
        if (!this.isVehicle() && !player.isSecondaryUseActive()) {
            if (!this.level.isClientSide) {
                player.startRiding(this);
            }
            return ActionResultType.sidedSuccess(this.level.isClientSide);
        }
        else {
            return ActionResultType.PASS;
        }
    }

}
