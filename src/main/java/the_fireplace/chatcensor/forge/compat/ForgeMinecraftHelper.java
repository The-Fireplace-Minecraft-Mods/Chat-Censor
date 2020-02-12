package the_fireplace.chatcensor.forge.compat;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.Logger;
import the_fireplace.chatcensor.ChatCensorForge;
import the_fireplace.chatcensor.abstraction.IMinecraftHelper;

public class ForgeMinecraftHelper implements IMinecraftHelper {
    @Override
    public MinecraftServer getServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    @Override
    public boolean isPluginLoaded(String id) {
        return ModList.get().isLoaded(id);
    }

    @Override
    public Logger getLogger() {
        return ChatCensorForge.getLogger();
    }
}
