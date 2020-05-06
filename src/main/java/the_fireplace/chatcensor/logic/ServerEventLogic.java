package the_fireplace.chatcensor.logic;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import the_fireplace.chatcensor.commands.CommandCensorPlayer;
import the_fireplace.chatcensor.commands.CommandMute;
import the_fireplace.chatcensor.commands.CommandToggleCensor;
import the_fireplace.chatcensor.data.PlayerDataManager;
import the_fireplace.chatcensor.util.CensorHelper;

public class ServerEventLogic {
    public static void onServerStarting(MinecraftServer server) {
        ICommandManager command = server.getCommandManager();
        ServerCommandManager manager = (ServerCommandManager) command;
        manager.registerCommand(new CommandToggleCensor());
        manager.registerCommand(new CommandMute());
        manager.registerCommand(new CommandCensorPlayer());
        CensorHelper.initCensoredMap();
    }

    public static void onServerStopping() {
        PlayerDataManager.save();
    }
}
