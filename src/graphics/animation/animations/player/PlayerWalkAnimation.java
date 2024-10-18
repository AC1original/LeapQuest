package graphics.animation.animations.player;

import entity.Direction;
import entity.player.Player;
import graphics.ImageLoader;
import graphics.Images;
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
        BufferedImage walk = Images.PLAYER_WALK_LEFT.image;
        if (player.getDirection().equals(Direction.RIGHT)) {
            walk = Images.PLAYER_WALK_RIGHT.image;
        }
        AnimationFrame[] animationFrames = new AnimationFrame[6];
        for (int i = 0; i<6; i++) {
            animationFrames[i] = AnimationFrame.create(ImageLoader.getSubImage(walk, i*(walk.getWidth()/6), 0, walk.getWidth()/6, walk.getHeight()));
        }
        return animationFrames;
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
