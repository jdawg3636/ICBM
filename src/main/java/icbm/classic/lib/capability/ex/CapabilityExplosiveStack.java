package icbm.classic.lib.capability.ex;

import icbm.classic.api.ICBMClassicAPI;
import icbm.classic.lib.NBTConstants;
import icbm.classic.api.caps.IExplosive;
import icbm.classic.api.reg.IExplosiveData;
import icbm.classic.content.reg.BlockReg;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Used by any item that has an explosive capability
 * Created by Dark(DarkGuardsman, Robert) on 1/7/19.
 */
public class CapabilityExplosiveStack implements IExplosive, ICapabilitySerializable<NBTTagCompound>
{
    private final ItemStack stack;
    private NBTTagCompound custom_ex_data;

    public CapabilityExplosiveStack(ItemStack stack)
    {
        this.stack = stack;
    }

    protected int getExplosiveID()
    {
        if(stack == null)
        {
            return 0;
        }
        return stack.getItemDamage(); //TODO replace meta usage for 1.14 update
    }

    @Nullable
    @Override
    public IExplosiveData getExplosiveData()
    {
        return ICBMClassicAPI.EXPLOSIVE_REGISTRY.getExplosiveData(getExplosiveID());
    }

    @Nullable
    @Override
    public CompoundNBT getCustomBlastData()
    {
        if (custom_ex_data == null)
        {
            custom_ex_data = new CompoundNBT();
        }
        return custom_ex_data;
    }

    public void setCustomData(CompoundNBT data)
    {
        this.custom_ex_data = data;
    }

    @Nullable
    @Override
    public ItemStack toStack() {

        if (stack == null)
            return new ItemStack(BlockReg.EXPLOSIVES, 1, 0);

        final ItemStack re = stack.copy();
        re.setCount(1);
        return re;

    }

    @Override
    public CompoundNBT serializeNBT()
    {
        //Do not save the stack itself as we are saving to its NBT
        CompoundNBT save = new CompoundNBT();
        if (!getCustomBlastData().isEmpty())
        {
            save.put(NBTConstants.CUSTOM_EX_DATA, getCustomBlastData());
        }
        return save;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt.contains(NBTConstants.CUSTOM_EX_DATA))
            custom_ex_data = nbt.getCompound(NBTConstants.CUSTOM_EX_DATA);
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {

        LazyOptional<Capability> toReturn = new LazyOptional<>();

        if (capability == ICBMClassicAPI.EXPLOSIVE_CAPABILITY)
            ICBMClassicAPI.EXPLOSIVE_CAPABILITY.orEmpty(capability)
            return (T) this;

        return null;

    }

}
