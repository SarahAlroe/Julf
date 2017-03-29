import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by silasa on 8/21/16.
 */
public class TextureHandler {
    private static TextureHandler instance = new TextureHandler();
    private HashMap<String, BufferedImage> textures = new HashMap<>();

    public static TextureHandler getInstance() {
        return instance;
    }

    public void addTextureIfNew(int textureID) {
            String textureName = Integer.toHexString(textureID).substring(2);
            addTextureIfNew(textureName);
    }
    public void addTextureIfNew(String textureID){
        if (!textureExists(textureID)) {
        textures.put(textureID, FileHelper.loadTexture(textureID));
        System.out.println(textureID);}
    }
    public BufferedImage getTexture(int textureID){
        return getTexture(FileHelper.getIdFromRgb(textureID));
    }
    public BufferedImage getTexture(String textureID){
        return textures.get(textureID);
    }

    public boolean textureExists(String textureName) {
        return (textures.containsKey(textureName));
    }

    public void addTexturesFromMap(BufferedImage map){
        int mapWidth = map.getWidth();
        int mapHeight = map.getHeight();
        Config config = Config.getInstance();
        int x = 0;
        while (x < mapWidth) {
            int y = 0;
            while (y < mapHeight) {
                    addTextureIfNew(map.getRGB(x,y));
                y++;
            }
            x++;
        }
    }
    public void addTexturesFromWOType(){
        WOTypeHandler woTypeHandler =  WOTypeHandler.getInstance();
        for (WOType woType :
                woTypeHandler.woTypes) {
            addTextureIfNew(woType.getID());
        }
    }

}
