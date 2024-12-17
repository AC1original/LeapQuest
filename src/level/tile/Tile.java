package level.tile;
import entity.Direction;
import entity.Entity;
import main.GamePanel;

import java.awt.*;

public interface Tile {
    int DEFAULT_SIZE = 64;

    TileType[] getTypes();
    default int getWidth() {
        return DEFAULT_SIZE;
    }
    default int getHeight() {
        return DEFAULT_SIZE;
    }
    String getName();
    void tick();
    void onCollide(Entity<?> entity, Direction direction);
    boolean isSolid();
    boolean register();
    default GamePanel getGamePanel() {
        return GamePanel.getInstance();
    }
}
