package the_fireplace.chatcensor.forge.event;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_fireplace.chatcensor.ChatCensor;
import the_fireplace.chatcensor.logic.TimerLogic;

@Mod.EventBusSubscriber(modid = ChatCensor.MODID)
public class Timer {
    private static int fiveMinuteCounter = 0;
    private static boolean executing = false;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if(!executing) {
            if(++fiveMinuteCounter >= 20*60*5) {
                executing = true;
                fiveMinuteCounter -= 20*60*5;
                TimerLogic.runFiveMinuteLogic();
                executing = false;
            }
        }
    }
}
