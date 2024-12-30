package level.tile.tiles;

import level.tile.Tile;
import level.tile.TileType;

import java.awt.image.BufferedImage;

public class BarrierTile extends VoidTile {
    private final TileType[] types= new TileType[]{new TileType(this, 6, null, new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB), -1, -1)};

    @Override
    public TileType[] getTypes() {
        return types;
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}
