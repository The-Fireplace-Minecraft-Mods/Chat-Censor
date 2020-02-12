package the_fireplace.chatcensor;

import the_fireplace.chatcensor.abstraction.IConfig;
import the_fireplace.chatcensor.abstraction.IMinecraftHelper;
import the_fireplace.chatcensor.abstraction.IPermissionHandler;

public final class ChatCensor {
    public static final String MODID = "chatcensor";
    private static IMinecraftHelper minecraftHelper;
    private static IConfig config;
    private static IPermissionHandler permissionManager;

    public static IMinecraftHelper getMinecraftHelper() {
        return minecraftHelper;
    }

    public static IConfig getConfig() {
        return config;
    }

    static void setMinecraftHelper(IMinecraftHelper minecraftHelper) {
        ChatCensor.minecraftHelper = minecraftHelper;
    }

    static void setConfig(IConfig config) {
        ChatCensor.config = config;
    }

    public static IPermissionHandler getPermissionManager() {
        return permissionManager;
    }

    static void setPermissionManager(IPermissionHandler permissionManager) {
        ChatCensor.permissionManager = permissionManager;
    }
}
