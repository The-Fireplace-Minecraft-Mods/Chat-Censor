package the_fireplace.chatfilter.logic;

import net.minecraft.entity.player.EntityPlayer;
import the_fireplace.chatfilter.data.PlayerDataManager;

import java.util.UUID;

public class PlayerEventLogic {
    public static void onPlayerLoggedIn(EntityPlayer player) {
        PlayerDataManager.setShouldDisposeReferences(player.getUniqueID(), false);
    }

    public static void onPlayerLoggedOut(UUID playerId) {
        PlayerDataManager.setShouldDisposeReferences(playerId, true);
    }
}
