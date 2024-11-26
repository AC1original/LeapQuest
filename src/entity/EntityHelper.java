package entity;
import entity.player.Player;
import main.GamePanel;
import utils.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class EntityHelper {
    private final HashSet<Entity<?>> entities = new HashSet<>();
    private final Player player;

    public EntityHelper() {
        player = GamePanel.register(new Player());
    }

    public void spawn(Entity<?> entity) {
        spawn(entity, entity.x, entity.y);
    }

    public void spawn(Entity<?> entity, int x, int y) {
        entity.x = x;
        entity.y = y;
        entity.onSpawn();
        entities.add(entity);
        Logger.log(this.getClass(), "Successfully added Entity \"" + entity.getClass().getSimpleName() + "\"");
    }

    public void remove(Entity<?> entity) {
        if (entities.contains(entity)) {
            Logger.log(this.getClass(), "Failed to remove Entity \"" + entity.getClass().getSimpleName() + "\". Entity not found!", true);
            return;
        }
        entity.onRemove();
        entities.remove(entity);
        Logger.log(this.getClass(), "Successfully removed Entity \"" + entity.getClass().getSimpleName() + "\".");
    }

    public List<Entity<?>> getEntitiesAt(int x, int y) {
        final List<Entity<?>> retuEntities = new ArrayList<>();
        retuEntities.add(entities.stream().filter(entity -> entity.x == x && entity.y == y).findAny().orElse(null));
        return retuEntities;
    }

    public void tick() {
        entities.forEach(Entity::onTick);
    }

    public void drawEntities(Graphics g) {
        for (Entity<?> entity : entities) {
            g.drawImage(entity.getImage(), entity.x, entity.y, entity.width, entity.height, null);
        }
    }

    public List<Entity<?>> getEntities() {
        return List.copyOf(entities);
    }

    public Player getPlayer() {
        return player;
    }
}