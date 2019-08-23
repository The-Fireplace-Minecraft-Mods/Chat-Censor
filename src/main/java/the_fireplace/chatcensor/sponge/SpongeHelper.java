package the_fireplace.chatcensor.sponge;

import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.api.entity.living.player.Player;

public class SpongeHelper {
    public static EntityPlayer player(Player p) {
        return (EntityPlayer) p;
    }
}
