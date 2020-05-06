package the_fireplace.chatcensor.forge.event;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import the_fireplace.chatcensor.ChatCensor;
import the_fireplace.chatcensor.util.CensorHelper;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = ChatCensor.MODID)
public class ClientEvents {
    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent event) {
        event.setMessage(CensorHelper.getCensoredTextComponent(event.getMessage()));
    }
}
