package the_fireplace.chatcensor.forge.event;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_fireplace.chatcensor.ChatCensor;
import the_fireplace.chatcensor.logic.PlayerEventLogic;

@Mod.EventBusSubscriber(modid = ChatCensor.MODID)
public class PlayerEvents {
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEventLogic.onPlayerLoggedIn(event.getPlayer().getUniqueID());
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        PlayerEventLogic.onPlayerLoggedOut(event.getPlayer().getUniqueID());
    }
}
