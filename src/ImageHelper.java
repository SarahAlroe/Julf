import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * Created by silasa on 8/21/16.
 */
public class ImageHelper {

    public static final String IMAGE_DIRECTORY = "images/";
    public static final String IMAGE_EXTENSION = ".png";

    public static BufferedImage loadImage(String name) {
        String imgFileName = IMAGE_DIRECTORY + name + IMAGE_EXTENSION;
        URL url = Julf.class.getResource(imgFileName);
        BufferedImage img = null;
        try {
            img = ImageIO.read(url);
        } catch (Exception e) {
        }
        return img;
    }
}
