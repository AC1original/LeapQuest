package graphics;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.swing.*;

import main.LeapQuest;
import org.jetbrains.annotations.Nullable;
import utils.GameLoop;
import utils.Logger;

public class GameRenderer extends JPanel {
    private final JFrame frame;
    private Graphics graphics = null;
    private final LeapQuest gp;
    private GameLoop loop;
    private final int fps;
    private int currentFps;
    private final List<Drawable> drawables = Collections.synchronizedList(new ArrayList<>());
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    /*
     * ReadWriteLock:
     * Read Lock = Prevent writing
     * Write Lock = Prevent writing AND reading
     */

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

        Logger.info(this, "Initialized.");

        loop = new GameLoop()
                .runOnThread(true)
                .setThreadName("Render-Thread")
                .start(fps, (fps) -> {
                    currentFps = fps;
                    SwingUtilities.invokeLater(this::repaint);
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
        lock.writeLock().lock();
        try {
            drawables.sort(Comparator.comparing(Drawable::priority));
            drawables.stream()
                    .filter(drawable -> drawable.layer() >= 0)
                    .forEach(drawable -> drawables.add(drawable.layer(), drawable));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graphics == null) graphics = g;

        if (gp == null || !LeapQuest.isRunning()) return;

        try {
            lock.readLock().lock();
            try {
                drawables.stream()
                        .filter(Drawable::visible)
                        .forEach(drawable -> drawable.fDraw(g));
            } finally {
                lock.readLock().unlock();
            }
        } catch (Exception ex) {
            loop.stop();
            Logger.error(this, "Failed to repaint! Threw exception: " + ex);
        }
        Toolkit.getDefaultToolkit().sync();
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
