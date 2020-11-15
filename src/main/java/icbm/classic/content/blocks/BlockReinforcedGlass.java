package icbm.classic.content.blocks;

import icbm.classic.ICBMConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockReinforcedGlass extends Block {

    public BlockReinforcedGlass() {
        super(Block.Properties.create(Material.GLASS).hardnessAndResistance(10, 48));
        //TODO//this.setCreativeTab(ICBMClassic.CREATIVE_TAB);
    }

    /* TODO - might have to be done through loot tables now? https://github.com/MinecraftForge/MinecraftForge/blob/1.15.x/src/test/java/net/minecraftforge/debug/gameplay/loot/GlobalLootModifiersTest.java
    @Override
    protected boolean canSilkHarvest() {
        return true;
    }
    */

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isSideInvisible(BlockState p_200122_1_, BlockState p_200122_2_, Direction p_200122_3_) {
        // Pretty sure p_200122_1_ is "this" but in BlockState form, overrides code from AbstractBlock that is obfuscated af so this could be wrong
        return p_200122_1_.getBlock() == p_200122_2_.getBlock() || super.isSideInvisible(p_200122_1_, p_200122_2_, p_200122_3_);
    }

}
