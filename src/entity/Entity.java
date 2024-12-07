package entity;

import graphics.animation.Animation;
import graphics.animation.AnimationManager;
import level.LevelManager;
import level.tile.Tile;
import main.GamePanel;
import utils.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;

@SuppressWarnings({"UnusedReturnValue", "unchecked"})
public abstract class Entity<T extends Entity<?>> {
    public static final String DEFAULT_PATH = "/res/entity/";
    protected int x = 0, y = 0, width = 50, height = 50, speed = 5;
    protected Direction direction = Direction.RIGHT;
    private final Point location = new Point(x, y);
    private final Rectangle hitBox = new Rectangle(x, y, width, height);
    private long lastMoved = 0;
    private boolean moving = false, showHitBox = false;
    private Animation animation;

    public abstract BufferedImage getImage();

    public abstract T onRemove();

    public abstract T onSpawn();

    public T onTick() {
        if (System.currentTimeMillis() - lastMoved >= 350) {
            setMoving(false);
        }
        return (T) this;
    }

    public int distanceTo(final Entity<?> entity) {
        int ex = entity.getX() + entity.getWidth() / 2;
        int ey = entity.getY() + entity.getHeight() / 2;
        return distanceTo(ex, ey);
    }

    public int distanceTo(int x, int y) {
        int fx = x + width / 2;
        int fy = y + height / 2;
        return Math.abs(fx - x + fy - y);
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

        Point targetLoc = Direction.getNewLocation(getLocation(), getSpeed(), direction);

        LevelManager levelManager = getGamePanel().getLevelManager();
        Rectangle uEntityBox = getHitBox();
        uEntityBox.x = targetLoc.x;
        uEntityBox.y = targetLoc.y;

        Tile tile = levelManager.wouldCollide(uEntityBox);
        if (tile != null) {
            for (int i = 0; i < speed; i++) {
                Point step = Direction.getNewLocation(getLocation(), 1, direction);
                uEntityBox.x = step.x;
                uEntityBox.y = step.y;
                if (levelManager.wouldCollide(uEntityBox) == null) {
                    this.x = step.x;
                    this.y = step.y;
                } else tile.onCollide(this, direction);
            }
        } else {
            this.x = targetLoc.x;
            this.y = targetLoc.y;
        }

        setMoving(true);
        lastMoved = System.currentTimeMillis();
        return (T) this;
    }

    public T teleport(Point location) {
        return teleport(location.x, location.y);
    }

    public T teleport(int x, int y) {
        this.x = x;
        this.y = y;
        Logger.log(Entity.class, "Teleported entity \"" + this.getClass().getSimpleName() + "\" to (X | Y): " + x + " | " + y);
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
        return GamePanel.getInstance();
    }

    public AnimationManager getAnimationManager() {
        return GamePanel.getInstance().getAnimationManager();
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

    public T checkPhysics() {
        return (T) this;
    }

    public T jump() {
        return jump((short) 2);
    }

    public T jump(short strength) {
        return (T) this;
    }

    public Rectangle getHitBox() {
        hitBox.setBounds(getX() + getHitBoxBuffer() / 2, getY() + getHitBoxBuffer() / 2, getWidth() - getHitBoxBuffer(), getHeight() - getHitBoxBuffer());
        return hitBox;
    }

    public int getHitBoxBuffer() {
        return 5;
    }

    public Point getLocation() {
        location.move(getX(), getY());
        return location;
    }

    public void showHitBox(boolean hitBox) {
        this.showHitBox = hitBox;
    }

    public boolean isHitBoxShown() {
        return showHitBox;
    }

    protected T onDraw(Graphics g) {
        return (T) this;
    }
}
