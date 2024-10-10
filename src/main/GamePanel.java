package main;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.EntityHelper;
import entity.player.Player;
import graphics.GameRenderer;
import utils.Logger;
import utils.Timed;

public class GamePanel {
	private static final List<Object> registered = new ArrayList<>();
	private static final Map<Method, Long> timed = new HashMap<>();
	private GameStates gameState = GameStates.MENU;
	private static boolean running = false;
	private final GameRenderer gr = register(new GameRenderer(this));
	private final Player player = register(new Player());
	private final EntityHelper entityHelper = register(new EntityHelper());

	
	public void run() throws InterruptedException {
		register(this);
		running = true;
		gr.run();
        while (running) {
			Thread.sleep(10);
        	tick();
        }
	}

	public void tick() {
			timed.forEach((method, timeStamp) -> {
				if (System.currentTimeMillis() - timeStamp >= method.getAnnotation(Timed.class).delay()) {
					timed.put(method, System.currentTimeMillis());
                    try {
						Object instance = registered.stream()
								.filter(obj -> method.getDeclaringClass().isInstance(obj))
								.findFirst()
								.orElse(null);
                        method.invoke(instance);
                    } catch (ReflectiveOperationException e) {
                        Logger.log("GamePanel: Failed to tick \"Timed\" annotation", true);
                    }
                }
			});
	}
	
	public<T> T register(T clazz) {
		registered.add(clazz);
		Logger.log("GamePanel: Registered class " + clazz.getClass().getSimpleName());

		for (Method methods : clazz.getClass().getMethods()) {
			if (methods.isAnnotationPresent(Timed.class)) {
				timed.put(methods, System.currentTimeMillis());
			}
		}
		return clazz;
	}
	
	public void setGameState(GameStates gameState) {
		this.gameState = gameState;
	}
	public GameStates getGameState() {
		return gameState;
	}
	
	public void forceStop() {
		//save logic
		running = false;
	}

	public Player getPlayer() {
		return player;
	}

	public EntityHelper getEntityHelper() {
		return entityHelper;
	}

	public static boolean isRunning() {
		return running;
	}
}
