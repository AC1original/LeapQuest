package graphics;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.swing.*;

import entity.Entity;
import main.LeapQuest;
import org.jetbrains.annotations.Nullable;
import utils.GameLoop;
import utils.Logger;

//TODO: Drawable interface and support
public class GameRenderer extends JPanel {
    private final JFrame frame;
    private Graphics graphics = null;
    private final LeapQuest gp;
    private GameLoop loop;
    private final int fps;
    private int currentFps;
    private final List<Drawable> drawables = Collections.synchronizedList(new ArrayList<>());
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public GameRenderer(String title, int width, int height, int fps) {
        this.fps = fps;
        this.gp = LeapQuest.instance;

        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void initialize() {
        frame.add(this);
        frame.setVisible(true);

        Executors.newSingleThreadExecutor(r -> {
            Thread renderThread = new Thread(r);
            renderThread.setName("Render-Thread");
            return renderThread;
        }).execute(() -> {
            Logger.info(this, "Initialized.");

            loop = new GameLoop().start(fps, (fps) -> {
                currentFps = fps;
                this.repaint();
            });
        });
    }

    public void addDrawable(Drawable drawable) {
        lock.writeLock().lock();
        try {
            this.drawables.add(drawable);
            sortDrawables();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeDrawable(Drawable drawable) {
        lock.writeLock().lock();
        try {
            this.drawables.remove(drawable);
            sortDrawables();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void sortDrawables() {
        drawables.sort(Comparator.comparing(Drawable::priority));
        drawables.stream()
                .filter(drawable -> drawable.layer() >= 0)
                .forEach(drawable -> drawables.add(drawable.layer(), drawable));
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (graphics == null) graphics = g;

        if (gp == null || !LeapQuest.isRunning()) return;

        super.paintComponent(g);

        try {
            lock.readLock().lock();
            try {
                drawables.stream()
                        .filter(Drawable::visible)
                        .forEach(drawable -> drawable.freeDraw(g));
            } finally {
                lock.readLock().unlock();
            }
        } catch (Exception ex) {
            loop.stop();
            Logger.error(this, "Failed to repaint! Threw exception: " + ex);
        }
    }

    public JFrame getFrame() {
        return frame;
    }

    @Nullable
    public Graphics getUGraphics() {
        return graphics;
    }

    public int getCurrentFps() {
        return currentFps;
    }

    public int getTargetFps() {
        return fps;
    }
}
