package graphics.animation;

import main.GamePanel;
import utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class AnimationManager {
    private final GamePanel gp;
    private final List<Animation> animations = new ArrayList<>();

    public AnimationManager(GamePanel gp){
        this.gp = gp;
    }

    public void play(Animation animation) {
        if (animation.isValid()) {
            Logger.log("AnimationManager: Playing animation " + animation.getClass().getSimpleName());
            animations.add(animation);
            gp.register(animation);
        }
    }

    public void stop(Animation animation) {
        if (animations.contains(animation)) {
            Logger.log("AnimationManager: Stopped animation " + animation.getClass().getSimpleName());
            animations.remove(animation);
            gp.unregister(animation);
        }
        Logger.log("AnimationManager: Failed to stop animation " + animation.getClass().getSimpleName(), true);
    }

    public List<Animation> getAnimations() {
        return List.copyOf(animations);
    }
}
