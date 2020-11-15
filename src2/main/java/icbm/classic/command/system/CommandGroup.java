package icbm.classic.command.system;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import icbm.classic.command.CommandUtils;
import icbm.classic.command.sub.CommandHelp;
import icbm.classic.lib.MapWithDefault;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Robert Seifert on 1/6/20.
 */
public class CommandGroup extends SubCommand implements ICommandGroup {

    private final MapWithDefault<String, ISubCommand> subCommandMap = new MapWithDefault();

    public CommandGroup(String id) {

        super(id);

        //Help command
        final SubCommand helpCommand = new CommandHelp();
        registerCommand(helpCommand);
        subCommandMap.setDefaultValue(helpCommand);

    }

    @Override
    public Collection<ISubCommand> getSubCommands() {
        return subCommandMap.values();
    }

    @Override
    public void registerCommand(ISubCommand command) {
        command.setParent(this);
        subCommandMap.put(command.getName().toLowerCase(), command);
    }

    @Override
    public int handleCommand(CommandContext<CommandSource> context) throws CommandException {
        final String subCommand = args.length == 0 ? "help" : args[0].toLowerCase();
        subCommandMap.get(subCommand).handleCommand(context);
        return 0;
    }

    @Override
    public List<String> getTabSuggestions(@Nonnull MinecraftServer server, @Nonnull CommandSource sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {

        if (args.length == 1) {
            return Command.getListOfStringsMatchingLastWord(args, subCommandMap.keySet());
        }
        else if (args.length >= 2) {
            final String subCommand = args[0].toLowerCase();
            return subCommandMap.get(subCommand).getTabSuggestions(server, sender, CommandUtils.removeFront(args), targetPos);
        }

        return Collections.<String>emptyList();

    }

}
