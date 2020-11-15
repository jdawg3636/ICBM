package icbm.classic.content.potion;

import icbm.classic.lib.transform.vector.Pos;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;

public class ContagiousPoison extends Poison {

    private boolean isContagious;

    public ContagiousPoison(String name, int id, boolean isContagious) {
        super(name);
        this.isContagious = isContagious;
    }

    @Override
    protected void doPoisonEntity(Pos emitPosition, LivingEntity entity, int amplifier) {

        if (this.isContagious) {
            entity.addPotionEffect(new CustomPotionEffect(PoisonContagion.INSTANCE, 45 * 20, amplifier, null));
            entity.addPotionEffect(new CustomPotionEffect(Effects.BLINDNESS, 15 * 20, amplifier));
        }
        else {
            entity.addPotionEffect(new CustomPotionEffect(PoisonToxin.INSTANCE, 30 * 20, amplifier, null));
            entity.addPotionEffect(new CustomPotionEffect(Effects.NAUSEA, 30 * 20, amplifier));
        }

        entity.addPotionEffect(new CustomPotionEffect(Effects.HUNGER, 30 * 20, amplifier));
        entity.addPotionEffect(new CustomPotionEffect(Effects.WEAKNESS, 35 * 20, amplifier));
        entity.addPotionEffect(new CustomPotionEffect(Effects.MINING_FATIGUE, 60 * 20, amplifier));

    }

}
