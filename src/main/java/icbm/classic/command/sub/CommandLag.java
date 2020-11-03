package icbm.classic.command.sub;

import com.mojang.brigadier.context.CommandContext;
import icbm.classic.command.CommandUtils;
import icbm.classic.command.system.SubCommand;
import icbm.classic.lib.explosive.ExplosiveHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/13/2018.
 */
public class CommandLag extends SubCommand {

    public static final String TRANSLATION_LAG_REMOVE = "command.icbmclassic:icbm.lag.remove";
    private final Predicate<Entity> icbmEntitySelector = (entity) -> entity.isAlive() && CommandUtils.isICBMEntity(entity);

    public CommandLag() {
        super("lag");
    }

    @Override
    protected void collectHelpForAll(Consumer<String> consumer) {}

    @Override
    protected void collectHelpWorldOnly(Consumer<String> consumer) {
        consumer.accept("[radius]");
    }

    @Override
    public int handleCommand(CommandContext<CommandSource> context) {

        //Parse range
        double range = context.getRange() context.length > 1 ? Double.parseDouble(args[1]) : 1000;

        //Remove ICBM entities
        final List<Entity> entities = CommandUtils.getEntities(
                context.getSource().getWorld(),
                context.getSource().getPos().x,
                context.getSource().getPos().y,
                context.getSource().getPos().z,
                range,
                icbmEntitySelector
        );
        entities.forEach(Entity::remove);

        //Remove blasts queue to run or currently running
        final int blastRemoveCount = ExplosiveHandler.removeNear(
                context.getSource().getWorld(),
                context.getSource().getPos().x,
                context.getSource().getPos().y,
                context.getSource().getPos().z,
                range
        );

        //Update user with data
        context.getSource().sendFeedback(new TranslationTextComponent(TRANSLATION_LAG_REMOVE, blastRemoveCount, entities.size(), range), true);

    }

}
