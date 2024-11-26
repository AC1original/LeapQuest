package graphics;
import utils.caching.Cache;
import utils.Logger;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ImageLoader {
    private static final Cache<BufferedImage> cachedImages = new Cache.CacheBuilder<BufferedImage>()
            .timeoutDelay(5, TimeUnit.MINUTES)
            .deleteAfterTimeout(true)
            .onlyDeleteWhenUnused(true)
            .build();


    public static BufferedImage getCachedOrLoad(String path, String name) {
        if (cachedImages.contains(name)) {
            return cachedImages.get(name);
        } else {
            BufferedImage fallback = load(path);
            cachedImages.add(name, fallback);
            return fallback;
        }
    }

    public static BufferedImage getCached(String name) {
        if (cachedImages.contains(name)) {
            return cachedImages.get(name);
        }
        return load("fallbackimage");
    }

    public static BufferedImage load(String path) {
        BufferedImage image;
        try {
            image = ImageIO.read(Objects.requireNonNull(ImageLoader.class.getResource(path)));
            Logger.log("ImageLoader: Loaded image at: " + path + ".");
        } catch (Exception e) {
            Logger.log("ImageLoader: Failed loading image \""+path+"\" | " + e + ". Returned fallback image instead.", true);
            BufferedImage fallback = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
            fallback.setRGB(0, 0, new Color(241, 28, 28).getRGB());
            fallback.setRGB(1, 0, new Color(0, 0, 0).getRGB());
            fallback.setRGB(1, 1, new Color(241, 28, 28).getRGB());
            fallback.setRGB(0, 1, new Color(0, 0, 0).getRGB());
            return fallback;
        }
        return image;
    }
}
