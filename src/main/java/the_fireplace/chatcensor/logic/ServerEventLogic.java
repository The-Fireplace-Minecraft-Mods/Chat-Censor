package the_fireplace.chatcensor.logic;

import net.minecraft.server.MinecraftServer;
import the_fireplace.chatcensor.commands.ChatCensorCommands;
import the_fireplace.chatcensor.data.PlayerDataManager;
import the_fireplace.chatcensor.util.CensorHelper;

public class ServerEventLogic {
    public static void onServerStarting(MinecraftServer server) {
        ChatCensorCommands.register(server.getCommandManager().getDispatcher());
        CensorHelper.initCensoredMap();
    }

    public static void onServerStopping() {
        PlayerDataManager.save();
    }
}
