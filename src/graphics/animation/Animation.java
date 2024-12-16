package graphics.animation;
import main.GamePanel;
import utils.Logger;
import utils.Timed;

import java.awt.*;

public abstract class Animation {
    public abstract AnimationFrame[] getFrames();
    public abstract int getDelay();
    public abstract boolean drawAnimation();
    public abstract void onFrameUpdate(AnimationFrame frame);
    private final GamePanel gp = GamePanel.getInstance();
    private Graphics graphics;
    private int counter = 1;
    private int index = 0;
    private final Point location = new Point(0, 0);

    public boolean checkValidation() {
        if (getDelay() <= 0) {
            Logger.log("AnimationManager: Failed to start animation. Delay is shorter than 1.", true);
            return false;
        }
        return true;
    }

    public void drawAnimation(Graphics g) {
        this.graphics = g;
        if (!drawAnimation()) {
            return;
        }
        g.drawImage(getFrames()[index].getImage(), getLocation().x, getLocation().y, getFrames()[index].getImgWidth(), getFrames()[index].getImgHeight(), null);
    }

    @Timed(delay = 0)
    public void updateIndex() {
        if (isAllowedToUpdate()) counter++;
        if (counter >= getDelay()) {
            counter = 1;
            if (index < getFrames().length-1) {
                index++;
                onFrameUpdate(getFrames()[index]);
            } else {
                index = 0;
            }
        }
    }

    public Point getLocation() {
        return location;
    }

    public Point updateLocation(int x, int y) {
        location.move(x, y);
        return location;
    }

    public final int getAnimationIndex() {
        return index;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public void onPlay() {}

    public void onStop() {}

    public boolean isAllowedToUpdate() {
        return gp.getGameRenderer().getFrame().isFocused();
    }
}
