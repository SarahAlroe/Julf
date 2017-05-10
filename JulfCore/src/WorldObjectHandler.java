import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

/**
 * Created by silasa on 8/24/16.
 */
public class WorldObjectHandler {
    private static WorldObjectHandler instance = new WorldObjectHandler();
    private ArrayList<WorldObject> worldObjects = new ArrayList<>();

    public static WorldObjectHandler getInstance() {
        return instance;
    }

    public ArrayList<WorldObject> getWorldObjects() {
        Collections.sort(worldObjects);
        return worldObjects;
    }

    public void addObjectsFromMap(String mapName) {
        addObjectsFromMap(FileHelper.loadMapWorldObjectImage(mapName));
    }

    public void addObjectsFromMap(BufferedImage map) {
        int mapWidth = map.getWidth();
        int mapHeight = map.getHeight();
        Config config = Config.getInstance();
        int x = 0;
        while (x < mapWidth) {
            int y = 0;
            while (y < mapHeight) {
                if (map.getRGB(x, y) != config.getEmptyColor()) {
                    addWorldObject(new WorldObject(new Point2D.Double((double) x, (double) y), map.getRGB(x, y)));
                }
                y++;
            }
            x++;
        }
    }

    public void addWorldObject(WorldObject object) {
        worldObjects.add(object);
    }

    public WorldObject getWorldObjectById(String playerId) {
        UUID uuid = UUID.fromString(playerId);
        for (WorldObject worldObject :
                worldObjects) {
            if (worldObject.getUid().equals(uuid)) {
                return worldObject;
            }
        }
        return null;
    }

    public void updateWorldObject(WorldObject object) {
        for (WorldObject wo :
                worldObjects) {
            if (wo.getUid().equals(object.getUid())) {
                wo.setPosition(object.getXPos(),object.getYPos(),object.getRotation());
                return;
            }
        }
    }

    public void destroyWorldObject(String objectId) {
        for (WorldObject worldObject : worldObjects) {
            if (worldObject.getUid().equals(UUID.fromString(objectId))) {
                worldObjects.remove(worldObject);
                return;
            }
        }
    }
}
