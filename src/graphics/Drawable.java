package graphics;

import utils.Logger;

import java.awt.*;

public interface Drawable {

    void fDraw(Graphics graphics);

    Priority priority();

    Drawable parent();

    default int layer() {
        return -1;
    }

    default boolean visible() {
        return true;
    }

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
