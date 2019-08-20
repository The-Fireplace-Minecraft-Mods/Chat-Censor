package the_fireplace.chatfilter.logic;

import the_fireplace.chatfilter.data.PlayerDataManager;

public class TimerLogic {
    public static void runFiveMinuteLogic() {
        PlayerDataManager.save();
    }
}
