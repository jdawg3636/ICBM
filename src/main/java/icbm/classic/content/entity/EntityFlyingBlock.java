package icbm.classic.content.entity;

import icbm.classic.lib.NBTConstants;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

/** @author Calclavia */
public class EntityFlyingBlock extends Entity implements IEntityAdditionalSpawnData {

    public static final float GRAVITY_DEFAULT = 0.045f;

    private BlockState _blockState;

    public float yawChange = 0;
    public float pitchChange = 0;

    public float gravity = GRAVITY_DEFAULT;

    public EntityFlyingBlock(World world) {
        super(world);
        this.ticksExisted = 0;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        //this.yOffset = height / 2.0F;
        this.setSize(0.98F, 0.98F);
    }

    public EntityFlyingBlock(World world, BlockPos position, BlockState state) {
        this(world);
        this.setPosition(position.getX() + 0.5, position.getY(), position.getZ() + 0.5);
        this.setMotion(0, 0, 0);
        this._blockState = state;
    }

    public EntityFlyingBlock(World world, BlockPos position, BlockState state, float gravity) {
        this(world, position, state);
        this.gravity = gravity;
    }

    public void restoreGravity()
    {
        gravity = GRAVITY_DEFAULT;
    }

    public BlockState getBlockState() {
        if (_blockState == null) {
            _blockState = Blocks.STONE.getDefaultState();
        }
        return _blockState;
    }

    @Override
    public String getName() {
        return "Flying Block [" + getBlockState() + ", " + hashCode() + "]";
    }

    @Override
    public void writeSpawnData(ByteBuf data) {
        ByteBufUtils.writeTag(data, NBTUtil.writeBlockState(new CompoundNBT(), getBlockState()));
        data.writeFloat(this.gravity);
        data.writeFloat(yawChange);
        data.writeFloat(pitchChange);
    }

    @Override
    public void readSpawnData(ByteBuf data) {
        _blockState = NBTUtil.readBlockState(ByteBufUtils.readTag(data));
        gravity = data.readFloat();
        yawChange = data.readFloat();
        pitchChange = data.readFloat();
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public void onUpdate() {

        //Death state handling
        if (!world.isRemote)
        {
            if (_blockState == null || ticksExisted > 20 * 60) //1 min despawn timer{
                this.setBlock();
                return;
            }

            //TODO make a black list of blocks that shouldn't be a flying entity block
            if (this.getPosY() > 400 || this.getPosY() < -40) {
                this.remove();
                return;
            }

            if ((this.onGround && this.ticksExisted > 20)) {
                this.setBlock();
                return;
            }
        }

        //Apply gravity acceleration
        this.setMotion(this.getMotion().add(0, -gravity, 0));

        //Do movement
        this.move(MoverType.SELF, this.getMotion());

        //Handle collisions
        if (this.collidedHorizontally || this.collidedVertically) {
            this.setPosition(this.getPosX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getPosZ());
        }

        //Animation
        if (this.yawChange > 0) {
            this.rotationYaw += this.yawChange;
            this.yawChange -= 2;
        }

        if (this.pitchChange > 0) {
            this.rotationPitch += this.pitchChange;
            this.pitchChange -= 2;
        }

        //Tick update
        this.ticksExisted++;

    }

    public void setBlock() {

        if (!this.world.isRemote) {
            final int i = MathHelper.floor(getPosX());
            final int j = MathHelper.floor(getPosY());
            final int k = MathHelper.floor(getPosZ());

            final BlockPos pos = new BlockPos(i, j, k);

            final BlockState currentState = world.getBlockState(pos);

            if (currentState.getBlock().isReplaceable(this.world, pos))
                this.world.setBlockState(pos, getBlockState(), 3);
            //TODO find first block if not replaceable
        }

        this.remove();

    }

    /** Checks to see if and entity is touching the missile. If so, blow up! */

    @Override
    public AxisAlignedBB getCollisionBox(Entity par1Entity) {

        // Make sure the entity is not an item
        if (par1Entity instanceof LivingEntity) {
            if (getBlockState() != null) {
                if (!(getBlockState().getBlock() instanceof IFluidBlock) && (this.getMotion().getX() > 2 || this.getMotion().getY() > 2 || this.getMotion().getZ() > 2)) {
                    int damage = (int) (1.2 * (Math.abs(this.getMotion().getX()) + Math.abs(this.getMotion().getY()) + Math.abs(this.getMotion().getZ())));
                    (par1Entity.attackEntityFrom(DamageSource.FALLING_BLOCK, damage);
                }
            }
        }
        return null;

    }

    @Override
    protected void writeEntityToNBT(CompoundNBT nbttagcompound) {
        if (_blockState != null)
            nbttagcompound.put(NBTConstants.BLOCK_STATE, NBTUtil.writeBlockState(new CompoundNBT(), _blockState));
        nbttagcompound.putFloat(NBTConstants.GRAVITY, this.gravity);
    }

    @Override
    protected void readEntityFromNBT(CompoundNBT nbttagcompound) {
        if (nbttagcompound.contains(NBTConstants.BLOCK_STATE))
            _blockState = NBTUtil.readBlockState(nbttagcompound.getCompound(NBTConstants.BLOCK_STATE));
        this.gravity = nbttagcompound.getFloat(NBTConstants.GRAVITY);
    }

    @Override
    public boolean canBePushed() {
        return true;
    }

    @Override
    protected boolean canTriggerWalking() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

}