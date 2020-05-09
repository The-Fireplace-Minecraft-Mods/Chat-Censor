package the_fireplace.chatcensor.commands;

import com.mojang.authlib.GameProfile;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import the_fireplace.chatcensor.data.PlayerDataManager;
import the_fireplace.chatcensor.util.PermissionManager;
import the_fireplace.chatcensor.util.translation.TranslationUtil;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CommandMute extends CommandBase {
    @Override
    public String getName() {
        return "mute";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return TranslationUtil.getRawTranslationString(sender, "commands.mute.usage");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(sender instanceof EntityPlayerMP) {
            GameProfile target = null;
            if (args.length > 0)
                try {
                    target = server.getPlayerProfileCache().getGameProfileForUsername(args[0]);
                } catch (Exception ignored) {
                }
            if (target != null) {
                boolean muted = PlayerDataManager.toggleMute(((EntityPlayerMP) sender).getUniqueID(), target.getId());
                sender.sendMessage(TranslationUtil.getTranslation(((EntityPlayerMP) sender).getUniqueID(), "commands.mute." + (muted ? "censored" : "uncensored"), target.getName()));
            } else
                throw new PlayerNotFoundException("commands.generic.player.notFound", args.length > 0 ? args[0] : "");
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender instanceof EntityPlayerMP && (!PermissionManager.permissionManagementExists() && sender.canUseCommand(getRequiredPermissionLevel(), getName()) || PermissionManager.hasPermission(sender, "command.mute"));
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1)
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        return Collections.emptyList();
    }
}
