package the_fireplace.chatcensor;

import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.util.TypeTokens;
import the_fireplace.chatcensor.abstraction.IConfig;
import the_fireplace.chatcensor.sponge.SpongePermissionHandler;
import the_fireplace.chatcensor.sponge.compat.SpongeMinecraftHelper;
import the_fireplace.chatcensor.sponge.listener.NetworkListeners;
import the_fireplace.chatcensor.sponge.listener.PlayerListeners;
import the_fireplace.chatcensor.sponge.listener.TimingHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Plugin(id = ChatCensor.MODID+"sponge", name = ChatCensor.MODNAME, version = ChatCensor.VERSION, description = "A plugin to censor chat and allow users to decide if they want to see censored chat.", url = "", authors = {"The_Fireplace"}, dependencies = {@Dependency(id=ChatCensor.MODID, optional=true)})
public final class ChatCensorSponge {
    @Inject
    public static Logger logger;

    public static boolean active;

    @Listener
    public void preInit(GamePreInitializationEvent event) {
        if(ChatCensor.getMinecraftHelper() == null) {
            active = true;
            ChatCensor.setMinecraftHelper(new SpongeMinecraftHelper());
            ChatCensor.setPermissionManager(new SpongePermissionHandler());
        } else
            System.out.println(System.getenv().keySet());
    }

    @Listener
    public void init(GameInitializationEvent event) {
        if(active) {
            Sponge.getGame().getEventManager().registerListeners(this, new NetworkListeners());
            Sponge.getGame().getEventManager().registerListeners(this, new PlayerListeners());
            new TimingHandler();
        }
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        if(active) {
            try {
                loadConfig();
                ChatCensor.setConfig(new cfg());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> loader;

    private ConfigurationNode group;

    private void loadConfig() throws IOException {
        boolean needsSaving = false;
        CommentedConfigurationNode root = loader.load(ConfigurationOptions.defaults());
        if (root.isVirtual()) {
            ConfigurationLoader<CommentedConfigurationNode> defaults =
                    HoconConfigurationLoader.builder()
                            .setURL(Sponge.getAssetManager()
                                    .getAsset(this, "default.conf").get()
                                    .getUrl())
                            .build();
            root.mergeValuesFrom(defaults.load(ConfigurationOptions.defaults()));
            needsSaving = true;
        }

        group = root.getNode("general");
        if (group.isVirtual()) {
            ConfigurationLoader<CommentedConfigurationNode> defaults =
                    HoconConfigurationLoader.builder()
                            .setURL(Sponge.getAssetManager()
                                    .getAsset(this, "default.conf").get()
                                    .getUrl())
                            .build();
            CommentedConfigurationNode defgroup = defaults.load(ConfigurationOptions.defaults()).getNode("general");
            group.mergeValuesFrom(defgroup);
            //set the value (i assume merging values does not clear the virtual flag)
            root.getNode("general").setValue(group);
            needsSaving = true;
        }

        if (needsSaving)
            loader.save(root);
    }

    public class cfg implements IConfig {
        @Override
        public String getLocale() {
            return group.getNode("locale").getString("en_us");
        }

        @Override
        public List<String> getStringsToCensor() {
            try {
                return group.getNode("stringsToCensor").getList(TypeTokens.STRING_TOKEN);
            } catch(ObjectMappingException e) {
                e.printStackTrace();
                return Collections.emptyList();
            }
        }

        @Override
        public boolean useCache() {
            return group.getNode("useCache").getBoolean(true);
        }
    }
}
