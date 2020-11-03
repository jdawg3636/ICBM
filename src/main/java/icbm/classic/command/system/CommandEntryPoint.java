package icbm.classic.command.system;

import com.mojang.brigadier.Command;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Entry point for all ICBM commands
 */
public class CommandEntryPoint extends Command<CommandSource> {

    public final String id;
    public final ICommandGroup commandGroup;

    public CommandEntryPoint(String id, ICommandGroup commandGroup) {
        this.id = id;
        this.commandGroup = commandGroup;
    }

    @Override
    public String getName() {
        return id;
    }

    @Override
    public String getUsage(CommandSource sender) {
        return "/" + id;
    }

    @Override
    public void execute(MinecraftServer server, CommandSource sender, String[] args) throws CommandException {
        commandGroup.handleCommand(server, sender, args);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, CommandSource sender, String[] args, @Nullable BlockPos targetPos) {
        return commandGroup.getTabSuggestions(server, sender, args, targetPos);
    }

}
