package entity;

import graphics.animation.Animation;
import graphics.animation.AnimationManager;
import level.LevelManager;
import level.tile.TileType;
import main.GamePanel;
import org.jetbrains.annotations.NotNull;
import utils.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;

//TODO: Jump physics
//TODO: Better hitbox
//TODO: Heath & enemies

@SuppressWarnings({"UnusedReturnValue", "unchecked"})
public abstract class Entity<T extends Entity<?>> {
    public static final String DEFAULT_PATH = "/res/entity/";
    protected int x = 0, y = 0, width = 50, height = 50, speed = 5;
    protected Direction direction = Direction.RIGHT;
    protected Direction lookDirection = Direction.RIGHT;
    private final Point location = new Point(x, y);
    private final Rectangle hitBox = new Rectangle(x, y, width, height);
    private long lastMoved = 0;
    private boolean moving = false, falling = false, showHitBox = false;
    private Animation animation;

    public abstract BufferedImage getImage();
    public abstract T onRemove();
    public abstract T onSpawn();
    public abstract T setImage(BufferedImage image);

    public T onTick() {
        if (System.currentTimeMillis() - lastMoved >= 350) {
            setMoving(false);
        }
        checkPhysics();
        return (T) this;
    }

    public int distanceTo(final @NotNull Entity<?> entity) {
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
                this.animation = getAnimationManager().play(animation);
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

    public T move(Direction direction, int speed) {
        setDirection(direction);
        setLookDirection(direction);

        Point targetLoc = Direction.getNewLocation(getLocation(), speed, direction);

        LevelManager levelManager = getGamePanel().getLevelManager();
        Rectangle uEntityBox = getHitBox();
        uEntityBox.x = targetLoc.x;
        uEntityBox.y = targetLoc.y;


        TileType tile = levelManager.wouldCollideTile(uEntityBox);
        if (tile != null) {
            for (int i = 0; i < speed; i++) {
                Point step = Direction.getNewLocation(getLocation(), 1, direction);
                uEntityBox.x = step.x;
                uEntityBox.y = step.y;
                if (!levelManager.wouldCollide(uEntityBox)) {
                    this.x = step.x;
                    this.y = step.y;
                } else {
                    tile.parent().onCollide(this, direction);
                }
            }
        } else {
            this.x = targetLoc.x;
            this.y = targetLoc.y;
        }

        setMoving(true);
        lastMoved = System.currentTimeMillis();
        return (T) this;
    }

    public T move(Direction direction) {
        move(direction, getSpeed());
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
        if (!isOnGround()) {
            falling = true;
            move(Direction.DOWN, 3);
        } else falling = false;
        return (T) this;
    }

    public boolean isOnGround() {
        LevelManager levelManager = getGamePanel().getLevelManager();
        if (levelManager.wouldCollideHitBox(getHitBox()) != null) {
            Rectangle hit = levelManager.wouldCollideHitBox(getHitBox());
            return getHitBox().y + getHitBox().height > hit.y;
        } else {
            return false;
        }
    }

    public T jump() {
        return jump((short) 10);
    }

    public T jump(short strength) {
        move(Direction.UP, strength);
        return (T) this;
    }

    public Rectangle getHitBox() {
        hitBox.setBounds(getX() + getHitBoxBufferX() / 2, getY() + getHitBoxBufferY() / 2,
                getWidth() - getHitBoxBufferX(), getHeight() - getHitBoxBufferY());
        return hitBox;
    }

    public int getHitBoxBufferX() {
        return 5;
    }

    public int getHitBoxBufferY() {
        return 5;
    }

    public final Point getLocation() {
        location.move(getX(), getY());
        return location;
    }

    public T showHitBox(boolean hitBox) {
        this.showHitBox = hitBox;
        return (T) this;
    }

    public boolean isHitBoxShown() {
        return showHitBox;
    }

    protected T onDraw(Graphics g) {
        return (T) this;
    }

    public Direction getLookDirection() {
        return lookDirection;
    }

    public long getLastMoved() {
        return lastMoved;
    }

    public T setLookDirection(Direction lookDirection) {
        if (lookDirection.equals(Direction.LEFT) || lookDirection.equals(Direction.RIGHT)) {
            this.lookDirection = lookDirection;
        }
        return (T) this;
    }

    public T setFalling(boolean falling) {
        this.falling = falling;
        return (T) this;
    }

    public boolean isFalling() {
        return falling;
    }
}
