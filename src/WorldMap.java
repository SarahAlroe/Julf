import java.awt.image.BufferedImage;

/**
 * Created by silasa on 8/20/16.
 */
class WorldMap {
    private int mapHeight;
    private int mapWidth;
    private BufferedImage map;
    private int[][] mapTable;
    private Config config;

    public WorldMap(BufferedImage cMap) {
        map = cMap;
        mapWidth = map.getWidth();
        mapHeight = map.getHeight();
        mapTable = new int[mapWidth][mapHeight];
        config = Config.getInstance();
    }

    public WorldMap(String mapName){
        this(FileHelper.loadMapCollisionImage(mapName));
    }

    public int getTile(int x, int y) {
        if (x >= mapWidth || y >= mapHeight || x < 0 || y < 0) {
            return config.getEmptyColor();
        }
        return mapTable[x][y];
    }
    public boolean hasTile (int x, int y){
        return (getTile(x,y) != Config.getInstance().getEmptyColor());
    }
}
