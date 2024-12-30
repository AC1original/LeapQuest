package level.tile.tiles;

import entity.Direction;
import entity.Entity;
import graphics.ImageLoader;
import level.tile.Tile;
import level.tile.TileType;

public class FallbackTile implements Tile {
    private final TileType[] types = {new TileType(this, -1, null, ImageLoader.getFallback(), -1, -1)};

    @Override
    public TileType[] getTypes() {
        return types;
    }

    @Override
    public String getName() {
        return "fallback_tile";
    }

    @Override
    public void tick() {}

    @Override
    public void onCollide(Entity<?> entity, Direction direction) {}

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public boolean register() {
        return false;
    }
}
