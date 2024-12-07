package level.tile.tiles;

import entity.Direction;
import entity.Entity;
import level.tile.Tile;
import level.tile.TileType;

import java.awt.image.BufferedImage;

public class VoidTile implements Tile {
    private final TileType[] types= new TileType[]{new TileType(this, 0, null, new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB))};

    @Override
    public TileType[] getTypes() {
        return types;
    }

    @Override
    public String getName() {
        return "void_tile";
    }

    @Override
    public void tick() {}

    @Override
    public void onCollide(Entity<?> entity, Direction direction) {}

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public boolean register() {
        return false;
    }
}
