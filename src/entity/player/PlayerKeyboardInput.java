package entity.player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayerKeyboardInput implements KeyListener {
    private final Player player;

    public PlayerKeyboardInput(Player player) {
        this.player = player;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        player.keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        player.keyReleased(e.getKeyChar());
    }
}
