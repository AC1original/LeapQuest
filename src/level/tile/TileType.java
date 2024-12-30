package level.tile;

import graphics.ImageLoader;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;

public record TileType(Tile parent, int ID, @Nullable String path, @Nullable BufferedImage image, int width, int height) {

    public int width() {
        return width <= -1 ? parent.getWidth() : width;
    }

    public int height() {
        return height <= -1 ? parent.getHeight() : height;
    }

    public String path() {
        return path == null ? "/res/level/tiles/" + parent.getName() + ID + ".png" : path;
    }
    public BufferedImage image() {
        return image == null ? ImageLoader.getCachedOrLoad(path(), parent.getName() + ID) : image;
    }
}
