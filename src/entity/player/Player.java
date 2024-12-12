package entity.player;

import entity.Direction;
import entity.Entity;
import graphics.ImageLoader;
import graphics.animation.animations.player.PlayerIdleAnimation;
import graphics.animation.animations.player.PlayerWalkAnimation;
import main.GamePanel;
import utils.Timed;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player extends Entity<Player> {
    private final PlayerKeyboardInput userKeyboardInput = GamePanel.register(new PlayerKeyboardInput(this));
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
            case ' ' -> jump();
            case 'a' -> move(Direction.LEFT);
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
        showHitBox(true);
        playAnimation(new PlayerIdleAnimation(this));
        return this;
    }

    @Override
    public Player onRemove() {
        return this;
    }

    public PlayerKeyboardInput getUserKeyboardInput() {
        return userKeyboardInput;
    }

    public void keyTyped(KeyEvent event) {
        moveRequested = event.getKeyChar();
    }

    public void keyReleased(char key) {
        moveRequested = '0';
    }
}
