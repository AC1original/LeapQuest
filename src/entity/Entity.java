package entity;
import graphics.animation.Animation;
import graphics.animation.AnimationManager;
import main.GamePanel;
import main.Main;
import org.jetbrains.annotations.Nullable;
import utils.Logger;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

@SuppressWarnings("ALL")
public abstract class Entity<T extends Entity<?>> {
    public static final String DEFAULT_PATH = "/res/entity/";
    protected int x = 0, y = 0, width = 50, height = 50, speed = 10;
    protected Direction direction = Direction.RIGHT;
    private boolean moving = false;
    private Animation animation;
    private final GamePanel gp;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public abstract BufferedImage getImage();
    public abstract T onTick();
    public abstract T onAdd();
    public abstract T onRemove();

    public int distanceTo(Entity<?> entity) {
        return distanceTo(entity.x, entity.y);
    }

    public int distanceTo(int x, int y) {
        return Math.abs(this.x-x + this.y-y);
    }

    public T playAnimation(Animation animation) {
        if (getAnimationManager() == null) {
            Logger.log(this.getClass(), "Failed playing animation " + animation.getClass().getSimpleName() + ". AnimationManager is null", true);
            return (T) this;
        }
        if (this.animation != null) {
            if (this.animation.getClass() != animation.getClass()) {
                stopAnimation();
                if (getGamePanel() != null) {
                    this.animation = getAnimationManager().play(animation);
                }
            }
        } else {
            this.animation = getAnimationManager().play(animation);
        }
        return (T) this;
    }

    public T stopAnimation() {
        if (animation != null) {
            if (getAnimationManager() == null) {
                Logger.log(this.getClass(), "Failed to stop animation " + animation.getClass().getSimpleName() + ". AnimationManager is null", true);
                return (T) this;
            }
            getAnimationManager().stop(animation);
            animation = null;
        }
        return (T) this;
    }

    public T move(Direction direction) {
        setDirection(direction);
        switch (direction) {
            case UP -> y -= speed;
            case DOWN -> y += speed;
            case LEFT -> x -= speed;
            case RIGHT -> x += speed;
        }
        setMoving(true);
        return (T) this;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getSpeed() {
        return speed;
    }

    public T setSpeed(int speed) {
        this.speed = speed;
        return (T) this;
    }

    public Direction getDirection() {
        return direction;
    }

    public T setDirection(Direction direction) {
        this.direction = direction;
        return (T) this;
    }

    public final GamePanel getGamePanel() {
        return gp == null ? Main.getGamePanel() : gp;
    }

    public AnimationManager getAnimationManager() {
        return gp.getAnimationManager();
    }

    public Animation getAnimation() {
        return animation;
    }

    public boolean isMoving() {
        return moving;
    }

    public T setMoving(boolean moving) {
        this.moving = moving;
        return (T) this;
    }
}
