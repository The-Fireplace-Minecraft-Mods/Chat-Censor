package the_fireplace.chatfilter.logic;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import the_fireplace.chatfilter.commands.CommandToggleCensor;
import the_fireplace.chatfilter.data.PlayerDataManager;

public class ServerEventLogic {
    public static void onServerStarting(MinecraftServer server) {
        ICommandManager command = server.getCommandManager();
        ServerCommandManager manager = (ServerCommandManager) command;
        manager.registerCommand(new CommandToggleCensor());
    }

    public static void onServerStopping() {
        PlayerDataManager.save();
    }
}
