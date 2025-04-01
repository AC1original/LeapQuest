package entity;

import graphics.Drawable;
import graphics.animation.Animation;
import graphics.animation.AnimationManager;
import level.LevelManager;
import main.LeapQuest;
import org.jetbrains.annotations.NotNull;
import utils.HitBox;
import utils.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

//TODO: Health & enemies
//TODO: Stop jump when bump head
//TODO: Short jump cooldown
//TODO: Ressource management / Default ressource path
//TODO: HitBox fix
@SuppressWarnings({"UnusedReturnValue", "unchecked"})
public abstract class Entity<T extends Entity<?>> implements Drawable {
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
    protected float gravity = 0.3f;
    private Animation animation;

    public abstract BufferedImage getImage();
    protected abstract T onRemove();
    protected abstract T onSpawn();
    public abstract T setImage(BufferedImage image);
    public abstract int getWidth();
    public abstract int getHeight();


    protected T onTick() {
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

    public T playAnimation(Animation animation) {
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
            getAnimationManager().stop(animation);
            animation = null;
        }
        return (T) this;
    }

    public T move(Direction direction, int speed) {
        setDirection(direction);

        int cP = collisionPrediction(direction, speed);
        this.x += cP * direction.getDeltaX();
        this.y += cP * direction.getDeltaY();

        setMoving(true);
        lastMoved = System.currentTimeMillis();
        return (T) this;
    }

    public T move(Direction direction) {
        return move(direction, getSpeed());
    }

    public T teleport(Point location) {
        return teleport(location.x, location.y);
    }

    public T teleport(int x, int y) {
        this.x = x;
        this.y = y;
        Logger.info(this, "Teleported to (X | Y): " + x + " | " + y);
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

    protected T setDirection(Direction direction) {
        this.direction = direction;
        setLookDirection(direction);
        return (T) this;
    }

    public AnimationManager getAnimationManager() {
        return LeapQuest.instance.getAnimationManager();
    }

    public Animation getAnimation() {
        return animation;
    }

    public boolean isMoving() {
        return moving;
    }

    protected T setMoving(boolean moving) {
        this.moving = moving;
        return (T) this;
    }

    protected T checkPhysics() {
        if (!isOnGround()) {
            fallSpeed += getGravity();
            if (fallSpeed > maxFallSpeed) fallSpeed = maxFallSpeed;
            move(Direction.DOWN, Math.round(fallSpeed));
        } else fallSpeed = 0;
        return (T) this;
    }

    public boolean isOnGround() {
        HitBox uEntityBox = getHitBox();
        uEntityBox.move(0, 1);
        LevelManager levelManager = LeapQuest.instance.getLevelManager();
        List<HitBox> collideBoxes = levelManager.getCollisions(uEntityBox);

        if (!collideBoxes.isEmpty()) {
            return collideBoxes
                    .stream()
                    .anyMatch(box -> getHitBox().getY() + getHitBox().getHeight() <= box.getY());
        }
        return false;
    }

    public T jump() {
        return jump((short) 10);
    }

    public T jump(short strength) {
        if (isOnGround()) {
            fallSpeed -= strength;
            move(Direction.UP, strength);
        }
        return (T) this;
    }

    public HitBox getHitBox() {
        hitBox.setX(getX() + getHitBoxBufferX() / 2);
        hitBox.setY(getY() + getHitBoxBufferY() / 2);
        hitBox.resize(getWidth() - getHitBoxBufferX(), getHeight() - getHitBoxBufferY());
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
        this.getHitBox().setVisible(hitBox);
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

    protected T setLookDirection(Direction lookDirection) {
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
        return gravity;
    }

    public int collisionPrediction(Direction direction, int speed) {
        return collisionPrediction(getHitBox(), direction, speed);
    }

    public int collisionPrediction(HitBox hitBox, Direction direction, int speed) {
        return collisionPrediction(hitBox.getX(), hitBox.getY(), hitBox.getWidth(), hitBox.getHeight(), direction, speed);
    }

    public int collisionPrediction(int x, int y, int width, int height, Direction direction, int speed) {
        LevelManager levelManager = LeapQuest.instance.getLevelManager();

        for (int i = 0; i < speed; i++) {
            x += direction.getDeltaX();
            y += direction.getDeltaY();

            if (levelManager.checkCollision(x, y, width, height)) {
                return i;
            }
        }
        return speed;
    }

    @Override
    public Drawable parent() {
        return this;
    }

    @Override
    public Priority priority() {
        return Priority.DEFAULT;
    }

    @Override
    public void fDraw(Graphics g) {
        g.drawImage(getImage(), getX(), getY(), getWidth(), getHeight(), null);
    }
}
