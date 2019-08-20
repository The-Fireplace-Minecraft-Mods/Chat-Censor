package the_fireplace.chatfilter.forge.event;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import the_fireplace.chatfilter.ChatCensor;
import the_fireplace.chatfilter.logic.PlayerEventLogic;

@Mod.EventBusSubscriber(modid = ChatCensor.MODID)
public class PlayerEvents {
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEventLogic.onPlayerLoggedIn(event.player);
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        PlayerEventLogic.onPlayerLoggedOut(event.player.getUniqueID());
    }
}
