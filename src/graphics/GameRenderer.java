package graphics;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import javax.swing.*;

import main.LeapQuest;
import org.jetbrains.annotations.Nullable;
import utils.Logger;

//TODO: Drawable interface and support
public class GameRenderer extends JPanel {
    private final JFrame frame;
    private Graphics graphics = null;
    private final LeapQuest gp;
    private Timer timer;
    private int fps;

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

        timer = new Timer(1000 / fps, e -> repaint());
        timer.start();
    }

    public void setKeyListener(KeyListener keyListener) {
        frame.addKeyListener(keyListener);
    }

    public void setMouseListener(MouseListener mouseListener) {
        frame.addMouseListener(mouseListener);
    }
	
	@Override
    protected void paintComponent(Graphics g) {
        if (graphics == null) graphics = g;

        if (gp == null || !LeapQuest.isRunning()) return;

		super.paintComponent(g);

        try {
            gp.getLevelManager().drawLevel(g);
            gp.getEntityHelper().drawEntities(g);
            gp.getAnimationManager().getAnimations().forEach(animation -> animation.drawAnimation(g));

            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString(LeapQuest.getUsedMemory() / (1024 * 1024) + "MB", 20, 20);
        } catch (Exception ex) {
            timer.stop();
            Logger.error(this, "Failed to repaint! Threw exception: " + ex);
        }
    }

    public JFrame getFrame() {
        return frame;
    }

    public void resizeFrame(int width, int height) {
        frame.setSize(width, height);
    }

    @Nullable
    public Graphics getUGraphics() {
        return graphics;
    }

    public void setTargetFps(int fps) {
        this.fps = fps;
    }

    public int getTargetFps() {
        return fps;
    }
}
