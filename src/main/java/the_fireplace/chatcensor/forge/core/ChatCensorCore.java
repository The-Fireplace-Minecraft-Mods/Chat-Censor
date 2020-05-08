package the_fireplace.chatcensor.forge.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(1000)
public final class ChatCensorCore implements IFMLLoadingPlugin {

    private static boolean wasInitialized;

    public static boolean wasInitialized(){
        return wasInitialized;
    }

    @Override
    public String[] getASMTransformerClass(){
        return new String[]{/* TransformerSendPacket.class.getName() */};
    }

    @Override
    public String getModContainerClass(){
        return null;
    }

    @Override
    public String getSetupClass(){
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data){
        wasInitialized = true;
    }

    @Override
    public String getAccessTransformerClass(){
        return null;
    }
}
