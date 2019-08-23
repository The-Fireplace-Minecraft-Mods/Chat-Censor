package the_fireplace.chatcensor.sponge.listener;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import the_fireplace.chatcensor.logic.PlayerEventLogic;

public class PlayerListeners {
    @Listener
    public void onLogin(ClientConnectionEvent.Join e) {
        PlayerEventLogic.onPlayerLoggedIn(e.getTargetEntity().getUniqueId());
    }

    @Listener
    public void onLogout(ClientConnectionEvent.Disconnect e) {
        PlayerEventLogic.onPlayerLoggedOut(e.getTargetEntity().getUniqueId());
    }
}
