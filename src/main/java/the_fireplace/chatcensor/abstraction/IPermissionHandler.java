package the_fireplace.chatcensor.abstraction;

import net.minecraft.entity.player.ServerPlayerEntity;

public interface IPermissionHandler {
    boolean hasPermission(ServerPlayerEntity player, String permissionName);
    void registerPermission(String permissionName, Object permissionLevel, String permissionDescription);
    boolean permissionManagementExists();
}
