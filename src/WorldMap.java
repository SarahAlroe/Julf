import java.awt.image.BufferedImage;

/**
 * Created by silasa on 8/20/16.
 */
class WorldMap {
    private Julf main;
    private int mapHeight;
    private int mapWidth;
    private BufferedImage map;
    private int[][] mapTable;

    public WorldMap(Julf parrent, BufferedImage cMap) {
        main = parrent;
        map = cMap;
        mapWidth = map.getWidth();
        mapHeight = map.getHeight();
        mapTable = new int[mapWidth][mapHeight];
        int x = 0;
        while (x < mapWidth) {
            int y = 0;
            while (y < mapHeight) {
                mapTable[x][y] = map.getRGB(x, y);
                y++;
            }
            x++;
        }
    }

    public int getTile(int x, int y) {
        if (x >= mapWidth || y >= mapHeight || x < 0 || y < 0) {
            return main.conf.emptyColor;
        }
        return mapTable[x][y];
    }
}
