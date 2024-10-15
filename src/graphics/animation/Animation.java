package graphics.animation;
import utils.Logger;
import utils.Timed;

import java.awt.*;

public abstract class Animation {
    public static int DEFAULT_ANIMATION_DELAY = 10;
    public abstract AnimationFrame[] getFrames();
    public abstract Point getLocation();
    public abstract int getDelay();
    public abstract boolean drawAnimation();
    public abstract void onFrameUpdate(AnimationFrame newFrame);
    private Graphics g;
    private int counter = 1;
    private int index = 0;

    public boolean isValid() {
        if (getFrames().length == 0) {
            Logger.log("AnimationManager: Failed to start animation. No animation frames.", true);
            return false;
        } else if (getDelay() <= 0) {
            Logger.log("AnimationManager: Failed to start animation. Delay is shorter than 1.", true);
            return false;
        }
        return true;
    }

    public void drawAnimation(Graphics g) {
        this.g = g;
        if (drawAnimation()) g.drawImage(getFrames()[index].getImage(), getLocation().x, getLocation().y, getFrames()[index].getImgWidth(), getFrames()[index].getImgHeight(), null);
    }

    @Timed(delay = 1)
    public void updateIndex() {
        counter++;
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

    public Graphics getGraphics() {
        return g;
    }
}
