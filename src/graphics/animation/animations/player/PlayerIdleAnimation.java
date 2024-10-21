package graphics.animation.animations.player;

import entity.Direction;
import entity.player.Player;
import graphics.ImageLoader;
import graphics.Images;
import graphics.animation.Animation;
import graphics.animation.AnimationFrame;

import java.awt.image.BufferedImage;

public class PlayerIdleAnimation extends Animation {
    private final Player player;

    public PlayerIdleAnimation(Player player) {
        this.player = player;
    }

    @Override
    public AnimationFrame[] getFrames() {
        if (player.getDirection().equals(Direction.RIGHT)) {
            return AnimationFrame.create(Images.PLAYER_IDLING_RIGHT.image, 12);
        } else {
            return AnimationFrame.create(Images.PLAYER_IDLING_LEFT.image, 12);
        }
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
    public void onFrameUpdate(AnimationFrame newFrame) {
        player.setImage(newFrame.getImage());
    }

    public Player getPlayer() {
        return player;
    }

}
