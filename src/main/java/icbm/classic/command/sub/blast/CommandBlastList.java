package icbm.classic.command.sub.blast;

import com.mojang.brigadier.context.CommandContext;
import icbm.classic.api.ICBMClassicAPI;
import icbm.classic.api.reg.IExplosiveData;
import icbm.classic.command.system.SubCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import java.util.stream.Collectors;

/**
 * Created by Robert Seifert on 1/6/20.
 */
public class CommandBlastList extends SubCommand {

    public CommandBlastList() {
        super("list");
    }

    @Override
    public int handleCommand(CommandContext<CommandSource> context) throws CommandException {

        //Convert list of explosives to string registry names
        String names = ICBMClassicAPI.EXPLOSIVE_REGISTRY.getExplosives().stream()
                .map(IExplosiveData::getRegistryName)
                .map(ResourceLocation::toString)
                .sorted()
                .collect(Collectors.joining(", "));

        //Output message TODO translate if possible?
        context.getSource().sendFeedback(new StringTextComponent("Explosive Types: " + names), true);

        return 0;

    }

}
