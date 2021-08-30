package com.jdawg3636.icbm.common.blast;

import net.minecraft.nbt.CompoundNBT;

public class Blast {

    public net.minecraft.nbt.CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    public static Blast deserializeNBT(net.minecraft.nbt.CompoundNBT nbt) {
        return new Blast();
    }

}
