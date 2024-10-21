package entity.player;

import entity.Direction;
import entity.Entity;
import graphics.Images;
import graphics.animation.animations.player.PlayerIdleAnimation;
import graphics.animation.animations.player.PlayerWalkAnimation;

import java.awt.image.BufferedImage;

public class Player extends Entity {
    private BufferedImage playerImage = Images.PLAYER_DEFAULT.image;

    @Override
    public BufferedImage getImage() {
        return playerImage;
    }

    public void setImage(BufferedImage image) {
        this.playerImage = image;
    }

    @Override
    public void onTick() {
        if (isMoving()) {
            playAnimation(new PlayerWalkAnimation(this));
        } else {
            playAnimation(new PlayerIdleAnimation(this));
        }
    }

    @Override
    public void onAdd() {
        width = 38*11;
        height = 28*11;
        playAnimation(new PlayerIdleAnimation(this));
    }

    @Override
    public void onRemove() {}
}
