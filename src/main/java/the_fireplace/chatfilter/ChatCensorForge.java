package the_fireplace.chatfilter;

import com.google.common.collect.Lists;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import the_fireplace.chatfilter.abstraction.IConfig;
import the_fireplace.chatfilter.forge.ForgePermissionHandler;
import the_fireplace.chatfilter.forge.compat.ForgeMinecraftHelper;
import the_fireplace.chatfilter.sponge.SpongePermissionHandler;
import the_fireplace.chatfilter.util.NetworkUtils;

import java.util.List;

import static the_fireplace.chatfilter.ChatCensor.MODID;

@Mod.EventBusSubscriber(modid = MODID)
@Mod(modid = MODID, name = ChatCensor.MODNAME, version = ChatCensor.VERSION, acceptedMinecraftVersions = "[1.12,1.13)", acceptableRemoteVersions = "*", dependencies="after:spongeapi")
public final class ChatCensorForge {
    @Mod.Instance(MODID)
    public static ChatCensorForge instance;

    private static Logger LOGGER = FMLLog.log;

    public static Logger getLogger() {
        return LOGGER;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ChatCensor.setMinecraftHelper(new ForgeMinecraftHelper());
        ChatCensor.setConfig(new cfg());
        LOGGER = event.getModLog();
        NetworkUtils.initCensoredMap();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        if(ChatCensor.getMinecraftHelper().isPluginLoaded("spongeapi"))
            ChatCensor.setPermissionManager(new SpongePermissionHandler());
        else
            ChatCensor.setPermissionManager(new ForgePermissionHandler());
    }

    @SuppressWarnings("WeakerAccess")
    @Config(modid = MODID)
    private static class cfg implements IConfig {
        //General mod configuration
        @Config.Comment("Server locale - the client's locale takes precedence if Clans is installed there.")
        public static String locale = "en_us";
        @Config.Comment("The strings that should be censored for players that have censoring enabled")
        public static String[] stingsToCensor = {"fuck", "shit", "bitch", "nigger", "dick", "crap", "cunt", "pussy", "niglet", "beaner", "wetback", "spic", "nazi", "faggot", "fag", "towelhead"};
        @Config.Comment("Using this will save CPU at the cost of slightly increased memory usage.")
        public static boolean useCache = true;

        @Override
        public String getLocale() {
            return locale;
        }

        @Override
        public List<String> getStringsToCensor() {
            return Lists.newArrayList(stingsToCensor);
        }

        @Override
        public boolean useCache() {
            return useCache;
        }
    }
}
