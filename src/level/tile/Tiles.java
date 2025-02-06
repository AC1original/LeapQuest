package level.tile;

import level.tile.tiles.BarrierTile;
import level.tile.tiles.FallbackTile;
import level.tile.tiles.GrassTile;
import level.tile.tiles.VoidTile;
import utils.Logger;

import java.util.ArrayList;
import java.util.List;

//TODO: Class independent tiles
public class Tiles {
    private static final List<Tile> registered = new ArrayList<>();

    public static Tile FALLBACK_TILE = register(new FallbackTile());
    public static Tile VOID_TILE = register(new VoidTile());
    public static Tile GRASS_TILE = register(new GrassTile());
    public static Tile BARRIER_TILE = register(new BarrierTile());

    private static Tile register(Tile tile) {
        registered.add(tile);
        Logger.info(tile, "Registered tile: " + tile.getName() + ".");
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
        return FALLBACK_TILE.getTypes()[0];
    }

    public static int getNextID() {
        int length = 0;
        for (Tile tile : registered) {
            length += tile.getTypes().length;
        }
        for (int i = 0; i < length + 1; i++) {
            if (getTypeByID(i).equals(FALLBACK_TILE.getTypes()[0])) {
                return i;
            }
        }
        return length + 1;
    }

    public static List<Tile> getRegistered() {
        return registered;
    }
}
