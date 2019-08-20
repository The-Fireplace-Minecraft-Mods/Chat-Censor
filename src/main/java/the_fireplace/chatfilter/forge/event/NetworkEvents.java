package the_fireplace.chatfilter.forge.event;

import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;
import the_fireplace.chatfilter.ChatCensor;
import the_fireplace.chatfilter.util.translation.TranslationUtil;

import java.util.Map;

@Mod.EventBusSubscriber(modid = ChatCensor.MODID)
public class NetworkEvents {

    @SubscribeEvent
    public static void clientConnectToServer(FMLNetworkEvent.ServerConnectionFromClientEvent event) {
        Map<String, String> clientMods = NetworkDispatcher.get(event.getManager()).getModList();
        if(event.getHandler() instanceof NetHandlerPlayServer && ((NetHandlerPlayServer) event.getHandler()).player != null && clientMods.containsKey("chatcensor"))
            TranslationUtil.chatCensorClients.add(((NetHandlerPlayServer) event.getHandler()).player.getUniqueID());
    }
}
