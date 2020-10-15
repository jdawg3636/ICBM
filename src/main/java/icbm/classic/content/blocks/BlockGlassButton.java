package icbm.classic.content.blocks;

import icbm.classic.ICBMConstants;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class BlockGlassButton extends AbstractButtonBlock {

    public BlockGlassButton() {
        super(false);
        this.setTickRandomly(true);
        this.setTranslationKey(ICBMConstants.PREFIX + "glassButton");
        this.setRegistryName(ICBMConstants.PREFIX + "glassButton");
        this.setSoundType(SoundType.GLASS);
        this.setHardness(0.5F);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    @Override
    protected void playClickSound(@Nullable PlayerEntity player, World worldIn, BlockPos pos) {
        worldIn.playSound(player, pos, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
    }

    @Override
    protected void playReleaseSound(World worldIn, BlockPos pos) {
        worldIn.playSound((PlayerEntity) null, pos, SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.5F);
    }
}
