package entity.player;

import entity.Direction;
import entity.Entity;
import graphics.ImageLoader;
import graphics.animation.animations.player.PlayerFallAnimation;
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
        if (isFalling()) {
            playAnimation(new PlayerFallAnimation(this));
            return this;
        }
        if (isMoving()) {
            playAnimation(new PlayerWalkAnimation(this));
        } else {
            playAnimation(new PlayerIdleAnimation(this));
        }
        return this;
    }

    @Timed(delay = 0)
    public final void movementTicks() {
        if (moveRequested == ' ') jump();
        if (moveRequested == 'a') move(Direction.LEFT);
        if (moveRequested == 'd') move(Direction.RIGHT);
    }

    @Override
    public Player onSpawn() {
        width = 19*3;
        height = 22*3;
        return this;
    }

    @Override
    public Player setImage(BufferedImage image) {
        this.playerImage = image;
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

    public void keyPressed(KeyEvent event) {}

    public void keyReleased(KeyEvent event) {
        moveRequested = '0';
    }
}
