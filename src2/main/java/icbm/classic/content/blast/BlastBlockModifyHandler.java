package icbm.classic.content.blast;

import icbm.classic.ICBMConstants;
import icbm.classic.api.events.BlastBlockModifyEvent;
import net.minecraft.block.Blocks;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

/**
 * Handles an incoming BlastBlockModifyEvent.
 */
@Mod.EventBusSubscriber(modid = ICBMConstants.DOMAIN)
public class BlastBlockModifyHandler {

    @SubscribeEvent
    public static void onBlastBlockModify(BlastBlockModifyEvent event) {

        switch(event.getModificationType()) {
            case SET_TO_AIR:
                event.getWorld().setBlockState(event.getPosition(), Blocks.AIR.getDefaultState());
                break;
            case SET_STATE:
                event.getWorld().setBlockState(event.getPosition(), Objects.requireNonNull(event.getNewState()));
                break;
            case SET_STATE_WITH_FLAGS:
                event.getWorld().setBlockState(event.getPosition(), Objects.requireNonNull(event.getNewState()), event.getFlags());
                break;
            case USE_CALLBACK:
                Objects.requireNonNull(event.getCallback()).run();
                break;
        }

    }

}
