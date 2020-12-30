package icbm.content.entity;

import icbm.content.event.BlastEvent;
import icbm.content.reg.BlockReg;
import icbm.content.reg.EntityReg;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityExplosivesIncendiary extends TNTEntity {

    public EntityExplosivesIncendiary(EntityType<? extends EntityExplosivesIncendiary> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityExplosivesIncendiary(World worldIn, double x, double y, double z, @Nullable LivingEntity igniter) {
        this(EntityReg.EXPLOSIVES_INCENDIARY.get(), worldIn);
        this.setPosition(x, y, z);
        double d0 = worldIn.rand.nextDouble() * (double)((float)Math.PI * 2F);
        this.setMotion(-Math.sin(d0) * 0.02D, (double)0.2F, -Math.cos(d0) * 0.02D);
        this.setFuse(80);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.tntPlacedBy = igniter;
    }

    @Override
    protected void explode() {
        MinecraftForge.EVENT_BUS.post(new BlastEvent.Incendiary(getPosition(), getEntityWorld()));
    }

    @Nonnull
    @Override
    public EntityType<?> getType() {
        return EntityReg.EXPLOSIVES_INCENDIARY.get();
    }

    @Nonnull
    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(BlockReg.EXPLOSIVES_INCENDIARY.get());
    }

}
