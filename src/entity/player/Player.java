package entity.player;

import entity.Direction;
import entity.Entity;
import graphics.Images;
import graphics.animation.animations.player.PlayerIdleLeftAnimation;
import graphics.animation.animations.player.PlayerIdleRightAnimation;
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
    }

    @Override
    public void onAdd() {
        width = 38*11;
        height = 28*11;
        playAnimation(new PlayerIdleRightAnimation(this));
    }

    @Override
    public void setDirection(Direction direction) {
        super.setDirection(direction);
        if (isMoving()) {
            playAnimation(new PlayerWalkAnimation(this));
        } else {
            switch (direction) {
                case RIGHT -> playAnimation(new PlayerIdleRightAnimation(this));
                case LEFT -> playAnimation(new PlayerIdleLeftAnimation(this));
            }
        }
    }

    @Override
    public void onRemove() {}
}
