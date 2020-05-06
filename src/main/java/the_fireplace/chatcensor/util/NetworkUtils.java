package the_fireplace.chatcensor.util;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ITextComponent;
import the_fireplace.chatcensor.data.PlayerDataManager;

import java.io.IOException;
import java.util.UUID;

public class NetworkUtils {
    @SuppressWarnings("unused")
    public static SPacketChat createModifiedChat(EntityPlayerMP chatTarget, SPacketChat original) {
        return createModifiedChat(chatTarget.getUniqueID(), original);
    }

    public static SPacketChat createModifiedChat(UUID chatTargetId, SPacketChat original) {
        if(PlayerDataManager.getIgnoresCensor(chatTargetId))
            return original;
        try {
            //Write the Packet to the PacketBuffer because the only other option to access the TextComponent on the server side would be an access transformer.
            PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
            original.writePacketData(buf);
            ITextComponent comp = buf.readTextComponent();
            comp = CensorHelper.getCensoredTextComponent(comp);
            return new SPacketChat(comp, original.getType());
        } catch(IOException e) {
            e.printStackTrace();
            return new SPacketChat();
        }
    }
}
