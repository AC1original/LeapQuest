package level.tile.tiles;

import entity.Direction;
import entity.Entity;
import graphics.ImageLoader;
import level.tile.Tile;
import level.tile.TileType;
import level.tile.Tiles;

import java.awt.image.BufferedImage;

public class GrassTile implements Tile {
    private final BufferedImage tileSet = ImageLoader.getCachedOrLoad("/res/level/tiles/default_tileset.png", "default_tileset");

    private final TileType[] types = {
            new TileType(this, Tiles.getNextID(), "/res/level/tiles/default_tileset.png",
                    ImageLoader.getCachedOrLoad(tileSet.getSubimage(0, 128, 24, 32),
                            "grass_bottom_left"), -1, -1),

            new TileType(this, Tiles.getNextID() + 1, "/res/level/tiles/default_tileset.png",
                    ImageLoader.getCachedOrLoad(tileSet.getSubimage(24, 128, 24, 32),
                            "grass_bottom_middle_1"), -1, -1),

            new TileType(this, Tiles.getNextID() + 2, "/res/level/tiles/default_tileset.png",
                    ImageLoader.getCachedOrLoad(tileSet.getSubimage(48, 128, 24, 32),
                            "grass_bottom_middle_2"), -1, -1),

            new TileType(this, Tiles.getNextID() + 3, "/res/level/tiles/default_tileset.png",
                    ImageLoader.getCachedOrLoad(tileSet.getSubimage(72, 128, 24, 32),
                            "grass_bottom_right"), -1, -1),

            new TileType(this, Tiles.getNextID() + 4, "/res/level/tiles/default_tileset.png",
                    ImageLoader.getCachedOrLoad(tileSet.getSubimage(288, 96, 64, 32),
                            "default_grass_block"), 64 * 2, 32 * 2)
    };

    @Override
    public int getHeight() {
        return 32 * 2;
    }

    @Override
    public int getWidth() {
        return 24 * 2;
    }

    @Override
    public TileType[] getTypes() {
        return types;
    }

    @Override
    public String getName() {
        return "grass_tile";
    }

    @Override
    public void tick() {}

    @Override
    public void onCollide(Entity<?> entity, Direction direction) {}

    @Override
    public boolean isSolid() {
        return true;
    }
}
