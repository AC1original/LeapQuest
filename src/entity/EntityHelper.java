package entity;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EntityHelper {
    private final List<Entity> entities = new ArrayList<>();

    public void spawn(Entity entity) {
        entities.add(entity);
    }

    public void spawn(Entity entity, int x, int y) {
        entity.x = x;
        entity.y = y;
        entities.add(entity);
    }

    public void remove(Entity entity) {
        entities.remove(entity);
    }

    public List<Entity> getEntityAt(int x, int y) {
        final List<Entity> retuEntities = new ArrayList<>();
        entities.forEach(entity -> {
            if (entity.x == x && entity.y == y)
                retuEntities.add(entity);
        });
        return retuEntities;
    }

    public void drawEntities(final Graphics g) {
        for (Entity entity : entities) {
            g.drawImage(entity.getImage(), entity.x, entity.y, entity.width, entity.height, null);
        }
    }
}