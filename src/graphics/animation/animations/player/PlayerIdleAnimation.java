package graphics.animation.animations.player;

import entity.Direction;
import entity.Entity;
import entity.player.Player;
import graphics.ImageLoader;
import graphics.animation.Animation;
import graphics.animation.AnimationFrame;

public class PlayerIdleAnimation extends Animation {
    private final Player player;

    public PlayerIdleAnimation(Player player) {
        this.player = player;
    }

    @Override
    public AnimationFrame[] getFrames() {
        if (player.getDirection().equals(Direction.RIGHT)) {
            return AnimationFrame.create(ImageLoader.getCachedOrLoad(Entity.DEFAULT_PATH + "player/player_idle_right.png", "player_idle_right"), 12);
        } else {
            return AnimationFrame.create(ImageLoader.getCachedOrLoad(Entity.DEFAULT_PATH + "player/player_idle_left.png", "player_idle_left"), 12);
        }
    }

    @Override
    public int getDelay() {
        return 20;
    }

    @Override
    public boolean drawAnimation() {
        return false;
    }

    @Override
    public void onFrameUpdate(AnimationFrame newFrame) {
        player.setImage(newFrame.getImage());
    }

    public Player getPlayer() {
        return player;
    }

}
