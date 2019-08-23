package the_fireplace.chatcensor.logic;

import the_fireplace.chatcensor.data.PlayerDataManager;

import java.util.UUID;

public class PlayerEventLogic {
    public static void onPlayerLoggedIn(UUID playerId) {
        PlayerDataManager.setShouldDisposeReferences(playerId, false);
    }

    public static void onPlayerLoggedOut(UUID playerId) {
        PlayerDataManager.setShouldDisposeReferences(playerId, true);
    }
}
