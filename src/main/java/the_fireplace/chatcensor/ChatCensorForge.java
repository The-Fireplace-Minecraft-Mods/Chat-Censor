package the_fireplace.chatcensor;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import the_fireplace.chatcensor.abstraction.IConfig;
import the_fireplace.chatcensor.forge.ForgePermissionHandler;
import the_fireplace.chatcensor.forge.compat.ForgeMinecraftHelper;
import the_fireplace.chatcensor.forge.event.NetworkEvents;
import the_fireplace.chatcensor.logic.ServerEventLogic;
import the_fireplace.chatcensor.sponge.SpongePermissionHandler;

import java.util.List;

import static the_fireplace.chatcensor.ChatCensor.MODID;

@Mod.EventBusSubscriber(modid = MODID)
@Mod(MODID)
public final class ChatCensorForge {
    public static ChatCensorForge instance;

    private static Logger LOGGER = LogManager.getLogger(MODID);
    private boolean validJar = true;

    public static Logger getLogger() {
        return LOGGER;
    }

    public ChatCensorForge() {
        instance = this;
        ChatCensor.setMinecraftHelper(new ForgeMinecraftHelper());
        ChatCensor.setConfig(new Config());
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
        NetworkEvents.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverConfig);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::postInit);
        if(!validJar)
            ChatCensor.getMinecraftHelper().getLogger().error("The jar's signature is invalid! Please redownload from https://www.curseforge.com/minecraft/mc-mods/chat-censor");
    }

    public void serverConfig(ModConfig.ModConfigEvent event) {
        if (event.getConfig().getType() == ModConfig.Type.COMMON) {
            //Load the config
            Config.load();
        }
    }

    public void postInit(FMLLoadCompleteEvent event){
        if(ChatCensor.getMinecraftHelper().isPluginLoaded("spongeapi"))
            ChatCensor.setPermissionManager(new SpongePermissionHandler());
        else
            ChatCensor.setPermissionManager(new ForgePermissionHandler());
    }

    @SubscribeEvent
    public void onServerStart(FMLServerStartingEvent event) {
        ServerEventLogic.onServerStarting(event.getServer());
    }

    @SubscribeEvent
    public void onServerStop(FMLServerStoppingEvent event) {
        ServerEventLogic.onServerStopping();
    }

    @SubscribeEvent
    public void invalidFingerprint(FMLFingerprintViolationEvent e) {
        if(!e.isDirectory()) {
            validJar = false;
        }
    }

    private static class Config implements IConfig {
        public static final CommonConfig COMMON;
        public static final ForgeConfigSpec COMMON_SPEC;
        static {
            final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
            COMMON_SPEC = specPair.getRight();
            COMMON = specPair.getLeft();
        }

        public static String locale;
        public static List<String> stringsToCensor;
        public static boolean useCache;

        @Override
        public String getLocale() {
            return locale;
        }

        @Override
        public List<String> getStringsToCensor() {
            return Lists.newArrayList(stringsToCensor);
        }

        @Override
        public boolean useCache() {
            return useCache;
        }

        public static void load() {
            locale = COMMON.locale.get();
            stringsToCensor = COMMON.stringsToCensor.get();
            useCache = COMMON.useCache.get();
        }

        public static class CommonConfig {
            public ForgeConfigSpec.ConfigValue<String> locale;
            public ForgeConfigSpec.ConfigValue<List<String>> stringsToCensor;
            public ForgeConfigSpec.BooleanValue useCache;

            CommonConfig(ForgeConfigSpec.Builder builder) {
                builder.push("general");
                locale = builder
                        .comment("Server locale - the client's locale takes precedence if Chat Censor is installed there.")
                        .translation("Locale")
                        .define("locale", "en_us", o -> o instanceof String && ((String) o).matches("[a-z][a-z]_[a-z][a-z]"));
                stringsToCensor = builder
                        .comment("The strings that should be censored for players that have censoring enabled.")
                        .translation("Strings to Censor")
                        .define("stringsToCensor", Lists.newArrayList("fuck", "shit", "bitch", "nigger", "dick", "crap", "cunt", "pussy", "niglet", "beaner", "wetback", "spic", "nazi", "faggot", "fag", "towelhead"));
                useCache = builder
                        .comment("Using this will save CPU at the cost of increased memory usage as players chat.")
                        .translation("Use Chat Cache")
                        .define("useCache", true);
                builder.pop();
            }
        }
    }
}
