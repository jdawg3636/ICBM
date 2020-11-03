package icbm.classic.command.system;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Robert Seifert on 1/6/20.
 */
public interface ISubCommand {

    /**
     * Called to run the command
     *
     * @param context - composite param containing calling entity/source, arguments, etc.
     * @throws CommandException - failed to run the command
     * @return exit code (0 = successful)
     */
    int handleCommand(CommandContext<CommandSource> context) throws CommandException;

    /**
     * Sends all help text to the sender
     *
     * @param sender
     */
    default void displayHelp(CommandSource sender) {
        sender.sendFeedback(new StringTextComponent(getUsage(sender)), true);
    }

    /**
     * Gets a list of suggestions for completing the current command
     *
     * @param server    - server running the command
     * @param sender    - user triggering the command
     * @param args      - arguments for the command
     * @param targetPos - block position for the command
     * @return empty list or list containing suggestions
     */
    List<String> getTabSuggestions(@Nonnull MinecraftServer server, @Nonnull CommandSource sender, @Nonnull String[] args, @Nullable BlockPos targetPos);


    /**
     * Name of the command
     *
     * @return string name
     */
    String getName();

    /**
     * Usage of the command
     *
     * @param sender - sender of the command
     * @return usage string (/parent + name)
     */
    String getUsage(CommandSource sender);

    /**
     * Sets the parent of the command
     *
     * @param parent - command's parent
     */
    void setParent(ICommandGroup parent);

}
