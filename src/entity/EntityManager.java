package entity;
import entity.player.Player;
import graphics.GameRenderer;
import main.LeapQuest;
import utils.HitBox;
import utils.Logger;
import utils.Ticked;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class EntityManager {
    private final Set<Entity<?>> entities = Collections.synchronizedSet(new HashSet<>());
    private final Set<Entity<?>> markedRemove = Collections.synchronizedSet(new HashSet<>());
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
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
        Logger.info(this, "Added Entity \"" + entity.getClass().getSimpleName() + "\".");
    }

    public void markForRemoval(Entity<?> entity) {
        if (!entities.contains(entity)) {
            Logger.warn(this.getClass(), "Failed to remove Entity \"" + entity.getClass().getSimpleName() + "\". Entity not found!");
            return;
        }

        markedRemove.add(entity);
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
        rwLock.writeLock().lock();
        try {
            var iterator = entities.iterator();
            while (iterator.hasNext()) {
                var e = iterator.next();
                e.onTick();
                if (markedRemove.contains(e)) {
                    e.onRemove();
                    LeapQuest.unregister(e);
                    renderer.removeDrawable(e);
                    renderer.removeDrawable(e.getHitBox());
                    iterator.remove();
                    markedRemove.remove(e);
                    Logger.info(this, "Removed Entity \"" + e.getClass().getSimpleName() + "\".");
                }
            }
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public List<Entity<?>> getEntities() {
        return List.copyOf(entities);
    }

    public Player getPlayer() {
        return player;
    }
}