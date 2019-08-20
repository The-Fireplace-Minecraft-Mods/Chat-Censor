package the_fireplace.chatcensor.sponge.compat;

import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Sponge;
import the_fireplace.chatcensor.ChatCensorSponge;
import the_fireplace.chatcensor.abstraction.IMinecraftHelper;

public class SpongeMinecraftHelper implements IMinecraftHelper {
    @Override
    public MinecraftServer getServer() {
        return (MinecraftServer)Sponge.getServer();//TODO test
    }

    @Override
    public boolean isPluginLoaded(String id) {
        return Sponge.getPluginManager().isLoaded(id);
    }

    @Override
    public Logger getLogger() {
        return ChatCensorSponge.logger;
    }
}
