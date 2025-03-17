package entity;

import java.awt.*;

public enum Direction {
    UP(0, -1),
    DOWN(0, +1),
    LEFT(-1, 0),
    RIGHT(+1, 0);

    private final int deltaX, deltaY;
    Direction(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    public boolean isHorizontal() {
        return this.deltaX != 0;
    }

    public boolean isVertical() {
        return this.deltaY != 0;
    }

    private final Point newLoc = new Point(0, 0);
    public Point getNewLocation(Point location, int speed) {
        newLoc.move(location.x + speed * this.getDeltaX(), location.y + speed * this.getDeltaY());
        return newLoc;
    }
}
