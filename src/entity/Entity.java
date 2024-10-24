package entity;
import graphics.animation.Animation;
import graphics.animation.AnimationManager;
import main.GamePanel;
import main.Main;

import java.awt.image.BufferedImage;

public abstract class Entity {
    protected int x = 0, y = 0, width = 50, height = 50, speed = 10;
    protected Direction direction = Direction.RIGHT;
    private boolean moving = false;
    private Animation animation;

    public abstract BufferedImage getImage();
    public abstract void onTick();
    public abstract void onAdd();
    public abstract void onRemove();

    public int distanceTo(Entity entity) {
        return distanceTo(entity.x, entity.y);
    }

    public int distanceTo(int x, int y) {
        return Math.abs(this.x-x) + (this.y-y);
    }

    public void playAnimation(Animation animation) {
        if (this.animation != null) {
            if (this.animation.getClass() != animation.getClass()) {
                stopAnimation();
                this.animation = getGamePanel().getAnimationManager().play(animation);
            }
        } else {
            this.animation = getGamePanel().getAnimationManager().play(animation);
        }
    }

    public void stopAnimation() {
        if (animation != null) {
            getGamePanel().getAnimationManager().stop(animation);
            animation = null;
        }
    }

    public void move(Direction direction) {
        setDirection(direction);
        switch (direction) {
            case UP -> y -= speed;
            case DOWN -> y += speed;
            case LEFT -> x -= speed;
            case RIGHT -> x += speed;
        }
        setMoving(true);
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
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public AnimationManager getAnimationManager() {
        return Main.getGamePanel().getAnimationManager();
    }

    public final GamePanel getGamePanel() {
        return Main.getGamePanel();
    }

    public Animation getAnimation() {
        return animation;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }
}
