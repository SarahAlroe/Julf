import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by silasa on 3/27/17.
 */
public class WOTypeHandler {
    private static WOTypeHandler instance = new WOTypeHandler();
    public static WOTypeHandler getInstance() {
        return instance;
    }

    ArrayList<WOType> woTypes = new ArrayList<>();

    public void loadMapWorldObjectTypes(String mapName){
        BufferedImage worldObjectMapFile = FileHelper.loadMapWorldObjectImage(mapName);
        addWorldObjectsFromMap(worldObjectMapFile);
    }

    private void addWorldObjectsFromMap(BufferedImage map) {
        int mapWidth = map.getWidth();
        int mapHeight = map.getHeight();
        Config config = Config.getInstance();
        int x = 0;
        while (x < mapWidth) {
            int y = 0;
            while (y < mapHeight) {
                if (map.getRGB(x, y) != config.getEmptyColor()) {
                    addWorldObjectFromIdIfNew(FileHelper.getIdFromRgb(map.getRGB(x, y)));
                }
                y++;
            }
            x++;
        }
    }

    private void addWorldObjectFromIdIfNew(String id) {
        for (WOType woType :
                woTypes) {
            if (woType.getID().equals(id)) {
                return;
            }
        }
        addWorldObjectFromId(id);
    }

    private void addWorldObjectFromId(String id) {
        try {
            woTypes.add(new WOType(FileHelper.loadWOString(id)));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
