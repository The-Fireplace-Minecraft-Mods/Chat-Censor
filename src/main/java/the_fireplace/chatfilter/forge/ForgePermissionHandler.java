package the_fireplace.chatfilter.forge;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import the_fireplace.chatfilter.abstraction.IPermissionHandler;

public class ForgePermissionHandler implements IPermissionHandler {

    public ForgePermissionHandler() {
        registerPermission("commands.chatfilter", DefaultPermissionLevel.ALL, "");
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
