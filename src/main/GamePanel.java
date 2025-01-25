package main;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

import entity.EntityHelper;
import graphics.GameRenderer;
import graphics.animation.AnimationManager;
import level.LevelManager;
import utils.GameLoop;
import utils.Logger;
import utils.Ticked;

//TODO: Dev cheat-chat
//TODO: Gamestate management
public final class GamePanel {
	private int gameWidth = 900, gameHeight = 600;
	private static GamePanel instance = null;
	private static final Queue<Object> ticked = new ConcurrentLinkedDeque<>();
	private GameStates gameState = GameStates.MENU;
	private static boolean running = false;
	private final AnimationManager animationManager = register(new AnimationManager());
	private final EntityHelper entityHelper = register(new EntityHelper());
	private final LevelManager levelManager = register(new LevelManager(this, "/res/level/test_level.txt"));
	private final GameRenderer gameRenderer =  new GameRenderer(this);

	public static GamePanel getInstance() {
		if (instance == null) {
			instance = new GamePanel();
		}
		return instance;
	}

	private GamePanel() {
		Logger.info(this, "Initialized.");
	}
	
	public void run() {
		running = true;

		entityHelper.spawn(entityHelper.getPlayer(), 400, 150);

		Thread rendererThread = new Thread(gameRenderer);
		rendererThread.start();

		new GameLoop().start(50, (lastFPS) -> {
            try {
                tick();
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
	}

	public static<T> T register(T clazz) {
		ticked.add(clazz);
		return clazz;
	}

	public static<T> void unregister(T clazz) {
		ticked.remove(clazz);
	}

	public void tick() throws InvocationTargetException {
		for (Object object : ticked) {
			for (Method method : object.getClass().getMethods()) {
				if (method.isAnnotationPresent(Ticked.class)) {
					if (method.getParameterCount() > 0) {
						Logger.warn(object, String.format("Error occurred while trying to invoke method '%s' because it takes too many arguments. Ticked methods aren't allowed to have any arguments! The method has been disabled!", method.getName()));
						unregister(object);
					} else {
						try {
							method.invoke(object);
						} catch (Exception e) {
							throw new InvocationTargetException(e, String.format("Error while invoking ticked method: %s! Cause: %s", method.getName(), e));
						}
					}

				}
			}
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
