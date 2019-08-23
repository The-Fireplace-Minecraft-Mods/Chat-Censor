package the_fireplace.chatcensor.sponge;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.api.Server;

public class SpongeConverter {
    public static MinecraftServer server(Server s) {
        return (MinecraftServer)s;
    }
}
