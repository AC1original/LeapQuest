package level.tile;

import level.tile.tiles.GrassTile;
import main.GamePanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tiles {
    private static final List<Tile> registered = new ArrayList<>();

    public static Tile GRASS_TILE = register(new GrassTile());

    private static Tile register(Tile tile) {
        registered.add(tile);
        if (tile.register()) GamePanel.register(tile);
        return tile;
    }

    public static Tile getTileByID(int ID) {
        return getTypeByID(ID).parent();
    }

    public static TileType getTypeByID(int ID) {
        for (var tile : registered) {
            for (var type : tile.getTypes()) {
                if (type.ID() == ID) return type;
            }
        }
        return GRASS_TILE.getTypes()[0];
    }

    public static List<Tile> getRegistered() {
        return registered;
    }
}
