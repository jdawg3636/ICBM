package icbm.classic.prefab.tile;

import icbm.classic.lib.NBTConstants;
import net.minecraft.nbt.CompoundNBT;

/**
 * Prefab for tiles that need to receive or send a signal at a Hz value
 */
public abstract class TileFrequency extends TilePoweredMachine
{
    /**
     * Frequency of the device
     */
    private int frequency = 0;

    /**
     * What is the frequency of the device
     *
     * @return Hz value
     */
    public int getFrequency()
    {
        return this.frequency;
    }

    /**
     * Called to se the frequency of the device
     *
     * @param frequency - Hz value
     */
    public void setFrequency(int frequency)
    {
        this.frequency = frequency;
    }

    /** Reads a tile entity from NBT. */
    @Override
    public void readFromNBT(CompoundNBT nbt)
    {
        super.readFromNBT(nbt);
        this.frequency = nbt.getInt(NBTConstants.FREQUENCY);
    }

    /** Writes a tile entity to NBT. */
    @Override
    public CompoundNBT writeToNBT(CompoundNBT nbt)
    {
        nbt.putInt(NBTConstants.FREQUENCY, this.frequency);
        return super.writeToNBT(nbt);
    }
}
