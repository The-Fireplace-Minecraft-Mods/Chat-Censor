package the_fireplace.chatcensor.forge.event;

import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import the_fireplace.chatcensor.ChatCensor;
import the_fireplace.chatcensor.logic.PlayerEventLogic;
import the_fireplace.chatcensor.util.NetworkUtils;

@Mod.EventBusSubscriber(modid = ChatCensor.MODID)
public class PlayerEvents {
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEventLogic.onPlayerLoggedIn(event.player.getUniqueID());
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        PlayerEventLogic.onPlayerLoggedOut(event.player.getUniqueID());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onServerChat(ServerChatEvent event) {
        NetworkUtils.messageSenders.put(event.getComponent().getUnformattedText().hashCode(), event.getPlayer().getUniqueID());
    }
}
