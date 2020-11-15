package icbm.classic.lib.transform.vector;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.jlib.data.vector.ITransform;
import com.builtbroken.jlib.data.vector.Pos3D;
import icbm.classic.ICBMClassic;
import icbm.classic.lib.NBTConstants;
import icbm.classic.lib.transform.rotation.EulerAngle;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

/**
 * Abstract version of Pos3D for interaction with the minecraft world
 * Created by robert on 1/13/2015.
 */
public abstract class AbstractPos<R extends AbstractPos> extends Pos3D<R> implements IPosition
{
    public AbstractPos()
    {
        this(0, 0, 0);
    }

    public AbstractPos(double a)
    {
        this(a, a, a);
    }

    public AbstractPos(double x, double y, double z)
    {
        super(x, y, z);
    }

    public AbstractPos(double yaw, double pitch)
    {
        this(-Math.sin(Math.toRadians(yaw)), Math.sin(Math.toRadians(pitch)), -Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
    }

    public AbstractPos(TileEntity tile)
    {
        this(tile.getPos());
    }

    public AbstractPos(Entity entity)
    {
        this(entity.getPosX(), entity.getPosY(), entity.getPosZ());
    }

    public AbstractPos(IPos3D vec)
    {
        this(vec.x(), vec.y(), vec.z());
    }

    public AbstractPos(CompoundNBT nbt)
    {
        this(nbt.getDouble(NBTConstants.X), nbt.getDouble(NBTConstants.Y), nbt.getDouble(NBTConstants.Z));
    }

    public AbstractPos(ByteBuf data)
    {
        this(data.readDouble(), data.readDouble(), data.readDouble());
    }

    public AbstractPos(BlockPos par1)
    {
        this(par1.getX(), par1.getY(), par1.getZ());
    }

    public AbstractPos(Direction dir)
    {
        this(dir.getXOffset(), dir.getYOffset(), dir.getZOffset());
    }

    public AbstractPos(Vector3d vec)
    {
        this(vec.x, vec.y, vec.z);
    }

    public double angle(IPos3D other)
    {
        return Math.acos((this.cross(other)).magnitude() / (new Pos(other).magnitude() * magnitude()));
    }

    public double anglePreNorm(IPos3D other)
    {
        return Math.acos(this.cross(other).magnitude());
    }

    //=========================
    //========Converters=======
    //=========================

    public Vector3d toVector3d()
    {
        return new Vector3d(x(), y(), z());
    }

    public Point toVector2()
    {
        return new Point(x(), z());
    }

    public Direction toDirection()
    {
        //TODO maybe add a way to convert convert any vector into a direction from origin
        for (Direction dir : Direction.values())
        {
            if (xi() == dir.getXOffset() && yi() == dir.getYOffset() && zi() == dir.getZOffset())
            {
                return dir;
            }
        }
        return null;
    }

    public EulerAngle toEulerAngle(IPos3D target)
    {
        return sub(target).toEulerAngle();
    }

    public EulerAngle toEulerAngle()
    {
        return new EulerAngle(Math.toDegrees(Math.atan2(x(), z())), Math.toDegrees(-Math.atan2(y(), Math.hypot(z(), x()))));
    }

    public IPos3D transform(ITransform transformer)
    {
        if (this instanceof IPos3D)
        {
            return transformer.transform((IPos3D) this);
        }
        return null;
    }

    /**
     * Calls {@link Math#abs(double)} on each term of the pos data
     *
     * @return abs
     */
    public R absolute()
    {
        return newPos(Math.abs(x()), Math.abs(y()), Math.abs(z()));
    }

    //=========================
    //======Math Operators=====
    //=========================

    public R add(BlockPos other)
    {
        return add(other.getX(), other.getY(), other.getZ());
    }

    public R add(Direction face)
    {
        return add(face.getXOffset(), face.getYOffset(), face.getZOffset());
    }

    public R add(Vector3d vec)
    {
        return add(vec.x, vec.y, vec.z);
    }

    public R sub(Direction face)
    {
        return sub(face.getXOffset(), face.getYOffset(), face.getZOffset());
    }

    public R sub(Vector3d vec)
    {
        return add(vec.x, vec.y, vec.z);
    }

    public double distance(Vector3i vec)
    {
        return distance(vec.getX() + 0.5, vec.getY() + 0.5, vec.getZ() + 0.5);
    }

    public double distance(Vector3d vec)
    {
        return distance(vec.x, vec.y, vec.z);
    }

    public double distance(Entity entity)
    {
        return distance(entity.getPosX(), entity.getPosY(), entity.getPosZ());
    }

    public R multiply(Direction face)
    {
        return multiply(face.getXOffset(), face.getYOffset(), face.getZOffset());
    }

    public R multiply(Vector3d vec)
    {
        return multiply(vec.x, vec.y, vec.z);
    }

    public R divide(Direction face)
    {
        return divide(face.getXOffset(), face.getYOffset(), face.getZOffset());
    }

    public R divide(Vector3d vec)
    {
        return divide(vec.x, vec.y, vec.z);
    }

    @Override
    public R floor()
    {
        return newPos(Math.floor(x()), Math.floor(y()), Math.floor(z()));
    }

    //=========================
    //========NBT==============
    //=========================

    public CompoundNBT toNBT()
    {
        return writeNBT(new CompoundNBT());
    }

    public CompoundNBT toIntNBT()
    {
        return writeIntNBT(new CompoundNBT());
    }

    public CompoundNBT writeNBT(CompoundNBT nbt)
    {
        nbt.putDouble(NBTConstants.X, x());
        nbt.putDouble(NBTConstants.Y, y());
        nbt.putDouble(NBTConstants.Z, z());
        return nbt;
    }


    public CompoundNBT writeIntNBT(CompoundNBT nbt)
    {
        nbt.putInt(NBTConstants.X, xi());
        nbt.putInt(NBTConstants.Y, yi());
        nbt.putInt(NBTConstants.Z, zi());
        return nbt;
    }

    public ByteBuf writeByteBuf(ByteBuf data)
    {
        data.writeDouble(x());
        data.writeDouble(y());
        data.writeDouble(z());
        return data;
    }

    /*
    public RayTraceResult rayTrace(World world, IPos3D dir, double dist)
    {
        return rayTrace(world, new Pos(x() + dir.x() * dist, y() + dir.y() * dist, z() + dir.z() * dist));
    }

    public RayTraceResult rayTrace(World world, IPos3D end)
    {
        return rayTrace(world, end, false, false, false);
    }

    public RayTraceResult rayTrace(World world, IPos3D end, boolean rightClickWithBoat, boolean doColliderCheck, boolean doMiss)
    {
        RayTraceResult block = rayTraceBlocks(world, end, rightClickWithBoat, doColliderCheck, doMiss);
        RayTraceResult entity = rayTraceEntities(world, end);

        if (block == null)
            return entity;
        if (entity == null)
            return block;

        if (distance(new Pos(block.getHitVec())) < distance(new Pos(entity.getHitVec())))
            return block;

        return entity;
    }


    public BlockRayTraceResult rayTraceBlocks(World world, IPos3D end)
    {
        return rayTraceBlocks(world, end, false, false, false);
    }

    public BlockRayTraceResult rayTraceBlocks(World world, IPos3D end, boolean b1, boolean b2, boolean b3)
    {
        return world.rayTraceBlocks(new RayTraceContext(toVector3d(), new Vector3d(end.x(), end.y(), end.z()), b1, b2, b3));
    }
    */

    public EntityRayTraceResult rayTraceEntities(World world, IPos3D end) {

        EntityRayTraceResult closestEntity = null;
        double closetDistance = 0D;

        double checkDistance = distance(end);
        AxisAlignedBB scanRegion = new AxisAlignedBB(-checkDistance, -checkDistance, -checkDistance, checkDistance, checkDistance, checkDistance).offset(x(), y(), z());
        for (Entity entity : world.getEntitiesWithinAABB(Entity.class, scanRegion)) {

            if (entity != null && entity.canBeCollidedWith() && entity.getBoundingBox() != null) {

                float border = entity.getCollisionBorderSize();
                AxisAlignedBB bounds = entity.getBoundingBox().expand(border, border, border);

                Vector3d hit = bounds.rayTrace(toVector3d(), new Vector3d(end.x(), end.y(), end.z())).orElse(null);

                if (hit != null) {
                    double dist = 0;
                    if(!bounds.contains(toVector3d()))
                        dist = distance(new Pos(hit));
                    if (dist <= closetDistance) {
                        closestEntity = new EntityRayTraceResult(entity);
                        closetDistance = dist;
                    }
                }
            }
        }

        return closestEntity;
    }

    //===================
    //===World Setters===
    //===================
    public boolean setBlock(World world, Block block)
    {
        return setBlock(world, block.getDefaultState());
    }

    public boolean setBlock(World world, BlockState state)
    {
        return setBlock(world, state, 3);
    }

    public boolean setBlock(World world, BlockState block, int notify)
    {
        if (world != null && block != null)
        {
            return world.setBlockState(toBlockPos(), block, notify);
        }
        else
        {
            return false;
        }
    }

    public boolean setBlockToAir(World world)
    {
        return world.setBlockState(toBlockPos(), Blocks.AIR.getDefaultState(), 1+2 /* cause a block update & send change to clients */);
    }

    public BlockPos toBlockPos()
    {
        return new BlockPos(xi(), yi(), zi());
    }

    //===================
    //==World Accessors==
    //===================
    public boolean isAirBlock(World world)
    {
        return world.isAirBlock(toBlockPos());
    }

    @Deprecated
    public boolean isBlockFreezable(World world)
    {
        return false;
    }

    /**
     * Checks if the block is replaceable
     *
     * @return true if it can be replaced
     */
    public boolean isReplaceable(World world)
    {
        BlockPos pos = toBlockPos();
        BlockState block = world.getBlockState(pos);
        return block.getBlock().isAir(block, world, pos) || block.getBlock().isAir(block, world, toBlockPos()) || world.getBlockState(toBlockPos()).getMaterial().isReplaceable();
    }

    /**
     * Checks to see if the tile can see the sky
     *
     * @return true if it can see sky, false if not or world is null
     */
    public boolean canSeeSky(World world)
    {
        return world.canSeeSky(toBlockPos());
    }

    public boolean isBlockEqual(IBlockReader world, Block block)
    {
        Block b = getBlock(world);
        return b != null && b == block;
    }

    public Block getBlock(IBlockReader world)
    {
        BlockState state = getBlockState(world);
        if (world != null && state != null) //TODO check if chunk is loaded
        {
            return state.getBlock();
        }
        else
        {
            return null;
        }
    }

    public BlockState getBlockState(IBlockReader world)
    {
        if (world != null) //TODO check if chunk is loaded
        {
            return world.getBlockState(toBlockPos());
        }
        else
        {
            return null;
        }
    }

    public TileEntity getTileEntity(IBlockReader world)
    {
        if (world != null) //TODO check if chunk is loaded
        {
            return world.getTileEntity(toBlockPos());
        }
        return null;
    }

    public float getHardness(World world)
    {
        BlockState state = getBlockState(world);
        if (state != null && !state.getBlock().isAir(state, world, toBlockPos()))
        {
            return state.getBlockHardness(world, toBlockPos());
        }
        else
        {
            return 0;
        }
    }

    /**
     * Gets the resistance of a block using block.getExplosionResistance method
     */
    public float getResistanceToEntity(Entity cause)
    {
        return getBlock(cause.world).getExplosionResistance();
    }

    /**
     * Gets the resistance of a block using block.getExplosionResistance method
     */
    public float getResistanceToEntity(IBlockReader world)
    {
        return getBlock(world).getExplosionResistance();
    }

    /**
     * Gets the resistance of a block using block.getResistance method
     *
     * @param world - world to check in
     */
    public float getResistance(World world)
    {
        // The original ICBM-Classic implementation had many more parameters and overloads
        // As of 1.16.3, the implementation in net.minecraftforge.common.extensions.IForgeBlock ignores them
        // It ignored them in 1.12.2 as well, but divided the field by 5.0F rather than directly returning.
        return getBlock(world).getExplosionResistance();
    }

    public boolean isAboveBedrock()
    {
        return y() > 0;
    }

    public boolean isInsideMap()
    {
        return isAboveBedrock() && y() < ICBMClassic.MAP_HEIGHT;
    }

    /**
     * Marks a block for update
     *
     * @param world - world to update the location in
     */
    public void markForUpdate(World world)
    {
        BlockPos pos = toBlockPos();
        BlockState state = world.getBlockState(pos);
        if (state != null && !state.getBlock().isAir(state, world, toBlockPos()))
        {
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }

    //===================
    //==ILocation Accessors==
    //===================
    @Override
    public double getX()
    {
        return x();
    }

    @Override
    public double getY()
    {
        return y();
    }

    @Override
    public double getZ()
    {
        return z();
    }
}
