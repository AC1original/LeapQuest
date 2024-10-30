package graphics;

import data.filemanager.FileManager;
import data.filemanager.IncorrectPathException;
import utils.CachedImage;
import utils.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ImageLoader {
    private static final String emptyPath = "/res/util/empty.png";
    private static final Set<CachedImage> cachedImages = new HashSet<>();

    public static BufferedImage getCached(String name) {
        for (Object image : cachedImages.toArray()) {
            if (image instanceof CachedImage cImg && cImg.name().equals(name)) {
                return cImg.image();
            }
        }
        return load(emptyPath);
    }

    public static BufferedImage getCachedUnsafe(String name) {
        for (Object image : cachedImages.toArray()) {
            if (image instanceof CachedImage cImg) {
                if (cImg.name().equals(name) || cImg.path().endsWith(name))
                    return cImg.image();
            }
        }
        return load(emptyPath);
    }

    public static BufferedImage loadOrGetCached(String path) {
        BufferedImage img = load(path);
        for (Object image : cachedImages.toArray()) {
            if (image instanceof CachedImage cImg && cImg.path().equals(path)) {
                img = cImg.image();
            }
        }
        return img;
    }

    public static BufferedImage getAutomated(String path) {
        BufferedImage image = loadOrGetCached(path);
        cachedImages.add(new CachedImage(getNameFromPath(path), path, image));
        return image;
    }

    public static BufferedImage loadAndCache(String path) {
        BufferedImage image = load(path);
        cachedImages.add(new CachedImage(getNameFromPath(path), path, image));
        return image;
    }

    public static CachedImage cache(String path) {
        CachedImage image = new CachedImage(getNameFromPath(path), path, load(path));
        cachedImages.add(image);
        return image;
    }

    public static BufferedImage load(String path) {
        BufferedImage image;
        try {
            image = ImageIO.read(Objects.requireNonNull(ImageLoader.class.getResource("/" + path)));
            Logger.log("ImageLoader: Loaded image at: " + path + ".");
        } catch (Exception e) {
            Logger.log("ImageLoader: Failed loading image \""+path+"\" | " + e.getMessage() + ". Returned fallback image instead.", true);
            try {
                return ImageIO.read(Objects.requireNonNull(ImageLoader.class.getResource("/res/util/empty.png")));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return image;
    }

    public static void deleteFromCache(String path) {
        for (Object image : cachedImages.toArray()) {
            if (image instanceof CachedImage cImg) {
                if (cImg.path().equals(path) || cImg.path().endsWith(path)) {
                    cachedImages.remove(image);
                }
            }
        }
    }

    public static BufferedImage getSubImage(BufferedImage img, int x, int y, int width, int height) {
        return img.getSubimage(x, y, width, height);
    }

    public static BufferedImage getSubImage(String path, int x, int y, int width, int height) {
        return load(path).getSubimage(x, y, width, height);
    }

    public static List<CachedImage> getCachedImages() {
        return cachedImages.stream().toList();
    }

    private static String getNameFromPath(String path) {
        String fixedPath = path.replaceAll("\\\\", "/");
        fixedPath = fixedPath.replace('\\', '/');
        fixedPath = fixedPath.replaceAll("//", "/");
        String[] split = fixedPath.split("/");
        return split[split.length-1];
    }
}
