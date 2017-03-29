import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by silasa on 8/20/16.
 */
class WorldPainter extends Component {
    public static final double WALL_HEIGHT = 5d;
    private final WorldMap worldMap;
    private final Config config;
    private final Player player;
    private TextureHandler textureHandler;
    private Dimension screenSize;
    private BufferedImage collideTexture;

    public WorldPainter(Julf main) {
        config = Config.getInstance();
        player = main.getPlayer();
        worldMap = main.getWorldMap();
        screenSize = getPreferredSize();
        textureHandler = TextureHandler.getInstance();
    }

    public Dimension getPreferredSize() {
        return new Dimension(640, 480);
    }

    public void paint(Graphics g) {
        screenSize = getSize();
        paintBackground(g, screenSize);

        paintRays(g);
    }

    private void paintBackground(Graphics g, Dimension screenSize) {
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, screenSize.width, screenSize.height / 2 + player.getVerticalOrientation());
        g.setColor(Color.gray);
        g.fillRect(0, screenSize.height / 2 + player.getVerticalOrientation(), screenSize.width, screenSize.height);
    }

    private void paintRays(Graphics g) {
        Point2D.Double playerPos = new Point2D.Double(player.getPos()[0], player.getPos()[1]);
        int fov = config.getFov();
        double fovR = Math.toRadians(fov);
        double playerOrient = player.getOrientation() - Math.toRadians(fov) / 2;
        double lineAngle = fovR / (double) screenSize.width;

        double[] lineDepths = new double[screenSize.width];

        for (int line = 0; line < screenSize.width; line++) {
            int lineHeight;
            boolean hasCollided = false;
            int collideColor;
            double maxLength = config.getMaxLength();

            double cAngle = playerOrient + lineAngle * (double) line;
            double incLength = config.getIncLength();
            Vector2D lineVector = new Vector2D(Math.sin(cAngle) * incLength, Math.cos(cAngle) * incLength);

            double lineLength = 0;
            Point2D.Double currentRayPosition = new Point2D.Double(playerPos.getX(), playerPos.getY());

            Point previousPosition = new Point((int) currentRayPosition.getX(), (int) currentRayPosition.getX());

            while (lineLength < maxLength) {
                lineLength += incLength;
                addVectorToPoint(currentRayPosition, lineVector);

                Point flooredPosition = new Point((int) currentRayPosition.getX(), (int) currentRayPosition.getY());
                if (flooredPosition.equals(previousPosition)) {
                    continue;
                }
                previousPosition.setLocation(flooredPosition);
                hasCollided = worldMap.hasTile(flooredPosition.x, flooredPosition.y);
                if (hasCollided) {
                    collideColor = worldMap.getTile(flooredPosition.x, flooredPosition.y);
                    collideTexture = textureHandler.getTexture(collideColor);
                    lineDepths[line] = lineLength;
                    break;
                }
            }
            if (hasCollided) {
                double verticalAngle = Math.atan(WALL_HEIGHT / (2d * lineLength));
                lineHeight = (int) (screenSize.height / Math.tan(fovR) * Math.tan(verticalAngle));
                int roofLineHeight = (screenSize.height - lineHeight) / 2;
                int textureColumn = (int) Math.round((currentRayPosition.x + currentRayPosition.y) * collideTexture.getWidth()) % collideTexture.getWidth();
                g.drawImage(collideTexture, line, roofLineHeight + player.getVerticalOrientation(), line + 1, screenSize.height - roofLineHeight + player.getVerticalOrientation(), textureColumn, 0, textureColumn + 1, collideTexture.getHeight(), this);
            } else {
                lineDepths[line] = maxLength;
                drawFog(g, line);
            }
        }
        //Draw world objects
        ArrayList<WorldObject> worldObjects = WorldObjectHandler.getInstance().getWorldObjects();
        for (int i = 0; i < worldObjects.size(); i++) {
            WorldObject worldObject = worldObjects.get(i);
            if (worldObject.isVisibleFrom(playerPos, playerOrient, fovR)) {
            BufferedImage worldObjectTexture = TextureHandler.getInstance().getTexture(worldObject.getTypeID());
            double objectDistance = worldObject.getDistanceFrom(playerPos);
            double verticalAngle = Math.atan(WALL_HEIGHT / (2d * objectDistance));
            int lineHeight = (int) (screenSize.height / Math.tan(fovR) * Math.tan(verticalAngle));
            int roofLineHeight = (screenSize.height - lineHeight) / 2;
            int verticalLineCount = (int) ((double) lineHeight / (double) worldObjectTexture.getHeight() * (double) worldObjectTexture.getWidth());
            int startLine = (int) ((MathUtils.angleClamp(worldObject.getAngleFrom(playerPos) - playerOrient+Math.PI)-Math.PI) / lineAngle);
            for (int l = 0; l < verticalLineCount; l++) {
                int textureColumn = worldObjectTexture.getWidth() * l / verticalLineCount;
                if (l + startLine > 0 && l + startLine < lineDepths.length && lineDepths[l + startLine] > objectDistance) {
                    g.drawImage(worldObjectTexture, startLine + l, roofLineHeight + player.getVerticalOrientation(), startLine + l + 1, screenSize.height - roofLineHeight + player.getVerticalOrientation(), textureColumn, 0, textureColumn + 1, worldObjectTexture.getHeight(), this);
                }
            }
             }
        }
    }

    private void addVectorToPoint(Point2D.Double currentRayPosition, Vector2D lineVector) {
        currentRayPosition.x += lineVector.getWidth();
        currentRayPosition.y += lineVector.getHeight();

    }

    private void drawFog(Graphics g, int line) {
        int lineHeight;
        lineHeight = screenSize.height / 2 - 10;
        g.setColor(Color.black);
        g.drawLine(line, lineHeight + player.getVerticalOrientation(), line, screenSize.height - lineHeight + player.getVerticalOrientation());
    }
}
