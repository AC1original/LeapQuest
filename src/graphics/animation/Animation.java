package graphics.animation;

import graphics.Images;
import utils.Logger;
import utils.Timed;

import java.awt.*;

public abstract class Animation {
    public abstract AnimationFrame[] getFrames();
    public abstract Point getLocation();
    private Graphics graphics;

    @Timed(delay = )
    public void play(Graphics g) {
        this.graphics = g;
        if (getFrames().length == 0) {
            Logger.log("Failed to start animation. No animation frames.", true);
            return;
        }
        for (var frame : getFrames()) {
            g.drawImage(frame.getImage(), getLocation().x, getLocation().y, frame.getImgWidth(), frame.getImgHeight(), null);
        }
    }

}
