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

    private static final Point newLoc = new Point(0, 0);
    public static Point getNewLocation(Point location, int speed, Direction direction) {
        newLoc.move(location.x + speed * direction.getDeltaX(), location.y + speed * direction.getDeltaY());
        return newLoc;
    }
}
