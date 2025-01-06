package graphics;

import java.awt.*;
import javax.swing.*;

import main.GamePanel;
import main.Main;
import org.jetbrains.annotations.Nullable;
import utils.Logger;

public class GameRenderer extends JPanel implements Runnable {
    private final JFrame frame = new JFrame("Leap Quest");
    private final GamePanel gp;
    private Graphics graphics = null;

    public GameRenderer(GamePanel gp) {
        this.gp = gp;

        frame.setSize(gp.getGameWidth(), gp.getGameHeight());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.addKeyListener(gp.getEntityHelper().getPlayer().getUserKeyboardInput());
        frame.setVisible(true);
        Logger.log("GameRenderer: Initialized");
    }


	@Override
	public void run() {
        long now;
        long updateTime;
        long wait;
        int TARGET_FPS = 60;
        long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        while (GamePanel.isRunning()) {
            now = System.nanoTime();
            updateTime = System.nanoTime() - now;
            wait = (OPTIMAL_TIME - updateTime) / 1000000;
            repaintTick();
            try {
                Thread.sleep(wait);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Logger.log(this.getClass(), "Failed to run rendering thread", true);
	}

    private void repaintTick() {
        if (frame.isFocused()) repaint();
    }
	
	@Override
	protected void paintComponent(Graphics g) {
        if (graphics == null) graphics = g;

		super.paintComponent(g);
        gp.getLevelManager().drawLevel(g);
        gp.getEntityHelper().drawEntities(g);
        gp.getAnimationManager().getAnimations().forEach(animation -> animation.drawAnimation(g));

        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString(Main.getUsedMemory() / (1024 * 1024) + "MB", 20, 20);
    }

    public JFrame getFrame() {
        return frame;
    }

    @Nullable
    public Graphics getUGraphics() {
        return graphics;
    }
}
