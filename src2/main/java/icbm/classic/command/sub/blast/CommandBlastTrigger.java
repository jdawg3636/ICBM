package icbm.classic.command.sub.blast;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import icbm.classic.ICBMClassic;
import icbm.classic.api.ICBMClassicHelpers;
import icbm.classic.api.explosion.BlastState;
import icbm.classic.api.reg.IExplosiveData;
import icbm.classic.command.CommandUtils;
import icbm.classic.command.ICBMCommands;
import icbm.classic.command.system.SubCommand;
import icbm.classic.lib.explosive.ExplosiveHandler;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * Created by Robert Seifert on 1/6/20.
 */
public class CommandBlastTrigger extends SubCommand {

    //Translations
    private static final String TRANSLATION_KEY = "command.icbmclassic:icbm.blast";
    public static final String TRANSLATION_TRIGGERED = TRANSLATION_KEY + ".triggered";
    public static final String TRANSLATION_THREADING = TRANSLATION_KEY + ".threading";

    //Translations: Errors
    public static final String TRANSLATION_ERROR = TRANSLATION_KEY + ".error";
    public static final String TRANSLATION_ERROR_BLOCKED = TRANSLATION_ERROR + ".blocked";
    public static final String TRANSLATION_ERROR_NULL = TRANSLATION_ERROR + ".null";
    public static final String TRANSLATION_ERROR_TRIGGERED = TRANSLATION_ERROR + ".triggered";
    public static final String TRANSLATION_ERROR_UNKNOWN = TRANSLATION_ERROR + ".unknown";

    public static final String TRANSLATION_ERROR_SCALE_ZERO = TRANSLATION_ERROR + ".scale.zero";
    public static final String TRANSLATION_ERROR_EXPLOSIVE_ID = TRANSLATION_ERROR + ".explosive.id";

    public CommandBlastTrigger() {
        super("trigger");
    }

    @Override
    protected void collectHelpForAll(Consumer<String> consumer) {
        consumer.accept("<id> <dim> <x> <y> <z> <scale>");
    }

    @Override
    protected void collectHelpWorldOnly(Consumer<String> consumer) {
        consumer.accept("<id> <scale>");
    }

    @Override
    public void handleCommand(CommandContext<CommandSource> context) throws CommandException {
        if (args.length <= 0 || !doCommand(context.getSource().getServer(), context.getSource(), args))
            throw new WrongUsageException(ICBMCommands.TRANSLATION_UNKNOWN_COMMAND, getUsage(context.getSource()));
    }

    private boolean doCommand(@Nonnull MinecraftServer server, @Nonnull CommandSource sender, @Nonnull String[] args) throws CommandSyntaxException {

        //Get explosive from user
        final IExplosiveData explosiveData = getExplosive(args[0]);

        if (args.length == 6) {
            longVersion(sender, explosiveData, args);
            return true;
        }
        else if (!(sender instanceof MinecraftServer) && args.length == 2) {
            shortVersion(sender, explosiveData, args);
            return true;
        }

        return false;

    }

    private void shortVersion(CommandSource sender, IExplosiveData explosiveData, String[] args) throws CommandSyntaxException {

        final float scale = Float.parseFloat(args[1]);
        if (scale <= 0)
            throw new CommandSyntaxException(new SimpleCommandExceptionType(() -> TRANSLATION_ERROR_SCALE_ZERO), () -> TRANSLATION_ERROR_SCALE_ZERO);

        //Get position data
        final World world = sender.getEntity().getEntityWorld();
        final double x = sender.getPos().x;
        final double y = sender.getPos().y;
        final double z = sender.getPos().z;

        //Trigger blast
        trigger(sender, world, x, y, z, explosiveData, scale);

    }

    private void longVersion(CommandSource sender, IExplosiveData explosiveData, String[] args) throws SyntaxErrorException {

        final float scale = Float.parseFloat(args[5]);
        if (scale <= 0)
            throw new SyntaxErrorException(TRANSLATION_ERROR_SCALE_ZERO);

        //Get position data
        final World world = CommandUtils.getWorld(sender, args[1], sender.getEntityWorld());
        final double x = CommandUtils.getNumber(sender, args[2], sender.getPositionVector().x);
        final double y = CommandUtils.getNumber(sender, args[3], sender.getPositionVector().y);
        final double z = CommandUtils.getNumber(sender, args[4], sender.getPositionVector().z);

        //Trigger blast
        trigger(sender, world, x, y, z, explosiveData, scale);

    }

    /**
     * Gets the explosive data based on the string ID from the user
     *
     * @param explosive_id - ID from user
     * @return explosive data
     * @throws CommandSyntaxException - if ID is not found
     */
    public static IExplosiveData getExplosive(String explosive_id) throws CommandSyntaxException {
        final IExplosiveData explosiveData = ICBMClassicHelpers.getExplosive(explosive_id, true);
        if (explosiveData == null)
            throw new CommandSyntaxException(new Dynamic2CommandExceptionType(TRANSLATION_ERROR_EXPLOSIVE_ID, explosive_id), );
        return explosiveData;
    }

    /**
     * Triggers the explosive at the location
     *
     * @param sender        - user running the command
     * @param world         - position data
     * @param x             - position data
     * @param y             - position data
     * @param z             - position data
     * @param explosiveData - explosive to run
     * @param scale         - scale to apply, keep this small as its scale and not size (size defaults to 25 * scale of 2 = 50 size)
     */
    private void trigger(CommandSource sender, World world, double x, double y, double z, IExplosiveData explosiveData, float scale) {

        final BlastState result = ExplosiveHandler.createExplosion(null,
                world, x, y, z,
                explosiveData.getRegistryID(), scale,
                null);

        //Send translated message to user
        sender.sendFeedback(
                new TranslationTextComponent(
                        getTranslationKey(result),
                        explosiveData.getRegistryName(), scale,
                        world.provider.getDimension(), world.getWorldType().getName(),
                        x, y, z
                ),
                true
        );

    }

    //Used to select translation for output message
    public static String getTranslationKey(BlastState result) {
        switch (result) {
            case TRIGGERED:
                return TRANSLATION_TRIGGERED;
            case THREADING:
                return TRANSLATION_THREADING;
            case FORGE_EVENT_CANCEL:
                return TRANSLATION_ERROR_BLOCKED;
            case NULL:
                return TRANSLATION_ERROR_NULL;
            case ERROR:
                return TRANSLATION_ERROR;
            case ALREADY_TRIGGERED:
                return TRANSLATION_ERROR_TRIGGERED;
            default:
                ICBMClassic.logger().error("CommandBlastTrigger: unknown blast status code " + result);
                return TRANSLATION_ERROR_UNKNOWN;
        }
    }

}
