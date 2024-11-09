package com.jdawg3636.icbm.common.capability.missiledirector;

import com.jdawg3636.icbm.ICBMReference;
import com.jdawg3636.icbm.common.block.launcher_platform.TileLauncherPlatform;
import com.jdawg3636.icbm.common.capability.ICBMCapabilities;
import com.jdawg3636.icbm.common.entity.EntityMissile;
import com.jdawg3636.icbm.common.event.AbstractBlastEvent;
import com.jdawg3636.icbm.common.event.BlastEventRegistryEntry;
import com.jdawg3636.icbm.common.item.ItemMissile;
import com.jdawg3636.icbm.common.reg.BlastEventReg;
import com.jdawg3636.icbm.common.reg.ItemReg;
import com.jdawg3636.icbm.common.reg.SoundEventReg;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.RegistryObject;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class LogicalMissile {

    public RegistryObject<BlastEventRegistryEntry> blastEventProvider;
    public RegistryObject<Item> missileItem;
    public MissileSourceType missileSourceType;
    public MissileLaunchPhase missileLaunchPhase;
    public BlockPos sourcePos;
    public BlockPos destPos;
    public double peakHeight;
    public int totalFlightTicks;
    public Function<Integer, Vector3d> pathFunction = null;
    public Function<Vector3d, Vector3d> gradientFunction = null;

    public int ticksSinceLaunch = 0;
    public boolean shouldExplode = false;

    public ServerWorld level;
    public Optional<UUID> puppetEntityUUID;
    public double x;
    public double y;
    public double z;
    public float yRot;
    public float xRot;
    public boolean removed = false;

    public LogicalMissile(RegistryObject<BlastEventRegistryEntry> blastEventProvider, RegistryObject<Item> missileItem, MissileSourceType missileSourceType, MissileLaunchPhase missileLaunchPhase, BlockPos sourcePos, BlockPos destPos, double peakHeight, int totalFlightTicks, Optional<UUID> puppetEntityUUID, ServerWorld level) {
        this.blastEventProvider = blastEventProvider;
        this.missileItem = missileItem;
        this.missileSourceType = missileSourceType;
        this.missileLaunchPhase = missileLaunchPhase;
        this.sourcePos = sourcePos;
        this.destPos = destPos;
        this.peakHeight = peakHeight;
        this.totalFlightTicks = totalFlightTicks;
        this.puppetEntityUUID = puppetEntityUUID;
        this.level = level;
    }

    public LogicalMissile(ServerWorld level, CompoundNBT compoundNBT) {
        this.level = level;
        this.load(compoundNBT);
    }

    public Optional<EntityMissile> getPuppetEntity() {
        return this.puppetEntityUUID.flatMap(uuid -> Optional.ofNullable(level.getEntity(uuid))).filter(EntityMissile.class::isInstance).map(EntityMissile.class::cast);
    }

    public void setMissileSourceType(int missileSourceType) {
        this.missileSourceType = MissileSourceType.values()[missileSourceType];
        getPuppetEntity().ifPresent(pe -> pe.getEntityData().set(EntityMissile.MISSILE_SOURCE_TYPE, missileSourceType));
    }

    public void setMissileLaunchPhase(int missileLaunchPhase) {
        this.missileLaunchPhase = MissileLaunchPhase.values()[missileLaunchPhase];
        getPuppetEntity().ifPresent(pe -> pe.getEntityData().set(EntityMissile.MISSILE_LAUNCH_PHASE, missileLaunchPhase));
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
                ticks -> new Vector3d(deltaX,deltaY,deltaZ).scale(totalFlightTicks != 0 ? ((double)ticks) / totalFlightTicks : 0).add(Vector3d.atCenterOf(sourcePos)),
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

        // Precalculating a Multiple for Rotation About the X axis, since only the position (and not the direction of travel) is known at runtime
        // Need to calculate for both X and Z, as the choice of which to use for calculating Y is made at runtime
        final int rotXMultipleFromX = destPos.getX() >= sourcePos.getX() ? -1 : 1;
        final int rotXMultipleFromZ = destPos.getZ() >= sourcePos.getZ() ? -1 : 1;

        return new Tuple<>(
                // Takes Ticks, Returns Position
                ticks -> {
                    final int funcTotalFlightTicks = totalFlightTicks != 0 ? totalFlightTicks : 1;

                    double x = sourcePos.getX() + ((double) (destPos.getX() - sourcePos.getX()) * ticks / funcTotalFlightTicks);
                    double z = sourcePos.getZ() + ((double) (destPos.getZ() - sourcePos.getZ()) * ticks / funcTotalFlightTicks);

                    double yFromX = parabolaXCoefficients[0] * x * x + parabolaXCoefficients[1] * x + parabolaXCoefficients[2];
                    double yFromZ = parabolaZCoefficients[0] * z * z + parabolaZCoefficients[1] * z + parabolaZCoefficients[2];
                    double y = !Double.isNaN(parabolaXCoefficients[0]) ? yFromX : yFromZ;

                    return new Vector3d(x, y, z);
                },
                // Takes Position, Returns Euler Rotations
                position -> {
                    double rotXFromX = rotXMultipleFromX * Math.toDegrees(Math.atan(2d * parabolaXCoefficients[0] * position.x + parabolaXCoefficients[1]));
                    double rotXFromZ = rotXMultipleFromZ * Math.toDegrees(Math.atan(2d * parabolaZCoefficients[0] * position.z + parabolaZCoefficients[1]));
                    Vector3d toReturn = new Vector3d((!Double.isNaN(parabolaXCoefficients[0]) ? rotXFromX : rotXFromZ), finalRotY, 0D);
                    return toReturn;
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

        // Ensure that the First Equation (x1Squared * a + x1 * b + x1Const * c = y1) has the higher absolute value
        // of x (and therefore higher x^2) as otherwise a value of 0 could cause serious problems
        final boolean sourceHorizAbsIsGreater = Math.abs(sourceHoriz) > Math.abs(targetHoriz);

        // Coefficients for 'c'
        double x1Const = 1;
        double x2Const = 1;
        double x3Const = 1;

        // Coefficients for 'b'
        double x1 = sourceHorizAbsIsGreater  ? sourceHoriz : targetHoriz;
        double x2 = (sourceHoriz + targetHoriz) / 2d;
        double x3 = !sourceHorizAbsIsGreater ? sourceHoriz : targetHoriz;

        // Coefficients for 'a'
        double x1Squared = x1 * x1;
        double x2Squared = x2 * x2;
        double x3Squared = x3 * x3;

        // Right Side of Equations
        double y1 = sourceHorizAbsIsGreater  ? sourceHeight : targetHeight;
        double y2 = peakHeightIn;
        double y3 = !sourceHorizAbsIsGreater ? sourceHeight : targetHeight;

        // Reused Temp Var for Scaling Process
        double multiple;

        // Scale Equation 2 using Equation 1
        multiple    = -x2Squared/x1Squared;
        //x2Squared   += multiple * x1Squared;
        x2          += multiple * x1;
        x2Const     += multiple * x1Const;
        y2          += multiple * y1;

        // Scale Equation 3 using Equation 1
        multiple    = -x3Squared/x1Squared;
        //x3Squared   += multiple * x1Squared;
        x3          += multiple * x1;
        x3Const     += multiple * x1Const;
        y3          += multiple * y1;

        // Scale Equation 3 using Equation 2
        multiple    = -x3/x2;
        //x3Squared   += multiple * x2Squared;
        //x3          += multiple * x2;
        x3Const     += multiple * x2Const;
        y3          += multiple * y2;

        // Final Solve
        double c =  y3 / x3Const;
        double b = (y2 - x2Const * c) / x2;
        double a = (y1 - x1Const * c - x1 * b) / x1Squared;

        // Package and Return
        return new double[]{a,b,c};

    }

    /**
     * Tick function for the logical/simulated missile. Note that this is assumed to only run on the logical server
     * AND note that the puppet entity (if it exists) will still tick as usual, handling certain client-side behavior.
     * @return flag for whether missile was modified
     */
    public boolean tick(ServerWorld level) {

        switch(missileLaunchPhase) {

            case STATIONARY:
            case STATIONARY_ACTIVATED:
                return false;

            case LAUNCHED:

                ticksSinceLaunch++;

                if(shouldExplode) {
                    explode(level);
                    break;
                }

                if(missileSourceType != MissileSourceType.LAUNCHER_PLATFORM && ticksSinceLaunch > ICBMReference.COMMON_CONFIG.getMaxNumTicksAliveForLinearMissiles()) {
                    explode(level);
                    break;
                }

                if(missileItem.equals(ItemReg.MISSILE_ANTIBALLISTIC) && ticksSinceLaunch >= totalFlightTicks) {
                    explode(level);
                    break;
                }

                // If below the world, die
                if(y < 0 && World.isOutsideBuildHeight((int) y)) {
                    kill(); // Don't Explode, Just Disappear.
                    break;
                }
                // If above the world, and there is no passenger, despawn puppet
                else if(y > 0 && World.isOutsideBuildHeight((int) y) && getPuppetEntity().map(pe -> pe.getPassengers().isEmpty()).orElse(true)) {
                    getPuppetEntity().ifPresent(EntityMissile::killPuppet);
                }
                // If in world, and puppet is despawned, then respawn
                else if(!getPuppetEntity().isPresent()) {
                    EntityMissile newPuppet = new EntityMissile(((ItemMissile) this.missileItem.get()).getMissileEntity().get(), this.level, uuid -> {
                        this.puppetEntityUUID = Optional.of(uuid);
                        return Optional.of(this);
                    }, this.missileItem, getLogicalUUID());
                    newPuppet.updatePuppetToMatchLogical();
                    level.addFreshEntity(newPuppet);
                }

                if ((ticksSinceLaunch-1) % 40 == 0) {
                    getPuppetEntity().ifPresent(entity -> entity.level.playSound((PlayerEntity) null, x, y, z, SoundEventReg.EFFECT_MISSILE_FLIGHT.get(), SoundCategory.BLOCKS, 2.5F, 1.0F));
                }

                Vector3d newPos = pathFunction.apply(ticksSinceLaunch);
                Vector3d newRot = gradientFunction.apply(newPos);
                //System.out.printf("Moving Missile from (%s, %s, %s) to (%s, %s, %s)\n", getX(), getY(), getZ(), newPos.x, newPos.y, newPos.z);
                this.move(newPos.x + 0.5, newPos.y + 0.5, newPos.z + 0.5);
                this.setRot((float)newRot.y, (float)newRot.x);

                break;

        }
        return true;
    }

    public void explode(ServerWorld level) {
        AbstractBlastEvent.fire(blastEventProvider.get(), missileSourceType.getResultantBlastType(), level, blockPosition(), getDirection());
        this.kill();
    }

    public CompoundNBT save(CompoundNBT compound) {

        compound.putString("BlastEvent", blastEventProvider.getId().toString());
        compound.putString("MissileItem", missileItem.getId().toString());

        if(puppetEntityUUID.isPresent()) compound.putUUID("PuppetEntityUUID", puppetEntityUUID.orElse(null));
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
        compound.putInt("TicksSinceLaunch", ticksSinceLaunch);
        compound.putBoolean("ShouldExplode", shouldExplode);

        compound.putDouble("x", x);
        compound.putDouble("y", y);
        compound.putDouble("z", z);
        compound.putFloat("yRot", yRot);
        compound.putFloat("xRot", xRot);

        return compound;

    }

    public void load(CompoundNBT compound) {

        this.blastEventProvider = BlastEventReg.getRegistryObjectFromResourceLocation(new ResourceLocation(compound.getString("BlastEvent")));
        this.missileItem = ItemReg.getRegistryObjectFromResourceLocation(new ResourceLocation(compound.getString("MissileItem")));

        puppetEntityUUID = compound.contains("PuppetEntityUUID") ? Optional.of(compound.getUUID("PuppetEntityUUID")) : Optional.empty();
        setMissileSourceType(compound.getInt("MissileSourceType"));
        setMissileLaunchPhase(compound.getInt("MissileLaunchPhase"));

        int sourcePosX = compound.getInt("SourcePosX");
        int sourcePosY = compound.getInt("SourcePosY");
        int sourcePosZ = compound.getInt("SourcePosZ");
        sourcePos = new BlockPos(sourcePosX, sourcePosY, sourcePosZ);

        int destPosX = compound.getInt("DestPosX");
        int destPosY = compound.getInt("DestPosY");
        int destPosZ = compound.getInt("DestPosZ");
        destPos = new BlockPos(destPosX, destPosY, destPosZ);

        peakHeight = compound.getFloat("PeakHeight");
        totalFlightTicks = compound.getInt("TotalFlightTicks");
        ticksSinceLaunch = compound.getInt("TicksSinceLaunch");
        shouldExplode = compound.getBoolean("ShouldExplode");

        x = compound.getDouble("x");
        y = compound.getDouble("y");
        z = compound.getDouble("z");
        yRot = compound.getFloat("yRot");
        xRot = compound.getFloat("xRot");

        updatePathFunctions();

    }

    public void updateMissileData(BlockPos sourcePos, BlockPos destPos, Float peakHeight, Integer totalFlightTicks, MissileSourceType missileSourceType) {
        if(sourcePos != null) this.sourcePos = sourcePos;
        if(destPos != null) this.destPos = destPos;
        if(peakHeight != null) this.peakHeight = peakHeight;
        if(totalFlightTicks != null) this.totalFlightTicks = totalFlightTicks;
        if(missileSourceType != null) setMissileSourceType(missileSourceType.ordinal());
        updatePathFunctions();
    }

    public void strikeWithEMP() {
        this.getPuppetEntity().ifPresent(pe -> pe.spawnAtLocation(this.missileItem.get().getDefaultInstance()));
        this.kill();
    }

    public LazyOptional<IMissileDirectorCapability> getMissileDirector() {
        return level.getCapability(ICBMCapabilities.MISSILE_DIRECTOR_CAPABILITY);
    }

    public Optional<UUID> getLogicalUUID() {
        return getMissileDirector().resolve().flatMap(md -> md.lookupLogicalMissile(this));
    }

    public void kill() {
        // Prevent loop between kill functions
        if(this.removed) return;
        this.removed = true;
        // Cut connections from the launcher platform TileEntity (only relevant if we're still on the launch pad)
        getPuppetEntity().ifPresent(missileEntity -> Optional.ofNullable(level.getBlockEntity(sourcePos)).filter(TileLauncherPlatform.class::isInstance).map(TileLauncherPlatform.class::cast).ifPresent(tileLauncherPlatform -> {
            if (missileEntity.getUUID().equals(tileLauncherPlatform.missileEntityID)) {
                tileLauncherPlatform.removeMissileItemWithAction((entity) -> true);
            }
        }));
        // Kill puppet entity, if it exists
        this.getPuppetEntity().ifPresent(EntityMissile::killPuppet);
        // Remove from simulation
        getMissileDirector().ifPresent(md -> md.deleteMissile(this));
    }

    public BlockPos blockPosition() {
        return new BlockPos(
                MathHelper.floor(this.x),
                MathHelper.floor(this.y),
                MathHelper.floor(this.z)
        );
    }

    public Direction getDirection() {
        return Direction.fromYRot((double)this.yRot);
    }

    public void move(double x, double y, double z) {
        this.getPuppetEntity().ifPresent(entity -> {
            entity.setDeltaMovement(x - this.x, y - this.y, z - this.z);
            entity.move(MoverType.SELF, entity.getDeltaMovement());
        });
        if(!this.getPuppetEntity().isPresent()) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public void setRot(float yRot, float xRot) {
        this.yRot = yRot;
        this.xRot = xRot;
        this.getPuppetEntity().ifPresent(pe -> pe.setRot(yRot, xRot));
    }

    @Override
    public String toString() {
        return String.format("LogicalMissile[%s][Phase %s] @ (%s, %s, %s)", missileItem, missileLaunchPhase.ordinal(), x, y, z);
    }


}
