package main;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import entity.EntityHelper;
import graphics.GameRenderer;
import graphics.animation.Animation;
import graphics.animation.AnimationManager;
import level.LevelManager;
import utils.Logger;
import utils.Ticked;
import utils.caching.Cache;

//TODO: Dev cheat-chat
//TODO: Gamestate management
public final class GamePanel {
	private int gameWidth = 900, gameHeight = 600;
	private static GamePanel instance = null;
	private static final Set<Object> ticked = new HashSet<>();
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
		Logger.log(this.getClass(), "Initialized");
	}
	
	public void run() throws InterruptedException, InvocationTargetException {
		running = true;

		entityHelper.spawn(entityHelper.getPlayer(), 400, 150);

		Thread rendererThread = new Thread(gameRenderer);
		rendererThread.start();

		//Game loop with 50 FPS
		long now;
		long updateTime;
		long wait;
		int TARGET_FPS = 50;
		long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
		while (running) {
			now = System.nanoTime();
			updateTime = System.nanoTime() - now;
			wait = (OPTIMAL_TIME - updateTime) / 1000000;
			tick();
			Thread.sleep(wait);
		}
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
					try {
						method.invoke(object);
					} catch (Exception e) {
						throw new InvocationTargetException(e, String.format("Error while invoking ticked method: %s! Cause: %s", method.getName(), e.getMessage()));
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
