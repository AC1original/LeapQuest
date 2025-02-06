package graphics.animation;
import main.LeapQuest;

import java.awt.*;

public abstract class Animation {
    public abstract AnimationFrame[] getFrames();
    public abstract int getDelay();
    public abstract boolean drawAnimation();
    protected abstract void onFrameUpdate(AnimationFrame frame);
    private final LeapQuest gp = LeapQuest.instance;
    private Graphics graphics;
    private int counter = 1;
    private int index = 0;
    private final Point location = new Point(0, 0);


    public boolean checkValidation() {
        return getFrames().length > 0;
    }

    public void drawAnimation(Graphics g) {
        this.graphics = g;
        if (!drawAnimation() || !checkValidation()) {
            return;
        }
        g.drawImage(getFrames()[index].getImage(), getLocation().x, getLocation().y, getFrames()[index].getImgWidth(), getFrames()[index].getImgHeight(), null);
    }

    protected void updateIndex() {
        if (getDelay() > 0 && checkValidation()) {
            if (isAllowedToUpdate()) counter++;
            if (counter >= getDelay()) {
                counter = 1;
                if (index < getFrames().length - 1) {
                    index++;
                    onFrameUpdate(getFrames()[index]);
                } else {
                    index = 0;
                }
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

    public final void jumpIndex(int index) {
        this.index = index;
    }

    public final void reset() {
        this.index = 0;
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
