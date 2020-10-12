package icbm.classic.prefab.gui;

import net.minecraft.entity.player.PlayerEntity;

import java.util.Collection;

/**
 * Used to track players currently using an object. Primaryly used
 * for GUI handling.
 * Created by robert on 1/12/2015.
 */
public interface IPlayerUsing {

    Collection<PlayerEntity> getPlayersUsing();

    default boolean addPlayerToUseList(PlayerEntity player) {
        return getPlayersUsing().add(player);
    }

    default boolean removePlayerToUseList(PlayerEntity player) {
        return getPlayersUsing().remove(player);
    }

}
