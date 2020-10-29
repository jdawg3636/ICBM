package icbm.classic.prefab.item;

import icbm.classic.lib.IOUtil;
import icbm.classic.lib.LanguageUtility;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Generic prefab to use in all items providing common implementation
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 12/20/2016.
 */
public class BlockItemAbstract extends BlockItem {

    //Make sure to mirror all changes to other abstract class
    public BlockItemAbstract(Block p_i45328_1_) {
        super(p_i45328_1_, new Item.Properties());
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {

        BlockState blockstate = context.getWorld().getBlockState(context.getPos());
        Block block = blockstate.getBlock();

        if (!block.isReplaceable(context.getWorld(), context.getPos()))
            pos = pos.offset(facing);

        ItemStack itemstack = context.getPlayer().getHeldItem(context.getHand());

        if (!itemstack.isEmpty() && canPlace(new BlockItemUseContext(context.getPlayer(), context.getHand(), itemstack, context.getHitVec()))) {

            int i = this.getMetadata(itemstack.getMetadata());
            BlockState blockstate1 = this.block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player, hand);

            if (placeBlockAt(itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, blockstate1)) {
                blockstate1 = context.getWorld().getBlockState(context.getPos());
                SoundType soundtype = blockstate1.getBlock().getSoundType(blockstate1, context.getWorld(), context.getPos(), context.getPlayer());
                context.getWorld().playSound(context.getPlayer(), context.getPos(), soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                itemstack.shrink(1);
            }

            return ActionResultType.SUCCESS;

        }
        else {
            return ActionResultType.FAIL;
        }
    }

    protected boolean canPlace(BlockItemUseContext context, BlockState state) {
        return context.getPlayer().canPlayerEdit(context.getPos(), context.getFace(), new ItemStack(this.getItem())) && context.getWorld().isBlockModifiable(this.getBlock(), context.getPos(), false, context.getFace(), (Entity) null);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List list, ITooltipFlag flag) {

        //Get player, don't run tool tips without
        PlayerEntity player = Minecraft.getInstance().player;

        try {

            //Generic info
            String translationKey = getTranslationKey() + ".info";
            String translation = LanguageUtility.getLocal(translationKey);
            if (!translation.isEmpty() && !translation.equals(translationKey))
                list.add(translation);

            if (hasDetailedInfo(stack, player))
                getDetailedInfo(stack, player, list);

            if (hasShiftInfo(stack, player)) {
                if (IOUtil.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
                    list.add(LanguageUtility.getLocal("info.voltzengine:tooltip.noShift").replace("#0", "\u00a7b").replace("#1", "\u00a77"));
                else
                    getShiftDetailedInfo(stack, player, list);
            }

        } catch (Exception e) {
            //TODO display tooltip if error happens to often
            e.printStackTrace();
        }

    }

    /**
     * Gets the detailed information for the item shown after the
     * global generic item details.
     *
     * @param stack
     * @param player
     * @param list
     */
    protected void getDetailedInfo(ItemStack stack, @Nullable PlayerEntity player, List list) {

        //Per item detailed info
        String translationKey = getTranslationKey(stack) + ".info";
        String translation = LanguageUtility.getLocal(translationKey);

        if (!translation.isEmpty() && !translation.equals(translationKey))
            list.addAll(LanguageUtility.splitByLine(translation));

    }

    /**
     * Gets the detailed when shift is held information for the item shown after the
     * global generic item details.
     * <p>
     * This is in addition to normal details
     *
     * @param stack
     * @param player
     * @param list
     */
    protected void getShiftDetailedInfo(ItemStack stack, @Nullable PlayerEntity player, List list) {

        //Per item detailed info
        String translationKey = getTranslationKey(stack) + ".info.detailed";
        String translation = LanguageUtility.getLocal(translationKey);

        if (!translation.isEmpty() && !translation.equals(translationKey))
            list.addAll(LanguageUtility.splitByLine(translation));

    }

    /**
     * Does the item have detailed information to be shown
     *
     * @param stack
     * @param player
     * @return
     */
    protected boolean hasDetailedInfo(ItemStack stack, @Nullable PlayerEntity player) {
        String translationKey = getTranslationKey() + ".info";
        String translationKey2 = getTranslationKey(stack) + ".info";
        return !translationKey.equals(translationKey2);
    }

    /**
     * Does the item have detailed information to be shown when
     * shift is held
     *
     * @param stack
     * @param player
     * @return
     */
    protected boolean hasShiftInfo(ItemStack stack, @Nullable PlayerEntity player) {
        return false;
    }

}
