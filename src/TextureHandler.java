import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by silasa on 8/21/16.
 */
public class TextureHandler {
    private static TextureHandler instance = new TextureHandler();
    private HashMap<Integer, BufferedImage> textures = new HashMap<>();

    public static TextureHandler getInstance() {
        return instance;
    }

    public void addTextureIfNew(int textureID) {
        if (!textureExists(textureID)) {
            String textureName = Integer.toHexString(textureID).substring(2);
            textures.put(textureID, ImageHelper.loadImage(textureName));
        }

    }
    public BufferedImage getTexture(int textureID){
        return textures.get(textureID);
    }

    public boolean textureExists(int textureName) {
        return (textures.containsKey(textureName));
    }

}
