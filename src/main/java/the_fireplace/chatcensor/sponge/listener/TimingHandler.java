package the_fireplace.chatcensor.sponge.listener;

import the_fireplace.chatcensor.logic.TimerLogic;

import java.util.Timer;
import java.util.TimerTask;

public class TimingHandler {
    public Timer timer;
    public TimingHandler() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                TimerLogic.runFiveMinuteLogic();
            }
        }, 1000*60*5, 1000*60*5);
    }
}
