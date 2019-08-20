package the_fireplace.chatfilter.abstraction;

import java.util.List;

public interface IConfig {
    //General mod configuration
    String getLocale();
    List<String> getStringsToCensor();
    boolean useCache();
}
