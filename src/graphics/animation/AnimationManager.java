package graphics.animation;

import main.GamePanel;
import utils.Logger;
import utils.Ticked;

import java.util.ArrayList;
import java.util.List;

//TODO: Animation caching (Remove GamePanel line 79)
//TODO: Animations on extra thread
public final class AnimationManager {
    private final List<Animation> animations = new ArrayList<>();

    public synchronized Animation play(Animation animation) {
        if (animation.checkValidation()) {
            animations.add(animation);
            animation.onPlay();
        }
        return animation;
    }

    public synchronized void stopByClass(Class<?> clazz) {
        Logger.info(clazz, "Stopping animations.");
        animations.stream()
                .filter(animation -> animation.getClass().equals(clazz))
                .forEach(this::stop);
    }

    public synchronized void stop(Animation animation) {
         if (animations.contains(animation)) {
            animations.remove(animation);
            animation.onStop();
        }
    }

    @Ticked
    public void tick() {
        animations.forEach(Animation::updateIndex);
    }

    public List<Animation> getAnimations() {
        return List.copyOf(animations);
    }
}
