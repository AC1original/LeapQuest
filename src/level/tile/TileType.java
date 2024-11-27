package level.tile;

import graphics.ImageLoader;

import java.awt.image.BufferedImage;

public record TileType(Tile parent, int ID) {
    public String getPath() {
        return "/res/level/tiles/" + parent.getName() + ID + ".png";
    }
    public BufferedImage getImage() {
        return ImageLoader.getCachedOrLoad(getPath(), parent.getName() + ID);
    }
}
