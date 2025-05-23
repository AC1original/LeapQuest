package entity.player;

import entity.Direction;
import entity.Entity;
import graphics.ImageLoader;
import graphics.animation.animations.player.PlayerIdleAnimation;
import graphics.animation.animations.player.PlayerWalkAnimation;
import main.LeapQuest;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public final class Player extends Entity<Player> {
    private final BufferedImage fullImage = ImageLoader.getCachedOrLoad(DEFAULT_PATH + "player/player_idle_right.png");
    private BufferedImage playerImage = fullImage.getSubimage(0, 0, fullImage.getWidth() / 12, fullImage.getHeight());
    private final char[] moveRequests = new char[10];
    private final int LEVEL_MOVE_BUFF = 100;

    @Override
    public BufferedImage getImage() {
        return playerImage;
    }

    @Override
    protected Player onTick() {
        super.onTick();
        for (char c : moveRequests) {
            switch (c) {
                case ' ' -> jump();
                case 'a' -> move(Direction.LEFT);
                case 'd' -> move(Direction.RIGHT);
            }
        }

        //TODO: Too much initializations (See pacman)
        if (hasMoveRequests()) {
            playAnimation(new PlayerWalkAnimation(this));
        } else if (isOnGround()) {
            playAnimation(new PlayerIdleAnimation(this));
        } else {
            stopAnimation();
        }
        return this;
    }

    @Override
    protected Player onSpawn() {
        UserKeyboardInput.instance.addListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                addMoveRequest(UserKeyboardInput.translateArrowKey(e));
            }

            @Override
            public void keyReleased(KeyEvent e) {
                removeMoveRequest(UserKeyboardInput.translateArrowKey(e));
            }
        });
        return null;
    }

    @Override
    public Player move(Direction direction, int speed) {
        var screenWidth = LeapQuest.instance.getGameRenderer().getWidth();

        if ((direction.getDeltaX() > 0 && getX() > screenWidth - LEVEL_MOVE_BUFF) ||
                (direction.getDeltaX() < 0 && getX() < LEVEL_MOVE_BUFF)) {
            moveLevelSafe(direction, speed);
            return super.move(direction, 0);
        }
        return super.move(direction, speed);
    }

    public void moveLevelSafe(Direction direction, int speed) {
        speed = collisionPrediction(direction, speed);

        var lvl = LeapQuest.instance.getLevelManager();
        lvl.moveLevel(-direction.getDeltaX() * speed, -direction.getDeltaY() * speed);
    }


    @Override
    public Player setImage(BufferedImage image) {
        this.playerImage = image;
        return this;
    }

    @Override
    public int getWidth() {
        return 19 * 3;
    }

    @Override
    public int getHeight() {
        return 22 * 3;
    }

    @Override
    protected Player onRemove() {
        return this;
    }

    public void addMoveRequest(char key) {
        if (hasMoveRequest(key)) return;
        for (int i = 0; i < moveRequests.length; i++) {
            if (moveRequests[i] == 0) {
                moveRequests[i] = Character.toLowerCase(key);
                return;
            }
        }


    }

    public void removeMoveRequest(char key) {
        for (int i = 0; i < moveRequests.length; i++) {
            if (moveRequests[i] == key) {
                moveRequests[i] = 0;
            }
        }
    }

    public void clearMoveRequests() {
        Arrays.fill(moveRequests, (char) 0);
    }

    public boolean hasMoveRequest(char key) {
        for (char moveRequest : moveRequests) {
            if (moveRequest == key) {
                return true;
            }
        }
        return false;
    }

    public char[] getMoveRequests() {
        return moveRequests;
    }

    public boolean hasMoveRequests() {
        for (char moveRequest : moveRequests) {
            if (moveRequest != 0) return true;
        }
        return false;
    }
}
