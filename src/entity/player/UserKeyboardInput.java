package entity.player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UserKeyboardInput {
    public static final UserKeyboardInput instance;
    private final Set<KeyListener> listeners = Collections.synchronizedSet(new HashSet<>());

    static {
        instance = new UserKeyboardInput();
    }

    private UserKeyboardInput() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                final KeyEvent ev = (KeyEvent) event;
                switch (ev.getID()) {
                    case KeyEvent.KEY_PRESSED -> listeners.forEach(listener -> listener.keyPressed(ev));
                    case KeyEvent.KEY_RELEASED -> listeners.forEach(listener -> listener.keyReleased(ev));
                    case KeyEvent.KEY_TYPED -> listeners.forEach(listener -> listener.keyTyped(ev));
                }
            }
        }, AWTEvent.KEY_EVENT_MASK);
    }

    public void addListener(@NotNull KeyListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(@NotNull KeyListener listener) {
        this.listeners.add(listener);
    }

    public static char translateArrowKey(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> {
                return ' ';
            }
            case KeyEvent.VK_LEFT -> {
                return 'a';
            }
            case KeyEvent.VK_RIGHT -> {
                return 'd';
            }
            case KeyEvent.VK_DOWN -> {
                return 's';
            }
            default -> {
                return e.getKeyChar();
            }
        }
    }
}
