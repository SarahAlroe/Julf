import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by silasa on 8/20/16.
 */
public class GameEventBroadcaster {

    private static GameEventBroadcaster instance = new GameEventBroadcaster();

    public static GameEventBroadcaster getInstance() {
        return instance;
    }
    ArrayList<GameEventListener> eventListeners;

    public GameEventBroadcaster(){
        eventListeners = new ArrayList<>();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (GameEventListener eventListener : eventListeners) {
                    eventListener.onGameTick();
                }
            }
        }, 16, 16);
    }

    public void addEventListener(GameEventListener listener){
        eventListeners.add(listener);
    }
}
