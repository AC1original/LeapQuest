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

    public static Point getNewLocation(Point location, int speed, Direction direction) {
        switch (direction) {
            case UP -> {
                return new Point(location.x, location.y - speed);
            }
            case DOWN -> {
                return new Point(location.x, location.y + speed);
            }
            case LEFT -> {
                return new Point(location.x - speed, location.y);
            }
            case RIGHT -> {
                return new Point(location.x + speed, location.y);
            }
            case null, default -> {
                return location;
            }
        }
    }
}
