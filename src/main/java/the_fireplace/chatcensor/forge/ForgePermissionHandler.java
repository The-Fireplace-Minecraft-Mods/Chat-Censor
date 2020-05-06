package the_fireplace.chatcensor.forge;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import the_fireplace.chatcensor.abstraction.IPermissionHandler;

public class ForgePermissionHandler implements IPermissionHandler {

    public ForgePermissionHandler() {
        registerPermission("command.togglecensor", DefaultPermissionLevel.ALL, "");
        registerPermission("command.mute", DefaultPermissionLevel.ALL, "");
        registerPermission("command.censorplayer", DefaultPermissionLevel.OP, "");
    }

    @Override
    public boolean hasPermission(EntityPlayerMP player, String permissionName) {
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
