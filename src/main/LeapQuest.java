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
public final class LeapQuest {
	public static final LeapQuest instance;
	private static final Queue<Object> ticked = new ConcurrentLinkedDeque<>();
	private GameStates gameState = GameStates.MENU;
	private static boolean running = false;
	private AnimationManager animationManager;
	private EntityHelper entityHelper;
	private LevelManager levelManager;
	private GameRenderer gameRenderer;

	static {
		instance = new LeapQuest();
	}

	public static void main(String[] args) {
		Logger.info(LeapQuest.class, "Starting Game.");
		instance.run();
		Logger.error(LeapQuest.class, "Main Game Loop failed!");
	}

	private LeapQuest() {
		Logger.info(this, "Initialized.");
	}
	
	public void run() {
		running = true;

		animationManager = register(new AnimationManager());
		entityHelper = register(new EntityHelper());
		levelManager = register(new LevelManager(this, "/res/level/test_level.txt"));
		gameRenderer = register(new GameRenderer("Leap Quest", 800, 600, 60));

		entityHelper.spawn(entityHelper.getPlayer(), 400, 150);
		gameRenderer.initialize();

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

	private void tick() throws InvocationTargetException {
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

	public static long getMaxMemory() {
		return Runtime.getRuntime().maxMemory();
	}

	public static long getUsedMemory() {
		return getTotalMemory() - getFreeMemory();
	}

	public static long getTotalMemory() {
		return Runtime.getRuntime().totalMemory();
	}

	public static long getFreeMemory() {
		return Runtime.getRuntime().freeMemory();
	}
}
