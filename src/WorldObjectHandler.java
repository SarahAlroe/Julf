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

}
