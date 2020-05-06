package the_fireplace.chatcensor.commands;

import com.mojang.authlib.GameProfile;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.server.MinecraftServer;
import the_fireplace.chatcensor.data.PlayerDataManager;
import the_fireplace.chatcensor.util.PermissionManager;
import the_fireplace.chatcensor.util.translation.TranslationUtil;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CommandCensorPlayer extends CommandBase {
    @Override
    public String getName() {
        return "censorplayer";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return TranslationUtil.getRawTranslationString(sender, "commands.censorplayer.usage");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        GameProfile target = null;
        if(args.length > 0)
            try {
                target = server.getPlayerProfileCache().getGameProfileForUsername(args[0]);
            } catch(Exception ignored) {}
        if(target != null) {
            boolean newIsCensored = !PlayerDataManager.isCensored(target.getId());
            PlayerDataManager.setCensored(target.getId(), newIsCensored);
            sender.sendMessage(TranslationUtil.getTranslation(sender, "commands.censorplayer." + (newIsCensored ? "censored" : "uncensored"), target.getName()));
        } else
            throw new PlayerNotFoundException("commands.generic.player.notFound", args.length > 0 ? args[0] : "");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return !PermissionManager.permissionManagementExists() && sender.canUseCommand(getRequiredPermissionLevel(), getName()) || PermissionManager.hasPermission(sender, "command.censorplayer");
    }
}
