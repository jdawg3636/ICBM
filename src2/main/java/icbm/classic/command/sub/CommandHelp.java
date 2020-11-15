package icbm.classic.command.sub;

import com.mojang.brigadier.context.CommandContext;
import icbm.classic.command.system.SubCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;

/**
 * Created by Robert Seifert on 1/6/20.
 */
public class CommandHelp extends SubCommand {

    public CommandHelp() {
        super("help");
    }

    @Override
    public int handleCommand(CommandContext<CommandSource> context) throws CommandException {
        parent.getSubCommands().forEach(command -> command.displayHelp(context.getSource()));
        return 0;
    }

}
