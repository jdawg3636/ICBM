package icbm.classic.content.potion;

import icbm.classic.ICBMConstants;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public abstract class CustomPotion extends Potion {

    /**
     * Creates a new type of potion
     *
     * @param isBadEffect - Is this potion a good potion or a bad one?
     * @param color       - The color of this potion.
     * @param name        - The name of this potion.
     */
    public CustomPotion(boolean isBadEffect, int color, int id, String name) {
        super(isBadEffect, color);
        this.setRegistryName("potion." + name);
        Registry.POTION.register(Registry.POTION, new ResourceLocation(ICBMConstants.PREFIX + name), this);
    }

}
