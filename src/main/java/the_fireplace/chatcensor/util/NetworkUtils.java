package the_fireplace.chatcensor.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SChatPacket;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import the_fireplace.chatcensor.ChatCensor;
import the_fireplace.chatcensor.data.PlayerDataManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class NetworkUtils {
    private static HashMap<Integer, ITextComponent> censoredComponents = Maps.newHashMap();
    @SuppressWarnings("unused")
    public static SChatPacket createModifiedChat(ServerPlayerEntity chatTarget, SChatPacket original) {
        return createModifiedChat(chatTarget.getUniqueID(), original);
    }

    public static SChatPacket createModifiedChat(UUID chatTargetId, SChatPacket original) {
        if(PlayerDataManager.getIgnoresCensor(chatTargetId))
            return original;
        try {
            //Write the Packet to the PacketBuffer because the only other option to access the TextComponent on the server side would be an access transformer.
            PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
            original.writePacketData(buf);
            ITextComponent comp = buf.readTextComponent();
            //getUnformattedComponentText
            int initHash = comp.getUnformattedComponentText().hashCode();
            //Use the message we have already created if possible.
            if(ChatCensor.getConfig().useCache() && censoredComponents.containsKey(initHash))
                comp = censoredComponents.get(initHash);
            else {
                comp = getCensoredTextComponent(comp);
                //getUnformattedComponentText
                if(ChatCensor.getConfig().useCache() && comp.getUnformattedComponentText().hashCode() != initHash)
                    censoredComponents.put(initHash, comp);
            }
            return new SChatPacket(comp, original.getType());
        } catch(IOException e) {
            e.printStackTrace();
            return new SChatPacket();
        }
    }

    public static ITextComponent getCensoredTextComponent(ITextComponent comp) {
        List<ITextComponent> newSiblings = Lists.newArrayList();
        for(ITextComponent sibling: comp.getSiblings())
            newSiblings.add(getCensoredTextComponent(sibling));
        if (comp instanceof StringTextComponent) {
            //getUnformattedComponentText
            String messageString = comp.getUnformattedComponentText();
            for (String censor : ChatCensor.getConfig().getStringsToCensor())
                messageString = messageString.replaceAll("(?i)" + censor, CensorHelper.censored.get(censor));
            //getUnformattedComponentText
            messageString = CensorHelper.matchCase(comp.getUnformattedComponentText(), messageString);
            comp = new StringTextComponent(messageString).setStyle(comp.getStyle());
        } else if (comp instanceof TranslationTextComponent) {
            Object[] args = ((TranslationTextComponent) comp).getFormatArgs();
            for (int i = 0; i < args.length; i++) {
                ITextComponent outArg = getCensoredTextComponent(getFormatArgumentAsComponent(i, args, comp.getStyle()));
                args[i] = outArg;
            }
            comp = new TranslationTextComponent(((TranslationTextComponent) comp).getKey(), args).setStyle(comp.getStyle());
        }
        for(ITextComponent sibling: newSiblings)
            comp.appendSibling(sibling);
        return comp;
    }

    public static ITextComponent getFormatArgumentAsComponent(int index, Object[] args, Style style)
    {
        Object object = args[index];
        ITextComponent itextcomponent;

        if (object instanceof ITextComponent)
        {
            itextcomponent = (ITextComponent)object;
        }
        else
        {
            itextcomponent = new StringTextComponent(object == null ? "null" : object.toString());
            itextcomponent.getStyle().setParentStyle(style);
        }

        return itextcomponent;
    }
}
