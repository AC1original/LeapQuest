package graphics.animation.animations.player;

import entity.Direction;
import entity.Entity;
import entity.player.Player;
import graphics.ImageLoader;
import graphics.animation.Animation;
import graphics.animation.AnimationFrame;

public class PlayerIdleAnimation extends Animation {
    private final Player player;
    private final AnimationFrame[] animationRight;
    private final AnimationFrame[] animationLeft;

    public PlayerIdleAnimation(Player player) {
        this.player = player;

        animationRight = AnimationFrame.create(ImageLoader.getCachedOrLoad(Entity.DEFAULT_PATH + "player/player_idle_right.png", "player_idle_right"), 12);
        animationLeft = AnimationFrame.create(ImageLoader.getCachedOrLoad(Entity.DEFAULT_PATH + "player/player_idle_left.png", "player_idle_left"), 12);
    }

    @Override
    public AnimationFrame[] getFrames() {
        if (player.getLookDirection().equals(Direction.LEFT)) {
            return animationLeft;
        }
        return animationRight;
    }

    @Override
    public int getDelay() {
        return 10;
    }

    @Override
    public boolean drawAnimation() {
        return true;
    }

    @Override
    protected void onFrameUpdate(AnimationFrame newFrame) {
        player.setImage(newFrame.getImage());
    }

    public Player getPlayer() {
        return player;
    }
}
