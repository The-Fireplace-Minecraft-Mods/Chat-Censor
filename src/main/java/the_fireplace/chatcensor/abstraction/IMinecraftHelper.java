package the_fireplace.chatcensor.abstraction;

import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;

public interface IMinecraftHelper {
    MinecraftServer getServer();
    boolean isPluginLoaded(String id);
    Logger getLogger();
}
