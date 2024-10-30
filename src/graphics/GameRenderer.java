package graphics;

import java.awt.*;
import javax.swing.*;

import main.GamePanel;
import main.Main;
import utils.Logger;

public class GameRenderer extends JPanel implements Runnable {
    private final JFrame frame = new JFrame("Projektarbeit 2BKI2");
    private final GamePanel gp;

    public GameRenderer(GamePanel gp) {
        this.gp = gp;

        frame.setSize(800, 600);
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
	}

    private void repaintTick() {
        if (frame.isFocused()) repaint();
    }
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
        gp.getLevelManager().drawLevel(g);
        gp.getEntityHelper().drawEntities(g);
        gp.getAnimationManager().getAnimations().forEach(animation -> animation.drawAnimation(g));
    }

    public JFrame getFrame() {
        return frame;
    }
}
