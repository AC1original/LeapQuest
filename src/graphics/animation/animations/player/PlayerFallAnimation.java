package graphics.animation.animations.player;

import entity.Direction;
import entity.Entity;
import entity.player.Player;
import graphics.ImageLoader;
import graphics.animation.Animation;
import graphics.animation.AnimationFrame;

public class PlayerFallAnimation extends Animation {
    private final Player player;
    private final AnimationFrame[] jumpRight = {AnimationFrame.create(ImageLoader.getCachedOrLoad(Entity.DEFAULT_PATH + "player/player_fall_right.png", "player_fall_right"))};
    private final AnimationFrame[] jumpLeft = {AnimationFrame.create(ImageLoader.getCachedOrLoad(Entity.DEFAULT_PATH + "player/player_fall_left.png", "player_fall_left"))};

    public PlayerFallAnimation(Player player) {
        this.player = player;
    }

    @Override
    public AnimationFrame[] getFrames() {
        if (player.getLookDirection().equals(Direction.RIGHT)) {
            return jumpRight;
        } else if (player.getLookDirection().equals(Direction.LEFT)) {
            return jumpLeft;
        }
        return AnimationFrame.createEmpty();
    }

    @Override
    public int getDelay() {
        return 0;
    }

    @Override
    public boolean drawAnimation() {
        return false;
    }

    @Override
    public void onFrameUpdate(AnimationFrame frame) {
        player.setImage(frame.getImage());
    }
}
