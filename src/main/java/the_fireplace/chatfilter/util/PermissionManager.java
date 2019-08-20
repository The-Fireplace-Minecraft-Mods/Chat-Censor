package the_fireplace.chatfilter.util;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import the_fireplace.chatfilter.ChatCensor;

public final class PermissionManager {

    public static boolean hasPermission(EntityPlayerMP player, String permissionKey) {
        if(ChatCensor.getPermissionManager() != null)
            return ChatCensor.getPermissionManager().hasPermission(player, permissionKey);
        else
            return true;
    }

    public static boolean hasPermission(ICommandSender sender, String permissionKey) {
        if(sender instanceof EntityPlayerMP)
            return hasPermission((EntityPlayerMP)sender, permissionKey);
        return true;
    }

    public static boolean permissionManagementExists() {
        return ChatCensor.getPermissionManager().permissionManagementExists();
    }
}
