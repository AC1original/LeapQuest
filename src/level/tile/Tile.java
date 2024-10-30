package level.tile;
import entity.Direction;
import entity.Entity;

public interface Tile {
    int DEFAULT_SIZE = 64;

    TileType[] getTypes();
    default int getWidth() {
        return DEFAULT_SIZE;
    }
    default int getHeight() {
        return DEFAULT_SIZE;
    }
    void tick();
    void onCollide(Entity entity, Direction direction);
}
