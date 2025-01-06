package entity.player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public record PlayerKeyboardInput(Player player) implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        player.addMoveRequest(translateArrowKey(e));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.removeMoveRequest(translateArrowKey(e));
    }

    private char translateArrowKey(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            return ' ';
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            return 'a';
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            return 'd';
        }
        else {
            return e.getKeyChar();
        }
    }
}
