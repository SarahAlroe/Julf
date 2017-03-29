import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by silasa on 8/21/16.
 */
public class FileHelper {
    public static final String MOD_PRE_DIR = "mod/";
    public static final String MAP_DIRECTORY = "maps/";
    public static final String MAP_EXTENSION = ".png";
    public static final String MAP_CO_PRE_EXT = "_CO";
    public static final String MAP_WO_PRE_EXT = "_WO";
    public static final String TEXTURE_DIRECTORY = "textures/";
    public static final String TEXTURE_EXTENSION = ".png";
    public static final String WO_DIRECTORY = "wos/";
    public static final String WO_EXTENSION = ".wo";

    public static URL getFileUrl(String filename) {
        if (new File(MOD_PRE_DIR + filename).exists()) {
            return Julf.class.getResource(MOD_PRE_DIR + filename);
        } else {
            return Julf.class.getResource(filename);
        }
    }

    public static BufferedImage loadImage(String name) {
        URL url = getFileUrl(name);
        BufferedImage img = null;
        try {
            img = ImageIO.read(url);
        } catch (Exception e) {
        }
        return img;
    }

    public static BufferedImage loadMapCollisionImage(String mapName) {
        return loadImage(MAP_DIRECTORY+mapName+MAP_CO_PRE_EXT+MAP_EXTENSION);
    }
    public static BufferedImage loadMapWorldObjectImage(String mapName) {
        return loadImage(MAP_DIRECTORY+mapName+MAP_WO_PRE_EXT+MAP_EXTENSION);
    }
    public static BufferedImage loadTexture(String textureName) {
        return loadImage(TEXTURE_DIRECTORY+textureName+TEXTURE_EXTENSION);
    }
    public static String loadWOString(String id) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(WO_DIRECTORY+id+WO_EXTENSION));
        return new String(encoded, StandardCharsets.UTF_8);
    }
    public static String getIdFromRgb(int rgb) {
        return Integer.toHexString(rgb).substring(2);
    }
}
