package graphics;
import org.jetbrains.annotations.Nullable;
import utils.CachedImage;
import utils.Logger;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.*;

public class ImageLoader {
    private static final String emptyPath = "/res/util/empty.png";
    private static final Set<CachedImage> cachedImages = new HashSet<>();

    public static BufferedImage getCached(String path, @Nullable String name) {
        return search(path, name);
    }


    public static BufferedImage loadOrGetCached(String path, @Nullable String name) {
        BufferedImage image = search(path, name);

        if (image == null) {
            image = load(path);
        }
        return image;
    }

    public static BufferedImage getAutomated(String path, @Nullable String name) {
        BufferedImage image = loadOrGetCached(path, name);
        cachedImages.add(new CachedImage(name == null ? getNameFromPath(path) : name, path, image));
        return image;
    }

    public static BufferedImage loadAndCache(String path, @Nullable String name) {
        BufferedImage image = load(path);
        cachedImages.add(new CachedImage(name == null ? getNameFromPath(path) : name, path, image));
        return image;
    }

    public static CachedImage cache(String path, @Nullable String name) {
        CachedImage image = new CachedImage(name == null ? getNameFromPath(path) : name, path, load(path));
        cachedImages.add(image);
        return image;
    }

    public static BufferedImage load(String path) {
        BufferedImage image;
        try {
            image = ImageIO.read(Objects.requireNonNull(ImageLoader.class.getResource(path)));
            Logger.log("ImageLoader: Loaded image at: " + path + ".");
        } catch (Exception e) {
            Logger.log("ImageLoader: Failed loading image \""+path+"\" | " + e + ". Returned fallback image instead.", true);
            try {
                return ImageIO.read(Objects.requireNonNull(ImageLoader.class.getResource(emptyPath)));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return image;
    }

    public static void deleteFromCache(String path, @Nullable String name) {
        for (Object image : cachedImages.toArray()) {
            if (image instanceof CachedImage cImg) {
                if (name != null) {
                    if (cImg.name().equals(name)) {
                        cachedImages.remove(image);
                        return;
                    }
                }
                if (cImg.path().endsWith(path)) {
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

    private static BufferedImage search(String path, @Nullable String name) {
        for (Object obj : cachedImages.toArray()) {
            if (!(obj instanceof CachedImage cImg)) continue;
            if (cImg.name().equals(name) || cImg.path().endsWith(path)) return cImg.image();
        }
        return null;
    }

    private static String getNameFromPath(String path) {
        String fixedPath = path.replaceAll("\\\\", "/");
        fixedPath = fixedPath.replace('\\', '/');
        fixedPath = fixedPath.replaceAll("//", "/");
        String[] split = fixedPath.split("/");
        return split[split.length-1];
    }
}
