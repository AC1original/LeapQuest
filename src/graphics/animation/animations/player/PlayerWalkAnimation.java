package graphics.animation.animations.player;

import entity.Direction;
import entity.Entity;
import entity.player.Player;
import graphics.ImageLoader;
import graphics.animation.Animation;
import graphics.animation.AnimationFrame;

import java.awt.image.BufferedImage;

public class PlayerWalkAnimation extends Animation {
    private final Player player;

    public PlayerWalkAnimation(Player player) {
        this.player = player;
    }

    @Override
    public AnimationFrame[] getFrames() {
        BufferedImage walk = ImageLoader.getCachedOrLoad(Entity.DEFAULT_PATH + "player/player_walk_left.png", "player_walk_left");
        if (player.getDirection().equals(Direction.RIGHT)) {
            walk = ImageLoader.getCachedOrLoad(Entity.DEFAULT_PATH + "player/player_walk_right.png", "player_walk_right");
        }
        return AnimationFrame.create(walk, 6);
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
