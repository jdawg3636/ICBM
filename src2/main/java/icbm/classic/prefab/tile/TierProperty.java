package icbm.classic.prefab.tile;

import com.google.common.collect.Lists;
import icbm.classic.api.EnumTier;
import net.minecraft.state.EnumProperty;

/**
 *
 * Created by Dark(DarkGuardsman, Robert) on 1/31/2018.
 */
public final class TierProperty extends EnumProperty<EnumTier> {

    public TierProperty() {
        super("tier", EnumTier.class, Lists.newArrayList(EnumTier.ONE, EnumTier.TWO, EnumTier.THREE));
    }

}
