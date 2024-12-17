package graphics.animation;

import main.GamePanel;
import utils.Logger;

import java.util.ArrayList;
import java.util.List;

//TODO: Animation caching (Remove GamePanel line 79)
public class AnimationManager {
    private final List<Animation> animations = new ArrayList<>();

    public Animation play(Animation animation) {
        if (animation.checkValidation()) {
            animations.add(animation);
            GamePanel.register(animation);
            animation.onPlay();
        }
        return animation;
    }

    public void stopByClass(Class<?> clazz) {
        Logger.log(this.getClass(), "Stopping all animations from class: " + clazz.getSimpleName());
        animations.stream()
                .filter(animation -> animation.getClass().equals(clazz))
                .forEach(this::stop);
    }

    public void stop(Animation animation) {
        if (animations.contains(animation)) {
            animations.remove(animation);
            GamePanel.unregister(animation);
            animation.onStop();
        } else {
            Logger.log("AnimationManager: Failed to stop animation " + animation.getClass().getSimpleName(), true);
        }
    }

    public List<Animation> getAnimations() {
        return List.copyOf(animations);
    }
}
