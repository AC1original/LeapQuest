package user;

import entity.Direction;
import entity.player.Player;
import main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class UserKeyboardInput implements KeyListener {
    private final Player player;

    public UserKeyboardInput(Player player) {
        this.player = player;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        player.keyPressed(e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.keyReleased(e.getKeyChar());
    }
}
