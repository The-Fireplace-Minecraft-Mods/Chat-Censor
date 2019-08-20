package the_fireplace.chatfilter.commands;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import the_fireplace.chatfilter.data.PlayerDataManager;
import the_fireplace.chatfilter.util.PermissionManager;
import the_fireplace.chatfilter.util.translation.TranslationUtil;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CommandToggleCensor extends CommandBase {
    @Override
    public String getName() {
        return "togglecensor";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return TranslationUtil.getRawTranslationString(sender, "commands.togglecensor.usage");
    }

    @Override
    public void execute(@Nullable MinecraftServer server, ICommandSender sender, String[] args) {
        boolean newIgnoresFilter = !PlayerDataManager.getIgnoresFilter(((EntityPlayerMP)sender).getUniqueID());
        PlayerDataManager.setIgnoresFilter(((EntityPlayerMP)sender).getUniqueID(), newIgnoresFilter);
        sender.sendMessage(TranslationUtil.getTranslation(((EntityPlayerMP) sender).getUniqueID(), "commands.togglecensor."+(newIgnoresFilter ? "unfiltered" : "filtered")));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender instanceof EntityPlayerMP && (!PermissionManager.permissionManagementExists() || PermissionManager.hasPermission((EntityPlayerMP) sender, "commands.togglecensor"));
    }
}
