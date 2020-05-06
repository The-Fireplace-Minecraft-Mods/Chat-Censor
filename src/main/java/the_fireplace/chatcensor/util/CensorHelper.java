package the_fireplace.chatcensor.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import the_fireplace.chatcensor.ChatCensor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CensorHelper {
    private static final HashMap<Integer, ITextComponent> censoredComponents = Maps.newHashMap();
    public static Map<String, String> censored = Maps.newHashMap();

    public static String censor(String in) {
        final String inCp = in;
        for (String censor : ChatCensor.getConfig().getStringsToCensor())
            in = in.replaceAll("(?i)" + censor, CensorHelper.censored.get(censor));
        in = CensorHelper.matchCase(inCp, in);
        return in;
    }

    public static void initCensoredMap() {
        for(String uncensored: ChatCensor.getConfig().getStringsToCensor())
            censored.put(uncensored, getCensoredWord(uncensored));
    }

    @SuppressWarnings("SuspiciousRegexArgument")
    public static String getCensoredWord(String censor) {
        if(ChatCensor.getConfig().censorFullWord()) {
            return censor.replaceAll(".", "*");
        } else {
            String sub = censor.substring(1, censor.length() - 1);
            return censor.substring(0, 1) + sub.replaceAll(".", "*") + censor.substring(censor.length() - 1);
        }
    }

    public static String matchCase(String correctCase, String formatted) {
        char[] out = formatted.toCharArray();
        for(int i=0;i<correctCase.length();i++)
            if(Character.isLetter(out[i]) && Character.isUpperCase(correctCase.charAt(i)))
                out[i] = Character.toUpperCase(out[i]);
        return String.copyValueOf(out);
    }

    public static ITextComponent getCensoredTextComponent(ITextComponent comp) {
        int initHash = comp.getUnformattedComponentText().hashCode();
        //Use the message we have already created if possible.
        if(ChatCensor.getConfig().useCache() && censoredComponents.containsKey(initHash))
            comp = censoredComponents.get(initHash);
        else {
            comp = censorTextComponent(comp);
            //getUnformattedComponentText
            if(ChatCensor.getConfig().useCache() && comp.getUnformattedComponentText().hashCode() != initHash)
                censoredComponents.put(initHash, comp);
        }
        return comp;
    }

    private static ITextComponent censorTextComponent(ITextComponent comp) {
        List<ITextComponent> newSiblings = Lists.newArrayList();
        for(ITextComponent sibling: comp.getSiblings())
            newSiblings.add(censorTextComponent(sibling));
        if (comp instanceof TextComponentString) {
            comp = new TextComponentString(censor(comp.getUnformattedComponentText())).setStyle(comp.getStyle());
        } else if (comp instanceof TextComponentTranslation) {
            Object[] args = ((TextComponentTranslation) comp).getFormatArgs();
            for (int i = 0; i < args.length; i++) {
                ITextComponent outArg = censorTextComponent(getFormatArgumentAsComponent(i, args, comp.getStyle()));
                args[i] = outArg;
            }
            comp = new TextComponentTranslation(((TextComponentTranslation) comp).getKey(), args).setStyle(comp.getStyle());
        }
        for(ITextComponent sibling: newSiblings)
            comp.appendSibling(sibling);
        return comp;
    }

    private static ITextComponent getFormatArgumentAsComponent(int index, Object[] args, Style style) {
        Object object = args[index];
        ITextComponent itextcomponent;

        if (object instanceof ITextComponent) {
            itextcomponent = (ITextComponent)object;
        } else {
            itextcomponent = new TextComponentString(object == null ? "null" : object.toString());
            itextcomponent.getStyle().setParentStyle(style);
        }

        return itextcomponent;
    }
}
