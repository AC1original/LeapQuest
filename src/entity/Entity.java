package entity;

import graphics.animation.Animation;
import graphics.animation.AnimationManager;
import level.LevelManager;
import level.tile.TileType;
import main.GamePanel;
import org.jetbrains.annotations.NotNull;
import utils.HitBox;
import utils.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;

//TODO: Better hitbox
//TODO: Heath & enemies
//TODO: Stop jump when bump head
//TODO: Short jump cooldown
//TODO: FIX IS ON GROUND!!!!
@SuppressWarnings({"UnusedReturnValue", "unchecked"})
public abstract class Entity<T extends Entity<?>> {
    public static final String DEFAULT_PATH = "/res/entity/";
    protected int x = 0, y = 0, speed = 4;
    protected Direction direction = Direction.RIGHT;
    protected Direction lookDirection = Direction.RIGHT;
    private final Point location = new Point(x, y);
    private final HitBox hitBox = new HitBox(x, y, getWidth(), getHeight());
    private long lastMoved = 0;
    private boolean moving = false, showHitBox = false;
    private float fallSpeed = 0;
    protected int maxFallSpeed = 10;
    protected final float GRAVITY = 0.3f;
    private Animation animation;

    public abstract BufferedImage getImage();
    public abstract T onRemove();
    public abstract T onSpawn();
    public abstract T setImage(BufferedImage image);
    public abstract int getWidth();
    public abstract int getHeight();


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
        int fx = x + getWidth() / 2;
        int fy = y + getHeight() / 2;
        return Math.abs(fx - x + fy - y);
    }

    public synchronized T playAnimation(Animation animation) {
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

    public synchronized T stopAnimation() {
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

    public synchronized T move(Direction direction, int speed) {
        setDirection(direction);

        Point targetLoc = Direction.getNewLocation(getLocation(), speed, direction);

        LevelManager levelManager = getGamePanel().getLevelManager();
        HitBox uEntityBox = getHitBox();
        uEntityBox.setX(targetLoc.x);
        uEntityBox.setY(targetLoc.y);

        TileType tile = levelManager.wouldCollideTile(uEntityBox);
        if (tile != null) {
            for (int i = 0; i < speed; i++) {
                Point step = Direction.getNewLocation(getLocation(), 1, direction);
                uEntityBox.setX(step.x);
                uEntityBox.setY(step.y);
                if (!levelManager.wouldCollide(uEntityBox)) {
                    this.x = step.x;
                    this.y = step.y;
                } else {
                    tile.parent().onCollide(this, direction);
                    break;
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

    public synchronized T move(Direction direction) {
        return move(direction, getSpeed());
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
        setLookDirection(direction);
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
            fallSpeed += getGravity();
            if (fallSpeed > maxFallSpeed) fallSpeed = maxFallSpeed;
            move(Direction.DOWN, Math.round(fallSpeed));
        } else fallSpeed = 0;
        return (T) this;
    }

    public boolean isOnGround() {
        LevelManager levelManager = getGamePanel().getLevelManager();
        HitBox collideBox = levelManager.wouldCollideHitBox(getHitBox());

        if (collideBox != null) {
            if (collideBox.getY() < getY() - getHeight()) {
                return true;
            }
        }
        return false;
    }

    public T jump() {
        return jump((short) 10);
    }

    public T jump(short strength) {
        if (isOnGround()) {
            fallSpeed -= (float) strength;
            move(Direction.UP, strength);
        }
        return (T) this;
    }

    public HitBox getHitBox() {
        hitBox.setX(getX());
        hitBox.setY(getY());
        hitBox.resize(getWidth(), getHeight());
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

    public boolean isFalling() {
        return fallSpeed > 0;
    }

    public int getMaxFallSpeed() {
        return maxFallSpeed;
    }

    public final float getGravity() {
        return GRAVITY;
    }
}
