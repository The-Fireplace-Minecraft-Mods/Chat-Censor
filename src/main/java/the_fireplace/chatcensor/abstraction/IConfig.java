package the_fireplace.chatcensor.abstraction;

import java.util.List;

public interface IConfig {
    //General mod configuration
    String getLocale();
    List<String> getStringsToCensor();
    boolean useCache();
}
