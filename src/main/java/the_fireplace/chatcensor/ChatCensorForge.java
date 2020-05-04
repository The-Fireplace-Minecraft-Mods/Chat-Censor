package the_fireplace.chatcensor;

import com.google.common.collect.Lists;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;
import the_fireplace.chatcensor.abstraction.IConfig;
import the_fireplace.chatcensor.forge.ForgePermissionHandler;
import the_fireplace.chatcensor.forge.compat.ForgeMinecraftHelper;
import the_fireplace.chatcensor.logic.ServerEventLogic;
import the_fireplace.chatcensor.sponge.SpongePermissionHandler;

import java.util.List;
import java.util.Objects;

import static the_fireplace.chatcensor.ChatCensor.MODID;

@Mod.EventBusSubscriber(modid = MODID)
@Mod(modid = MODID, name = ChatCensor.MODNAME, version = ChatCensor.VERSION, acceptedMinecraftVersions = "[1.12,1.13)", acceptableRemoteVersions = "*", dependencies="after:spongeapi", certificateFingerprint = "51ac068a87f356c56dc733d0c049a9a68bc7245c")
public final class ChatCensorForge {
    @Mod.Instance(MODID)
    public static ChatCensorForge instance;

    private static Logger LOGGER = FMLLog.log;
    private boolean validJar = true;

    public static Logger getLogger() {
        return LOGGER;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ChatCensor.setMinecraftHelper(new ForgeMinecraftHelper());
        ChatCensor.setConfig(new cfg());
        LOGGER = event.getModLog();
        if(!validJar)
            ChatCensor.getMinecraftHelper().getLogger().error("The jar's signature is invalid! Please redownload from "+Objects.requireNonNull(Loader.instance().activeModContainer()).getUpdateUrl());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        if(ChatCensor.getMinecraftHelper().isPluginLoaded("spongeapi"))
            ChatCensor.setPermissionManager(new SpongePermissionHandler());
        else
            ChatCensor.setPermissionManager(new ForgePermissionHandler());
    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        ServerEventLogic.onServerStarting(event.getServer());
    }

    @Mod.EventHandler
    public void onServerStop(FMLServerStoppingEvent event) {
        ServerEventLogic.onServerStopping();
    }

    @Mod.EventHandler
    public void invalidFingerprint(FMLFingerprintViolationEvent e) {
        if(!e.isDirectory()) {
            validJar = false;
        }
    }

    @SuppressWarnings("WeakerAccess")
    @Config(modid = MODID)
    private static class cfg implements IConfig {
        //General mod configuration
        @Config.Comment("Server locale - the client's locale takes precedence if Chat Censor is installed there.")
        public static String locale = "en_us";
        @Config.Comment("The strings that should be censored for players that have censoring enabled.")
        public static String[] stingsToCensor = {"fuck", "shit", "bitch", "nigger", "dick", "crap", "cunt", "pussy", "niglet", "beaner", "wetback", "spic", "nazi", "faggot", "fag", "towelhead", "kike", "cock"};
        @Config.Comment("Using this will save CPU at the cost of slightly increased memory usage.")
        public static boolean useCache = true;
        @Config.Comment("Using this will censor the full word and not just everything between the first and last letters. On: ******, off: f****t.")
        public static boolean fullWordCensor = true;

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

        @Override
        public boolean censorFullWord() {
            return fullWordCensor;
        }
    }
}
