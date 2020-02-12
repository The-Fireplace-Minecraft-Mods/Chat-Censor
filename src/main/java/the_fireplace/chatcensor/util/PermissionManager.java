package the_fireplace.chatcensor.util;

import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import the_fireplace.chatcensor.ChatCensor;

public final class PermissionManager {

    public static boolean hasPermission(ServerPlayerEntity player, String permissionKey) {
        if(ChatCensor.getPermissionManager() != null)
            return ChatCensor.getPermissionManager().hasPermission(player, permissionKey);
        else
            return true;
    }

    public static boolean hasPermissionOrNoManager(ServerPlayerEntity player, String permissionKey) {
        if(ChatCensor.getPermissionManager() != null)
            return !ChatCensor.getPermissionManager().permissionManagementExists() || ChatCensor.getPermissionManager().hasPermission(player, permissionKey);
        else
            return true;
    }

    public static boolean hasPermission(CommandSource sender, String permissionKey) {
        return hasPermission(sender.getEntity() != null ? sender.getEntity() : sender.getServer(), permissionKey);
    }

    public static boolean hasPermission(ICommandSource sender, String permissionKey) {
        if(sender instanceof ServerPlayerEntity)
            return hasPermission((ServerPlayerEntity)sender, permissionKey);
        return true;
    }

    public static boolean permissionManagementExists() {
        return ChatCensor.getPermissionManager().permissionManagementExists();
    }
}
