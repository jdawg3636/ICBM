package icbm.classic.lib.capability.ex;

import com.google.common.base.Optional;
import icbm.classic.api.ICBMClassicAPI;
import icbm.classic.api.ICBMClassicHelpers;
import icbm.classic.lib.NBTConstants;
import icbm.classic.api.caps.IExplosive;
import icbm.classic.api.reg.IExplosiveData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Dark(DarkGuardsman, Robert) on 1/7/19.
 */
public class CapabilityExplosive implements IExplosive, ICapabilitySerializable<CompoundNBT>
{
    public int explosiveID; //TODO change over to resource location or include in save to check for issues using ID only for in memory
    public CompoundNBT blastNBT;

    public CapabilityExplosive()
    {
    }

    public CapabilityExplosive(int id)
    {
        this.explosiveID = id;
    }

    @Nullable
    @Override
    public IExplosiveData getExplosiveData()
    {
        return ICBMClassicHelpers.getExplosive(explosiveID, false);
    }

    @Nullable
    @Override
    public CompoundNBT getCustomBlastData()
    {
        if (blastNBT == null)
        {
            blastNBT = new CompoundNBT();
        }
        return blastNBT;
    }

    public void setCustomData(CompoundNBT data)
    {
        blastNBT = data;
    }

    @Nullable
    @Override
    public ItemStack toStack()
    {
        return null;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        if (capability == ICBMClassicAPI.EXPLOSIVE_CAPABILITY) {
            // Official documentation is not yet updated, porting file-by-file for the time being so hoping this works
            // https://forums.minecraftforge.net/topic/69813-lazyoptional/
            return LazyOptional.of(() -> ICBMClassicAPI.EXPLOSIVE_CAPABILITY).cast();
        }
        return null;
    }

    @Override
    public final CompoundNBT serializeNBT()
    {
        final CompoundNBT tagCompound = new CompoundNBT();
        serializeNBT(tagCompound);

        tagCompound.putInt(NBTConstants.EXPLOSIVE_ID, explosiveID);
        tagCompound.put(NBTConstants.BLAST_DATA, getCustomBlastData());
        return tagCompound;
    }

    protected void serializeNBT(CompoundNBT tag)
    {

    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        if (nbt.contains(NBTConstants.EXPLOSIVE_ID))
        {
            explosiveID = nbt.getInt(NBTConstants.EXPLOSIVE_ID);
        }
        if (blastNBT == null || nbt.contains(NBTConstants.BLAST_DATA))
        {
            blastNBT = nbt.getCompound(NBTConstants.BLAST_DATA);
        }
    }

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IExplosive.class, new Capability.IStorage<IExplosive>()
        {
            @Nullable
            @Override
            public INBT writeNBT(Capability<IExplosive> capability, IExplosive instance, Direction side)
            {
                if (instance instanceof CapabilityExplosive)
                {
                    return ((CapabilityExplosive) instance).serializeNBT();
                }
                return null;
            }

            @Override
            public void readNBT(Capability<IExplosive> capability, IExplosive instance, Direction side, INBT nbt)
            {
                if (instance instanceof CapabilityExplosive && nbt instanceof CompoundNBT)
                {
                    ((CapabilityExplosive) instance).deserializeNBT((CompoundNBT) nbt);
                }
            }
        },
        () -> new CapabilityExplosive(-1));
    }
}
