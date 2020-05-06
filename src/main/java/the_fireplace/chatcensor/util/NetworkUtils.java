package the_fireplace.chatcensor.util;

import com.google.common.collect.Maps;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import the_fireplace.chatcensor.data.PlayerDataManager;
import the_fireplace.chatcensor.util.translation.TranslationUtil;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class NetworkUtils {
    public static final Map<String, UUID> messageSenders = Maps.newHashMap();

    @SuppressWarnings("unused")
    public static SPacketChat createModifiedChat(EntityPlayerMP chatTarget, SPacketChat original) {
        return createModifiedChat(chatTarget.getUniqueID(), original);
    }

    public static SPacketChat createModifiedChat(UUID chatTargetId, SPacketChat original) {
        UUID senderId = messageSenders.get(original.getChatComponent().toString());
        if(senderId != null && PlayerDataManager.hasMuted(chatTargetId, senderId))
            return getMutedMessage(chatTargetId, original.getType());
        if(PlayerDataManager.getIgnoresCensor(chatTargetId))
            return original;
        if(senderId != null && PlayerDataManager.isCensored(senderId))
            return getMutedMessage(chatTargetId, original.getType());
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

    @Nullable
    private static SPacketChat getMutedMessage(UUID chatTargetId, ChatType type) {
        if(PlayerDataManager.getReceivedCensoredMessage(chatTargetId))
            return null;
        else {
            PlayerDataManager.setReceivedCensoredMessage(chatTargetId, true);
            return new SPacketChat(TranslationUtil.getTranslation(chatTargetId, "chatcensor.censored_message").setStyle(new Style().setColor(TextFormatting.GRAY)), type);
        }
    }
}
