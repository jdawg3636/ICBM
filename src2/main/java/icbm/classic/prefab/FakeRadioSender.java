package icbm.classic.prefab;

import icbm.classic.api.tile.IRadioWaveReceiver;
import icbm.classic.api.tile.IRadioWaveSender;
import icbm.classic.ICBMClassic;
import icbm.classic.lib.transform.region.Cube;
import icbm.classic.lib.radio.RadioRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Wrapper used by items to act as a radio wave sender
 *
 *
 * Created by Dark(DarkGuardsman, Robert) on 4/24/2016.
 */
public class FakeRadioSender implements IRadioWaveSender {

    public final PlayerEntity player;
    public final ItemStack item;
    Cube cube;

    public FakeRadioSender(PlayerEntity player, ItemStack item, int range) {
        this.player = player;
        this.item = item;
        this.cube = new Cube(-range, Math.max(-range, 0), -range, range, range, range).add(player.getPosX(), player.getPosY(), player.getPosZ());
    }

    @Override
    public void onMessageReceived(IRadioWaveReceiver receiver, float hz, String header, Object[] data) {
        if (ICBMClassic.runningAsDev) {
            //player.addChatComponentMessage(new TextComponentString("Received message with header " + header + " on band " + hz + "hz with data " + data));
        }
    }

    @Override
    public void sendRadioMessage(float hz, String header, Object... data) {
        RadioRegistry.popMessage(world(), this, hz, header, data);
    }

    @Override
    public Cube getRadioSenderRange() {
        return cube;
    }

    @Override
    public double x() {
        return player.getPosX();
    }

    @Override
    public double y() {
        return player.getPosY();
    }

    @Override
    public double z() {
        return player.getPosZ();
    }

    @Override
    public World world() {
        return player.world;
    }

}
