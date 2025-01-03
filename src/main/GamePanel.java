package main;
import java.lang.reflect.Method;
import java.util.*;

import entity.EntityHelper;
import graphics.GameRenderer;
import graphics.animation.Animation;
import graphics.animation.AnimationManager;
import level.LevelManager;
import utils.Logger;
import utils.Timed;

//TODO: Dev cheat-chat
//TODO: Gamestate management
//TODO: Timed annotation thread
//TODO: Timed annotation cant tick error (Maybe cached class)
public final class GamePanel {
	private int gameWidth = 900, gameHeight = 600;
	private static GamePanel instance = null;
	private static final List<Object> registered = new ArrayList<>();
	private static final Map<Method, Long> timed = new HashMap<>();
	private GameStates gameState = GameStates.MENU;
	private static boolean running = false;
	private final AnimationManager animationManager = register(new AnimationManager());
	private final EntityHelper entityHelper = register(new EntityHelper());
	private final LevelManager levelManager = register(new LevelManager(this, "/res/level/test_level.txt"));
	private final GameRenderer gameRenderer = register(new GameRenderer(this));

	public static GamePanel getInstance() {
		if (instance == null) {
			instance = new GamePanel();
		}
		return instance;
	}

	private GamePanel() {
		Logger.log(this.getClass(), "Initialized");
	}
	
	public void run() throws InterruptedException {
		register(this);
		running = true;

		entityHelper.spawn(entityHelper.getPlayer(), 400, 150);

		Thread rendererThread = new Thread(gameRenderer);
		rendererThread.start();

		//Game loop with 100 FPS
		long now;
		long updateTime;
		long wait;
		int TARGET_FPS = 100;
		long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
		while (running) {
			now = System.nanoTime();
			updateTime = System.nanoTime() - now;
			wait = (OPTIMAL_TIME - updateTime) / 1000000;
			tick();
			Thread.sleep(wait);
		}
	}

	public void tick() {
		entityHelper.tick();
		levelManager.tick();
			timed.forEach((method, timeStamp) -> {
				if (method == null) return;
				if ((System.currentTimeMillis() - timeStamp >= method.getAnnotation(Timed.class).delay()) || method.getAnnotation(Timed.class).delay() < 1) {
                    try {
						Object instance = registered.stream()
								.filter(obj -> method.getDeclaringClass().isInstance(obj))
								.findFirst()
								.orElse(null);
						if (instance == null) return;
                        method.invoke(instance);
						timed.replace(method, System.currentTimeMillis());
                    } catch (ReflectiveOperationException e) {
                        Logger.log("GamePanel: Failed to tick \"Timed\" annotation. Cause: " + e.getMessage(), true);
                    }
                }
			});
	}
	
	public static <T> T register(T clazz) {
		if (!registered.contains(clazz)) {
			registered.add(clazz);
			if (!clazz.getClass().getSuperclass().equals(Animation.class))
				Logger.log("GamePanel: Registered class " + clazz.getClass().getSimpleName());
		}

		for (Method methods : clazz.getClass().getMethods()) {
			if (methods.isAnnotationPresent(Timed.class)) {
				timed.put(methods, System.currentTimeMillis());
			}
		}
		return clazz;
	}

	public static <T> void unregister(T clazz) {
		timed.forEach((K, V) -> {
			if (K.getDeclaringClass().equals(clazz.getClass())) {
				timed.remove(K);
			}
		});
		if (registered.contains(clazz)) {
			registered.remove(clazz);
			if (!clazz.getClass().getSuperclass().equals(Animation.class))
				Logger.log("GamePanel: Unregistered class " + clazz.getClass().getSimpleName());
		}
	}

	public void setGameWidth(int gameWidth) {
		this.gameWidth = gameWidth;
	}

	public void setGameHeight(int gameHeight) {
		this.gameHeight = gameHeight;
	}

	public int getGameWidth() {
		return gameWidth;
	}

	public int getGameHeight() {
		return gameHeight;
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

	public EntityHelper getEntityHelper() {
		return entityHelper;
	}

	public static boolean isRunning() {
		return running;
	}

	public AnimationManager getAnimationManager() {
		return animationManager;
	}

	public GameRenderer getGameRenderer() {
		return gameRenderer;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}
}
