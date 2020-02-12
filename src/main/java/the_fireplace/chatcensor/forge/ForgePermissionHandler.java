package the_fireplace.chatcensor.forge;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import the_fireplace.chatcensor.abstraction.IPermissionHandler;

public class ForgePermissionHandler implements IPermissionHandler {

    public ForgePermissionHandler() {
        registerPermission("command.togglecensor", DefaultPermissionLevel.ALL, "");
    }

    @Override
    public boolean hasPermission(ServerPlayerEntity player, String permissionName) {
        return PermissionAPI.hasPermission(player, permissionName);
    }

    @Override
    public void registerPermission(String permissionName, Object permissionLevel, String permissionDescription) {
        PermissionAPI.registerNode(permissionName, (DefaultPermissionLevel)permissionLevel, permissionDescription);
    }

    @Override
    public boolean permissionManagementExists() {
        return true;
    }
}
