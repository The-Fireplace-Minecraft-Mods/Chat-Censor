package the_fireplace.chatcensor.logic;

import the_fireplace.chatcensor.data.PlayerDataManager;

public class TimerLogic {
    public static void runFiveMinuteLogic() {
        PlayerDataManager.save();
    }
}
