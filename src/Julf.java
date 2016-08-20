import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class Julf extends JApplet {

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
    public WorldMap wm;
    public Player player;
    public Config conf;
    WorldPainter painter;
    private GameEventBroadcaster gameEventBroadcaster;

    public static void main(String[] args) {
        JFrame f = new JFrame("Julf");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JApplet ap = new Julf();
        ap.init();
        ap.start();
        f.add("Center", ap);
        f.pack();
        f.setVisible(true);
    }

    public void start() {
        initComponents();
    }

    public void initComponents() {
        initGameElements();

        setupPanel();

        startRepaintTimer();

        startKeyListener();
    }

    private void initGameElements() {
        BufferedImage mapFile = loadImage("map2");
        conf = Config.getInstance();
        wm = new WorldMap(this, mapFile);
        player = new Player(6.0, 6.0);
        player.setMap(wm);
        gameEventBroadcaster = GameEventBroadcaster.getInstance();
    }

    private void setupPanel() {
        setLayout(new BorderLayout());
//        JPanel p = new JPanel();
//        p.setBackground(Color.black);
//        add("Center", p);
        setBackground(Color.black);
        painter = new WorldPainter(this);
        painter.setBackground(Color.black);
        add("Center", painter);
//        p.add("Center", painter);
    }

    private void startRepaintTimer() {
        Timer timer = new Timer();
        timer.schedule(new UpdateTask(this), 16, 16);
    }

    private void startKeyListener() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        if (e.getID() == KeyEvent.KEY_TYPED){
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
                });
    }

    public BufferedImage loadImage(String name) {
        String imgFileName = "images/" + name + ".png";
        URL url = Julf.class.getResource(imgFileName);
        BufferedImage img = null;
        try {
            img = ImageIO.read(url);
        } catch (Exception e) {
        }
        return img;
    }

}

class UpdateTask extends TimerTask {
    private Julf main;

    public UpdateTask(Julf parent) {
        main = parent;
    }

    public void run() {
        main.painter.repaint();
    }
}


