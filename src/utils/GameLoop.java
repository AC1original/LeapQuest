package utils;

import java.util.function.Consumer;

public class GameLoop {
    private boolean running = false;

    public void start(final int TARGET_FPS, Consumer<Integer> action) {
        if (!running) {
            running = true;
        } else {
            return;
        }

        final long OPTIMAL_TIME = 1_000_000_000 / TARGET_FPS;

        long lastLoopTime = System.nanoTime();
        int fps = 0;
        int lastFPS = 0;
        long lastFpsTime = 0;

        while (running) {
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;

            fps++;

            lastFpsTime += updateLength;
            if (lastFpsTime >= 1_000_000_000) {
                lastFPS = fps;
                lastFpsTime = 0;
                fps = 0;
            }

            action.accept(lastFPS);

            long sleepTime = (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1_000_000;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop() {
        running = false;
    }
}
