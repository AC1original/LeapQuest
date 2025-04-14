package utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

public class GameLoop {
    private boolean running = false;
    private boolean paused = false;
    private boolean runOnThread = false;
    private boolean virtualThread = false;
    private String threadName = "";

    public GameLoop runOnThread(boolean runOnThread) {
        this.runOnThread = runOnThread;
        return this;
    }

    public GameLoop runThreadVirtual(boolean virtual) {
        this.virtualThread = virtual;
        return this;
    }

    public GameLoop setThreadName(String name) {
        this.threadName = name;
        return this;
    }

    public GameLoop start(final int TARGET_TPS, Consumer<Integer> action) {
        if (!running) {
            running = true;
        } else {
            return this;
        }

        Runnable runnable = () -> {
            final long OPTIMAL_TIME = 1_000_000_000 / TARGET_TPS;

            long lastLoopTime = System.nanoTime();
            int tps = 0;
            int lastTps = 0;
            long lastTpsTime = 0;

            while (running) {
                if (!paused) {
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
            }
        };

        if (runOnThread) {
            if (virtualThread) {
                var thread = Thread.ofVirtual();
                if (!threadName.isBlank()) {
                    thread.name(threadName);
                    thread.start(runnable);
                }
            } else {
                ThreadFactory namedThreadFactory = (r) -> {
                    Thread thread = new Thread(r, threadName);
                    if (!threadName.isBlank()) {
                        thread.setName(threadName);
                    }
                    return thread;
                };
                Executors.newSingleThreadExecutor(namedThreadFactory).execute(runnable);
            }
        } else {
            runnable.run();
        }
        return this;
    }

    public void stop() {
        running = false;
    }

    public void pause() {
        paused = true;
    }

    public void unpause() {
        paused = false;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isRunOnThread() {
        return runOnThread;
    }

    public boolean isVirtualThread() {
        return virtualThread;
    }
}
