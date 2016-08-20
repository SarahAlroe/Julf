import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.TimerTask;
import java.util.Timer;

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

    private BufferedImage loadImage(String name) {
        String imgFileName = "images/" + name + ".png";
        URL url = Julf.class.getResource(imgFileName);
        BufferedImage img = null;
        try {
            img = ImageIO.read(url);
        } catch (Exception e) {
        }
        return img;
    }

    public void initComponents() {
        BufferedImage mapFile = loadImage("map");
        conf = new Config();
        wm = new WorldMap(this, mapFile);
        player = new Player(6.0, 6.0);
        setLayout(new BorderLayout());
        JPanel p = new JPanel();
        p.setBackground(Color.black);
        add("North", p);
        painter = new WorldPainter(this);
        p.add("Center", painter);
        Timer timer = new Timer();
        timer.schedule(new UpdateTask(this), 16, 16);
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        if (e.getID()==KeyEvent.KEY_PRESSED){
                            System.out.println(e.getKeyCode());
                            switch (e.getKeyCode()){
                                case KEYCODE_W:
                                case KEYCODE_ARROW_UP:
                                    player.moveForward();
                                    break;
                                case KEYCODE_S:
                                case KEYCODE_ARROW_DOWN:
                                    player.moveBackwards();;
                                    break;
                                case KEYCODE_A:
                                case KEYCODE_ARROW_LEFT:
                                    player.turnLeft();
                                    break;
                                case KEYCODE_D:
                                case KECODE_ARROW_RIGHT:
                                    player.turnRight();
                                    break;
                                case KEYCODE_Q:
                                    player.strafeLeft();
                                    break;
                                case KEYCODE_E:
                                    player.strafeRight();
                                    break;
                            }
                        }
                        return false;
                    }
                });
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


