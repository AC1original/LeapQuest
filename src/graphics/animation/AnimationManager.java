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

    public Animation play(Animation animation) {
        if (animation.isValid()) {
            animations.add(animation);
            gp.register(animation);
            animation.onPlay();
        }
        return animation;
    }

    public void stopByClass(Class<?> clazz) {
        animations.forEach(animation -> {
            if (animation.getClass().equals(clazz)) {
                stop(animation);
            }
        });
    }

    public void stop(Animation animation) {
        if (animations.contains(animation)) {
            animations.remove(animation);
            gp.unregister(animation);
            animation.onStop();
        } else {
            Logger.log("AnimationManager: Failed to stop animation " + animation.getClass().getSimpleName(), true);
        }
    }

    public List<Animation> getAnimations() {
        return List.copyOf(animations);
    }
}
