/**
 * Created by silasa on 3/27/17.
 */
public class JulfDS {
    WorldMap worldMap;
    public static void main(String[] args) {
        String mapName = args[0];
        JulfDS julfDS = new JulfDS(mapName);
    }

    private void loadMap(String mapName) {
        worldMap = new WorldMap(mapName);
        WOTypeHandler.getInstance().loadMapWorldObjectTypes(mapName);
        WorldObjectHandler.getInstance().addObjectsFromMap(mapName);
    }
    public JulfDS(String mapName){
        loadMap(mapName);
        //Start server
    }
}
