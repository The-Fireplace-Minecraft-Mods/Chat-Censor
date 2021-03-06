package the_fireplace.chatcensor.forge.compat;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.Logger;
import the_fireplace.chatcensor.ChatCensorForge;
import the_fireplace.chatcensor.abstraction.IMinecraftHelper;

public class ForgeMinecraftHelper implements IMinecraftHelper {
    @Override
    public MinecraftServer getServer() {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }

    @Override
    public boolean isPluginLoaded(String id) {
        return Loader.isModLoaded(id);
    }

    @Override
    public Logger getLogger() {
        return ChatCensorForge.getLogger();
    }
}
