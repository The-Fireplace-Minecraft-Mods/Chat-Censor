package the_fireplace.chatcensor.util;

import com.google.common.collect.Maps;
import the_fireplace.chatcensor.ChatCensor;

import java.util.Map;

public class CensorHelper {
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

    public static String getCensoredWord(String censor) {
        String sub = censor.substring(1, censor.length()-1);;
        //noinspection ReplaceAllDot
        return censor.substring(0, 1) + sub.replaceAll(".", "*") + censor.substring(censor.length() - 1);
    }

    public static String matchCase(String correctCase, String formatted) {
        char[] out = formatted.toCharArray();
        for(int i=0;i<correctCase.length();i++)
            if(Character.isLetter(out[i]) && Character.isUpperCase(correctCase.charAt(i)))
                out[i] = Character.toUpperCase(out[i]);
        return String.copyValueOf(out);
    }
}
