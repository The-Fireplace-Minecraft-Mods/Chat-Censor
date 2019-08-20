package the_fireplace.chatcensor;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import the_fireplace.chatcensor.sponge.SpongePermissionHandler;
import the_fireplace.chatcensor.sponge.compat.SpongeMinecraftHelper;

//@Plugin(id = Clans.MODID+"sponge", name = Clans.MODNAME, version = Clans.VERSION, description = "A server-side land protection and PVP system.", url = "https://www.curseforge.com/minecraft/mc-mods/clans", authors = {"The_Fireplace"})
public final class ChatCensorSponge {

    @Inject
    public static Logger logger;

    @Listener()
    public void init(GameInitializationEvent event) {
        if(ChatCensor.getMinecraftHelper() == null) {
            ChatCensor.setMinecraftHelper(new SpongeMinecraftHelper());
            ChatCensor.setPermissionManager(new SpongePermissionHandler());
        }
    }
}
