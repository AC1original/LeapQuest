package utils;

import java.awt.*;

public class HitBox {
    private int x;
    private int y;
    private int width;
    private int height;
    private final Point location = new Point();

    public HitBox(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean intersects(HitBox hitBox) {
        return this.x <= hitBox.x + hitBox.width &&
                this.x + this.width >= hitBox.x &&
                this.y <= hitBox.y + hitBox.height &&
                this.y + this.height >= hitBox.y;
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Point getLocation() {
        location.move(this.x, this.y);
        return location;
    }

    @Override
    public String toString() {
        return "HitBox [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height;
    }
}
