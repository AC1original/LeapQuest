package entity;

import graphics.animation.Animation;

import java.awt.image.BufferedImage;

public abstract class Entity {
    protected int x = 0, y = 0, width = 50, height = 50, speed = 3;
    protected Animation animation;

    public abstract BufferedImage getImage();
    public abstract void onTick();

    public int distanceTo(Entity entity) {
        return distanceTo(entity.x, entity.y);
    }

    public int distanceTo(int x, int y) {
        return Math.abs(this.x-x) + (this.y-y);
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }
}
