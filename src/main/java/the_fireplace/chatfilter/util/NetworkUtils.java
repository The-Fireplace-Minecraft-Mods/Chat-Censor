package the_fireplace.chatfilter.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import the_fireplace.chatfilter.ChatCensor;
import the_fireplace.chatfilter.data.PlayerDataManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class NetworkUtils {
    public static HashMap<String, String> censored = Maps.newHashMap();
    public static void initCensoredMap() {
        for(String uncensored: ChatCensor.getConfig().getStringsToCensor())
            censored.put(uncensored, getCensored(uncensored));
    }

    private static HashMap<Integer, ITextComponent> censoredComponents = Maps.newHashMap();
    @SuppressWarnings("unused")
    public static SPacketChat createModifiedChat(EntityPlayerMP chatTarget, SPacketChat original) {
        return createModifiedChat(chatTarget.getUniqueID(), original);
    }

    public static SPacketChat createModifiedChat(UUID chatTargetId, SPacketChat original) {
        if(PlayerDataManager.getIgnoresFilter(chatTargetId))
            return original;
        try {
            //Write the Packet to the PacketBuffer because the only other option to access the TextComponent on the server side would be an access transformer.
            PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
            original.writePacketData(buf);
            ITextComponent comp = buf.readTextComponent();
            int initHash = comp.getUnformattedComponentText().hashCode();
            //Use the message we have already created if possible.
            if(ChatCensor.getConfig().useCache() && censoredComponents.containsKey(initHash))
                comp = censoredComponents.get(initHash);
            else {
                comp = getCensoredTextComponent(comp);
                if(ChatCensor.getConfig().useCache() && comp.getUnformattedComponentText().hashCode() != initHash)
                    censoredComponents.put(initHash, comp);
            }
            return new SPacketChat(comp, original.getType());
        } catch(IOException e) {
            e.printStackTrace();
            return new SPacketChat();
        }
    }

    public static ITextComponent getCensoredTextComponent(ITextComponent comp) {
        List<ITextComponent> newSiblings = Lists.newArrayList();
        for(ITextComponent sibling: comp.getSiblings())
            newSiblings.add(getCensoredTextComponent(sibling));
        if (comp instanceof TextComponentString) {
            String messageString = comp.getUnformattedComponentText();
            for (String censor : ChatCensor.getConfig().getStringsToCensor())
                messageString = messageString.replaceAll("(?i)" + censor, censored.get(censor));
            messageString = matchCase(comp.getUnformattedComponentText(), messageString);
            comp = new TextComponentString(messageString).setStyle(comp.getStyle());
        } else if (comp instanceof TextComponentTranslation) {
            Object[] args = ((TextComponentTranslation) comp).getFormatArgs();
            for (int i = 0; i < args.length; i++) {
                ITextComponent outArg = getCensoredTextComponent(getFormatArgumentAsComponent(i, args, comp.getStyle()));
                args[i] = outArg;
            }
            comp = new TextComponentTranslation(((TextComponentTranslation) comp).getKey(), args).setStyle(comp.getStyle());
        }
        for(ITextComponent sibling: newSiblings)
            comp.appendSibling(sibling);
        return comp;
    }

    public static String getCensored(String censor) {
        String sub = censor.substring(1, censor.length()-1);;
        return censor.substring(0, 1) + sub.replaceAll(".", "*") + censor.substring(censor.length() - 1);
    }

    public static String matchCase(String correctCase, String formatted) {
        char[] out = formatted.toCharArray();
        for(int i=0;i<correctCase.length();i++)
            if(Character.isLetter(out[i]) && Character.isUpperCase(correctCase.charAt(i)))
                out[i] = Character.toUpperCase(out[i]);
        return String.copyValueOf(out);
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
            itextcomponent = new TextComponentString(object == null ? "null" : object.toString());
            itextcomponent.getStyle().setParentStyle(style);
        }

        return itextcomponent;
    }
}
