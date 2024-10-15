package graphics.animation.animations;

import entity.player.Player;
import graphics.ImageLoader;
import graphics.Images;
import graphics.animation.Animation;
import graphics.animation.AnimationFrame;
import utils.Timed;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayerIdleAnimation extends Animation {
    private final Player player;

    public PlayerIdleAnimation(Player player) {
        this.player = player;
    }

    @Override
    public AnimationFrame[] getFrames() {
        BufferedImage img = Images.PLAYER_IDLING_RIGHT.image;
        AnimationFrame[] animationFrames = new AnimationFrame[12];
        for (int i = 0; i<12; i++) {
            animationFrames[i] = AnimationFrame.create(ImageLoader.getSubImage(img, i*(img.getWidth()/12), 0, img.getWidth()/12, img.getHeight()));
        }
        return animationFrames;
    }

    @Override
    public Point getLocation() {
        return new Point(player.getX(), player.getY());
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
