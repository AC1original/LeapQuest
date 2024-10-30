package entity.player;

import entity.Entity;
import graphics.ImageLoader;
import graphics.animation.animations.player.PlayerIdleAnimation;
import graphics.animation.animations.player.PlayerWalkAnimation;
import main.GamePanel;
import user.UserKeyboardInput;

import java.awt.image.BufferedImage;

public class Player extends Entity {
    private final UserKeyboardInput userKeyboardInput = GamePanel.register(new UserKeyboardInput(this));
    private final BufferedImage fullImage = ImageLoader.getAutomated(DEFAULT_PATH + "player/player_idle_right.png", "player_idle_right");
    private BufferedImage playerImage = fullImage.getSubimage(0, 0, fullImage.getWidth()/12, fullImage.getHeight());

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
        speed = 5;
        playAnimation(new PlayerIdleAnimation(this));
    }

    @Override
    public void onRemove() {}

    public UserKeyboardInput getUserKeyboardInput() {
        return userKeyboardInput;
    }
}
