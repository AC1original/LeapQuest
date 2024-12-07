package entity.player;

import entity.Direction;
import entity.Entity;
import graphics.ImageLoader;
import graphics.animation.animations.player.PlayerIdleAnimation;
import graphics.animation.animations.player.PlayerWalkAnimation;
import main.GamePanel;
import user.UserKeyboardInput;
import utils.Timed;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player extends Entity<Player> {
    private final UserKeyboardInput userKeyboardInput = GamePanel.register(new UserKeyboardInput(this));
    private final BufferedImage fullImage = ImageLoader.getCachedOrLoad(DEFAULT_PATH + "player/player_idle_right.png", "player_idle_right");
    private BufferedImage playerImage = fullImage.getSubimage(0, 0, fullImage.getWidth()/12, fullImage.getHeight());
    private char moveRequested = '0';

    @Override
    public BufferedImage getImage() {
        return playerImage;
    }

    @Override
    public Player onTick() {
        super.onTick();
        if (isMoving()) {
            playAnimation(new PlayerWalkAnimation(this));
        } else {
            playAnimation(new PlayerIdleAnimation(this));
        }
        return this;
    }

    @Timed(delay = 50)
    public final void movementTicks() {
        switch (moveRequested) {
            case 'w' -> move(Direction.UP);
            case 'a' -> move(Direction.LEFT);
            case 's' -> move(Direction.DOWN);
            case 'd' -> move(Direction.RIGHT);
        }
    }

    public void setImage(BufferedImage image) {
        this.playerImage = image;
    }

    @Override
    public Player onSpawn() {
        width = 19*3;
        height = 22*3;
        playAnimation(new PlayerIdleAnimation(this));

        showHitBox(true);
        return this;
    }

    @Override
    public Player onRemove() {
        return this;
    }

    public UserKeyboardInput getUserKeyboardInput() {
        return userKeyboardInput;
    }

    public void keyTyped(KeyEvent event) {
        moveRequested = event.getKeyChar();
    }

    public void keyReleased(char key) {
        moveRequested = '0';
    }
}
