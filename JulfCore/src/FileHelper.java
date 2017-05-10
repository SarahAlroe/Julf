import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

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
        String filePath = WO_DIRECTORY + id + WO_EXTENSION;
        return loadTextFile(filePath);
    }

    private static String loadTextFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        System.out.println(file.getAbsolutePath());
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine())!=null){
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String fileContents = stringBuilder.toString();
        return fileContents;
    }

    public static String getIdFromRgb(int rgb) {
        return Integer.toHexString(rgb).substring(2);
    }
}
