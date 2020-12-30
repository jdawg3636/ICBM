package icbm.content.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockReinforcedGlass extends Block {

    public BlockReinforcedGlass() {
        super(Block.Properties.create(Material.GLASS).hardnessAndResistance(10, 48).notSolid());
    }

    /**
     * Copied from {@link net.minecraft.block.AbstractGlassBlock}
     */
    @OnlyIn(Dist.CLIENT)
    public float getAmbientOcclusionLightValue(BlockState p_220080_1_, IBlockReader p_220080_2_, BlockPos p_220080_3_) {
        return 1.0F;
    }

    /**
     * Prevents the rendering of internal faces when multiple blocks of the same type are placed next to each other
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isSideInvisible(BlockState p_200122_1_, BlockState p_200122_2_, Direction p_200122_3_) {
        // Pretty sure p_200122_1_ is "this" but in BlockState form, overrides code from AbstractBlock that is obfuscated af so this could be wrong
        return p_200122_1_.getBlock() == p_200122_2_.getBlock() || super.isSideInvisible(p_200122_1_, p_200122_2_, p_200122_3_);
    }

    /**
     * Copied from {@link net.minecraft.block.AbstractGlassBlock}
     */
    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    /* TODO - might have to be done through loot tables now? https://github.com/MinecraftForge/MinecraftForge/blob/1.15.x/src/test/java/net/minecraftforge/debug/gameplay/loot/GlobalLootModifiersTest.java
    @Override
    protected boolean canSilkHarvest() {
        return true;
    }
    */

}
