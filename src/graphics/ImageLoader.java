package graphics;

import utils.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class ImageLoader {

    public static BufferedImage getImageByPath(String path) {
        BufferedImage img;
        try {
            img = ImageIO.read(Objects.requireNonNull(ImageLoader.class.getResource("/" + path)));
            Logger.log("ImageLoader: Loaded image at: " + path + ".");
        } catch (Exception e) {
            Logger.log("ImageLoader: Failed loading image \""+path+"\"   |   " + e.getMessage() + ". Returned empty.png instead.", true);
            try {
                return ImageIO.read(Objects.requireNonNull(ImageLoader.class.getResource("/res/util/empty.png")));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return img;
    }

    public static BufferedImage getSubImage(BufferedImage img, int x, int y, int width, int height) {
        return img.getSubimage(x, y, width, height);
    }

    public static BufferedImage getSubImage(String path, int x, int y, int width, int height) {
        return getImageByPath(path).getSubimage(x, y, width, height);
    }
}
