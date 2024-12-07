package level.tile;

import graphics.ImageLoader;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;

public record TileType(Tile parent, int ID, @Nullable String path, @Nullable BufferedImage image) {
    public String getPath() {
        return path == null ? "/res/level/tiles/" + parent.getName() + ID + ".png" : path;
    }
    public BufferedImage getImage() {
        return image == null ? ImageLoader.getCachedOrLoad(getPath(), parent.getName() + ID) : image;
    }
}
