package graphics;

import utils.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public interface Drawable {
    default BufferedImage drawImage() {
        return null;
    }

    default int imageX() {
        return 0;
    }

    default int imageY() {
        return 0;
    }

    default int width() {
        return 0;
    }

    default int height() {
        return 0;
    }

    default void freeDraw(Graphics graphics) {
        //do nothing
    }

    Priority priority();

    enum Priority {
        LOWEST,
        LOW,
        DEFAULT,
        HIGH,
        HIGHEST;

        public boolean isHigherThan(Priority priority) {
            return indexOf(this) > indexOf(priority);
        }

        private int indexOf(Priority priority) {
            for (int i = 0; i < Priority.values().length; i++) {
                if (Priority.values()[i].equals(priority)) {
                    return i;
                }
            }
            Logger.warn(Priority.class, "Failed to find correct Priority index. Drawing issues can occur!");
            return 0;
        }
    }
}
