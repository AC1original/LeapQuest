package graphics.animation.animations.player;

import entity.player.Player;
import graphics.ImageLoader;
import graphics.Images;
import graphics.animation.AnimationFrame;

import java.awt.image.BufferedImage;

public class PlayerIdleLeftAnimation extends PlayerIdleRightAnimation {
    public PlayerIdleLeftAnimation(Player player) {
        super(player);
    }

    @Override
    public AnimationFrame[] getFrames() {
        BufferedImage img = Images.PLAYER_IDLING_LEFT.image;
        AnimationFrame[] animationFrames = new AnimationFrame[12];
        for (int i = 0; i<12; i++) {
            animationFrames[i] = AnimationFrame.create(ImageLoader.getSubImage(img, i*(img.getWidth()/12), 0, img.getWidth()/12, img.getHeight()));
        }
        return animationFrames;
    }
}
