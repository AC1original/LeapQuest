package graphics;
import utils.Logger;
import utils.caching.Cache;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ImageLoader {
    private static final Cache<BufferedImage> cachedImages = new Cache.CacheBuilder<BufferedImage>()
            .objectsExpires(true)
            .objectsOnlyExpiresWhenUnused(true)
            .objectsExpiresAfter(7, TimeUnit.MINUTES)
            .deleteObjectsWhenExpired(true)
            .build();

    private static final BufferedImage fallback;
    static {
        fallback = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        fallback.setRGB(0, 0, new Color(241, 28, 28).getRGB());
        fallback.setRGB(1, 0, new Color(0, 0, 0).getRGB());
        fallback.setRGB(1, 1, new Color(241, 28, 28).getRGB());
        fallback.setRGB(0, 1, new Color(0, 0, 0).getRGB());
    }

    public static BufferedImage getCachedOrLoad(String path) {
        return getCachedOrLoad(path, String.valueOf(path.hashCode()));
    }

    public static BufferedImage getCachedOrLoad(BufferedImage image, String name) {
        if (cachedImages.contains(name)) {
            return cachedImages.get(name);
        } else {
            return cache(image, name);
        }
    }

    public static BufferedImage getCachedOrLoad(String path, String name) {
        if (cachedImages.contains(name)) {
            return cachedImages.get(name);
        } else {
            return cache(load(path), name);
        }
    }

    public static BufferedImage getCached(String name) {
        if (cachedImages.contains(name)) {
            return cachedImages.get(name);
        }
        return getFallback();
    }

    public static BufferedImage cache(BufferedImage img, String name) {
        cachedImages.add(name, img);
        Logger.info(ImageLoader.class, "Cached image '" + name + "'.");
        return img;
    }

    public static BufferedImage load(String path) {
        BufferedImage image;
        try (var stream = ImageLoader.class.getResourceAsStream(path)) {
            if (stream == null) throw new IllegalArgumentException("Resource not found: " + path);
            image = ImageIO.read(stream);
            Logger.info(ImageLoader.class, "Loaded image at: " + path + ".");
        } catch (Exception e) {
            Logger.warn(ImageLoader.class, "Failed loading image \""+path+"\" | " + e + ". Returned fallback image instead.");
            return getFallback();
        }
        return image;
    }

    public static BufferedImage toBufferedImage(Image img) {
        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return bufferedImage;
    }

    public static BufferedImage getFallback() {
        return fallback;
    }
}
