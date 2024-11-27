package level.tile.tiles;

import entity.Direction;
import entity.Entity;
import graphics.ImageLoader;
import level.tile.Tile;
import level.tile.TileType;

public class GrassTile implements Tile {
    @Override
    public TileType[] getTypes() {
        return new TileType[]{new TileType(this, 0)};
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
    public boolean register() {
        return false;
    }
}
