package graphics.animation.animations.player;

import entity.Direction;
import entity.Entity;
import entity.player.Player;
import graphics.ImageLoader;
import graphics.animation.Animation;
import graphics.animation.AnimationFrame;

public class PlayerWalkAnimation extends Animation {
    private final Player player;
    private final AnimationFrame[] walkLeft = AnimationFrame.create(ImageLoader.getCachedOrLoad(Entity.DEFAULT_PATH + "player/player_walk_left.png", "player_walk_left"), 6);
    private final AnimationFrame[] walkRight = AnimationFrame.create(ImageLoader.getCachedOrLoad(Entity.DEFAULT_PATH + "player/player_walk_right.png", "player_walk_right"), 6);

    public PlayerWalkAnimation(Player player) {
        this.player = player;
    }

    @Override
    public AnimationFrame[] getFrames() {
        if (player.getLookDirection().equals(Direction.RIGHT)) {
            return walkRight;
        } else if (player.getLookDirection().equals(Direction.LEFT)) {
            return walkLeft;
        }
        return AnimationFrame.createEmpty();
    }

    @Override
    public int getDelay() {
        return 12;
    }

    @Override
    public boolean drawAnimation() {
        return false;
    }

    @Override
    public void onFrameUpdate(AnimationFrame frame) {
        player.setImage(frame.getImage());
    }

    public Player getPlayer() {
        return player;
    }

}
