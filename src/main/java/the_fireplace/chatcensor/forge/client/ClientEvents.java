package the_fireplace.chatcensor.forge.client;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import the_fireplace.chatcensor.ChatCensor;
import the_fireplace.chatcensor.util.NetworkUtils;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = ChatCensor.MODID)
public class ClientEvents {
    @SubscribeEvent
    public static void configChanged(ConfigChangedEvent event) {
        if (event.getModID().equals(ChatCensor.MODID)) {
            ConfigManager.sync(ChatCensor.MODID, Config.Type.INSTANCE);
            NetworkUtils.initCensoredMap();
        }
    }
}
