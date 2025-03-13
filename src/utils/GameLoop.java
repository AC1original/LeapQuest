package utils;

import java.util.function.Consumer;

public class GameLoop {
    private boolean running = false;

    public GameLoop start(final int TARGET_TPS, Consumer<Integer> action) {
        if (!running) {
            running = true;
        } else {
            return this;
        }

        final long OPTIMAL_TIME = 1_000_000_000 / TARGET_TPS;

        long lastLoopTime = System.nanoTime();
        int tps = 0;
        int lastTps = 0;
        long lastTpsTime = 0;

        while (running) {
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;

            tps++;

            lastTpsTime += updateLength;
            if (lastTpsTime >= 1_000_000_000) {
                lastTps = tps;
                lastTpsTime = 0;
                tps = 0;
            }

            action.accept(lastTps);

            long sleepTime = (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1_000_000;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

    public void stop() {
        running = false;
    }
}
