package entity.player;

import entity.Direction;
import entity.Entity;
import graphics.ImageLoader;
import graphics.animation.animations.player.PlayerIdleAnimation;
import graphics.animation.animations.player.PlayerWalkAnimation;
import main.GamePanel;
import user.UserKeyboardInput;

import java.awt.image.BufferedImage;

public class Player extends Entity<Player> {
    private final UserKeyboardInput userKeyboardInput = GamePanel.register(new UserKeyboardInput(this));
    private final BufferedImage fullImage = ImageLoader.getAutomated(DEFAULT_PATH + "player/player_idle_right.png", "player_idle_right");
    private BufferedImage playerImage = fullImage.getSubimage(0, 0, fullImage.getWidth()/12, fullImage.getHeight());

    public Player(GamePanel gp) {
        super(gp);
    }

    @Override
    public BufferedImage getImage() {
        return playerImage;
    }

    @Override
    public Player onTick() {
        if (isMoving()) {
            playAnimation(new PlayerWalkAnimation(this));
        } else {
            playAnimation(new PlayerIdleAnimation(this));
        }
        return this;
    }

    public void setImage(BufferedImage image) {
        this.playerImage = image;
    }

    @Override
    public Player onAdd() {
        width = 38*11;
        height = 28*11;
        speed = 5;
        playAnimation(new PlayerIdleAnimation(this));
        return this;
    }


    @Override
    public Player onRemove() {
        return this;
    }

    public UserKeyboardInput getUserKeyboardInput() {
        return userKeyboardInput;
    }

    public void keyPressed(char key) {
        switch (key) {
            case 'w' -> move(Direction.UP);
            case 'a' -> move(Direction.LEFT);
            case 's' -> move(Direction.DOWN);
            case 'd' -> move(Direction.RIGHT);
        }
    }

    public void keyReleased(char key) {
        setMoving(false);
    }
}
