package the_fireplace.chatcensor.sponge.listener;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class NetworkListeners {
    @Listener
    public void onLogin(ClientConnectionEvent.Login e) {
        //TODO see if it is possible to read what mods the client has, if any
    }
}
