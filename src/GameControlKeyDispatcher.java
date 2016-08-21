import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by silasa on 8/21/16.
 */
public class GameControlKeyDispatcher implements KeyEventDispatcher {
    public static final int KEYCODE_ARROW_UP = 38;
    public static final int KEYCODE_ARROW_DOWN = 40;
    public static final int KEYCODE_ARROW_LEFT = 37;
    public static final int KECODE_ARROW_RIGHT = 39;
    public static final int KEYCODE_W = 87;
    public static final int KEYCODE_S = 83;
    public static final int KEYCODE_A = 65;
    public static final int KEYCODE_D = 68;
    public static final int KEYCODE_Q = 81;
    public static final int KEYCODE_E = 69;
    private Player player;

    public GameControlKeyDispatcher(Player player) {
        this.player = player;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_TYPED) {
            return false;
        }
        boolean isPressed = (e.getID() == KeyEvent.KEY_PRESSED);
        switch (e.getKeyCode()) {
            case KEYCODE_W:
            case KEYCODE_ARROW_UP:
                player.toggleMovement(Direction.MOVE_FORWARD, isPressed);
                break;
            case KEYCODE_S:
            case KEYCODE_ARROW_DOWN:
                player.toggleMovement(Direction.MOVE_BACKWARDS, isPressed);
                break;
            case KEYCODE_A:
            case KEYCODE_ARROW_LEFT:
                player.toggleMovement(Direction.TURN_LEFT, isPressed);
                break;
            case KEYCODE_D:
            case KECODE_ARROW_RIGHT:
                player.toggleMovement(Direction.TURN_RIGHT, isPressed);
                break;
            case KEYCODE_Q:
                player.toggleMovement(Direction.MOVE_LEFT, isPressed);
                break;
            case KEYCODE_E:
                player.toggleMovement(Direction.MOVE_RIGHT, isPressed);
                break;
        }
        return false;
    }
}
