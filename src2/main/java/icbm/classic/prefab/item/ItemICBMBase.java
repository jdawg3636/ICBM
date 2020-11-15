package icbm.classic.prefab.item;

import icbm.classic.ICBMClassic;
import net.minecraft.item.Item;

/**
 * Prefab for ICBM items that sets the creative tab, texture name, and translation name
 *
 * @author DarkGuardsman
 */
@Deprecated
public class ItemICBMBase extends ItemBase {

    public ItemICBMBase(String name) {
        this(new Item.Properties(), name);
    }

    public ItemICBMBase(Item.Properties properties, String name) {
        super(properties.group(ICBMClassic.CREATIVE_TAB));
        super.setName(name);
    }

}
