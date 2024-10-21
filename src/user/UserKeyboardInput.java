package user;

import entity.Direction;
import main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class UserKeyboardInput implements KeyListener {
    private final GamePanel gp;

    public UserKeyboardInput(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        char key = e.getKeyChar();
        switch (key) {
            case 'w' -> gp.getPlayer().move(Direction.UP);
            case 'a' -> gp.getPlayer().move(Direction.LEFT);
            case 's' -> gp.getPlayer().move(Direction.DOWN);
            case 'd' -> gp.getPlayer().move(Direction.RIGHT);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        gp.getPlayer().setMoving(false);
    }
}
