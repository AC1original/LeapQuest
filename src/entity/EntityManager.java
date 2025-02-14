package entity;
import entity.player.Player;
import graphics.GameRenderer;
import main.LeapQuest;
import utils.HitBox;
import utils.Logger;
import utils.Ticked;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class EntityManager {
    private final HashSet<Entity<?>> entities = new HashSet<>();
    private final Player player;
    private final GameRenderer renderer;

    public EntityManager(GameRenderer renderer, Player player) {
        this.renderer = renderer;
        this.player = player;
        Logger.info(this, "Initialized.");
    }

    public void spawn(Entity<?> entity) {
        spawn(entity, entity.x, entity.y);
    }

    public void spawn(Entity<?> entity, int x, int y) {
        entity.x = x;
        entity.y = y;
        entity.onSpawn();
        entities.add(entity);
        renderer.addDrawable(entity);
        renderer.addDrawable(entity.getHitBox());
        Logger.info(this, "Successfully added Entity \"" + entity.getClass().getSimpleName() + "\".");
    }

    public void remove(Entity<?> entity) {
        if (entities.contains(entity)) {
            Logger.warn(this.getClass(), "Failed to remove Entity \"" + entity.getClass().getSimpleName() + "\". Entity not found!");
            return;
        }
        entity.onRemove();
        renderer.removeDrawable(entity);
        renderer.removeDrawable(entity.getHitBox());
        entities.remove(entity);
        Logger.info(this, "Successfully removed Entity \"" + entity.getClass().getSimpleName() + "\".");
    }

    public List<Entity<?>> getEntitiesAt(int x, int y) {
        HitBox hitBox = new HitBox(x, y, 1, 1);
        return entities
                .stream()
                .filter(entity -> entity.getHitBox().intersects(hitBox))
                .collect(Collectors.toList());
    }

    @Ticked
    public void tick() {
        entities.forEach(Entity::onTick);
    }

    public List<Entity<?>> getEntities() {
        return List.copyOf(entities);
    }

    public Player getPlayer() {
        return player;
    }
}