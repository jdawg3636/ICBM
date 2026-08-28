package com.jdawg3636.icbm.mixin.accessors;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(TileEntityType.class)
public interface ITileEntityTypeAccessor {

    @Accessor("validBlocks")
    Set<Block> mixin$ballisticRedux$getValidBlocks();

    @Mutable
    @Accessor("validBlocks")
    void mixin$ballisticRedux$setValidBlocks(Set<Block> validBlocks);

}
