package icbm.classic.prefab.item;

import icbm.classic.ICBMConstants;
import icbm.classic.lib.IOUtil;
import icbm.classic.lib.LanguageUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;

/**
 * Generic prefab to use in all items providing common implementation
 * <p>
 * <p>
 * Created by Dark(DarkGuardsman, Robert) on 12/20/2016.
 */
public class ItemBase extends Item
{

    public ItemBase(Item.Properties properties) {
        super(properties);
    }

    public ItemBase setName(String name)
    {
        this.setRegistryName(ICBMConstants.PREFIX + name);
        return this;
    }

    //Make sure to mirror all changes to other abstract class
    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        PlayerEntity player = Minecraft.getInstance().player;

        //Generic info, shared by item group
        splitAdd(getTranslationKey() + ".info", tooltip, false, true);

        if (hasDetailedInfo(stack, player))
        {
            getDetailedInfo(stack, player, tooltip);
        }

        if (hasShiftInfo(stack, player))
        {
            if (IOUtil.isKeyPressed(GLFW_KEY_LEFT_SHIFT))
            {
                tooltip.add(new StringTextComponent(LanguageUtility.getLocal("info.voltzengine:tooltip.noShift").replace("#0", "\u00a7b").replace("#1", "\u00a77")));
            }
            else
            {
                getShiftDetailedInfo(stack, player, tooltip);
            }
        }
    }

    /**
     * Gets the detailed information for the item shown after the
     * global generic item details.
     *
     * @param stack
     * @param player
     * @param tooltip
     */
    protected void getDetailedInfo(ItemStack stack, PlayerEntity player, List<ITextComponent> tooltip)
    {
        //Per item detailed info
        splitAdd(getTranslationKey(stack) + ".info", tooltip, true, true);
    }

    /**
     * Gets the detailed when shift is held information for the item shown after the
     * global generic item details.
     * <p>
     * This is in addition to normal details
     *
     * @param stack
     * @param player
     * @param tooltip
     */
    protected void getShiftDetailedInfo(ItemStack stack, PlayerEntity player, List<ITextComponent> tooltip)
    {
        //Per item detailed info
        splitAdd(getTranslationKey(stack) + ".info.detailed", tooltip, true, true);
    }

    protected void splitAdd(String translationKey, List<ITextComponent> tooltip, boolean addKeyIfEmpty, boolean translate)
    {
        String translation = translate ? LanguageUtility.getLocal(translationKey) : translationKey;
        if (!translate || !translation.isEmpty() && !translation.equals(translationKey))
        {
            LanguageUtility.splitByLine(translation).forEach(line -> tooltip.add(new StringTextComponent(line)));
        }
    }

    /**
     * Does the item have detailed information to be shown
     *
     * @param stack
     * @param player
     * @return
     */
    protected boolean hasDetailedInfo(ItemStack stack, PlayerEntity player)
    {
        return false;
    }

    /**
     * Does the item have detailed information to be shown when
     * shift is held
     *
     * @param stack
     * @param player
     * @return
     */
    protected boolean hasShiftInfo(ItemStack stack, PlayerEntity player)
    {
        return false;
    }
}
