import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by silasa on 8/24/16.
 */
public class WorldObjectHandler {
    private static WorldObjectHandler instance = new WorldObjectHandler();

    public static WorldObjectHandler getInstance() {
        return instance;
    }

    public ArrayList<WorldObject> getWorldObjects() {
        Collections.sort(worldObjects);
        return worldObjects;
    }

    private ArrayList<WorldObject> worldObjects = new ArrayList<>();

    public void addWorldObject (WorldObject object){
        worldObjects.add(object);
    }

    public void addObjectsFromMap(String mapName){
        addObjectsFromMap(FileHelper.loadMapCollisionImage(mapName));
    }

    public void addObjectsFromMap(BufferedImage map){
        int mapWidth = map.getWidth();
        int mapHeight = map.getHeight();
        Config config = Config.getInstance();
        int x = 0;
        while (x < mapWidth) {
            int y = 0;
            while (y < mapHeight) {
                if (map.getRGB(x, y) != config.getEmptyColor()){
                    addWorldObject(new WorldObject(new Point2D.Double((double)x,(double)y), map.getRGB(x, y)));
                }
                y++;
            }
            x++;
        }
    }

}
