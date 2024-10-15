package entity.player;

import entity.Entity;
import graphics.Images;
import graphics.animation.animations.PlayerIdleAnimation;

import java.awt.image.BufferedImage;

public class Player extends Entity {
    private BufferedImage playerImage = Images.EMPTY.image;

    @Override
    public BufferedImage getImage() {
        return playerImage;
    }

    public void setImage(BufferedImage image) {
        this.playerImage = image;
    }

    @Override
    public void onTick() {
    }

    @Override
    public void onAdd() {
        width = 38*5;
        height = 28*5;
        getGamePanel().getAnimationManager().play(new PlayerIdleAnimation(this));
    }

    @Override
    public void onRemove() {
    }
}
