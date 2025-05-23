package main;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

import entity.EntityManager;
import entity.player.Player;
import entity.player.command.CommandExecutor;
import entity.player.command.CommandProviderImpl;
import graphics.GameRenderer;
import graphics.animation.AnimationManager;
import level.LevelManager;
import utils.GameLoop;
import utils.Logger;
import utils.Ticked;

//TODO: Performance issue -> Laggy gamerenderer under 120 FPS (Maybe gameloop issue)
//TODO: Dev cheat-chat
//TODO: Gamestate management
public final class LeapQuest {
	public static final LeapQuest instance;
	private final LocalDateTime startTime = LocalDateTime.now();
	private static final Queue<Object> ticked = new ConcurrentLinkedDeque<>();
	private GameStates gameState = GameStates.MENU;
	private static boolean running = false;
	private AnimationManager animationManager;
	private EntityManager entityHelper;
	private LevelManager levelManager;
	private GameRenderer gameRenderer;
	private CommandExecutor commandExecutor;

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

		gameRenderer = register(new GameRenderer("Leap Quest", 800, 600, 120));
		gameRenderer.initialize();

		animationManager = register(new AnimationManager(gameRenderer));
		entityHelper = register(new EntityManager(gameRenderer, new Player()));
		levelManager = register(new LevelManager(gameRenderer, "/res/level/test_level.txt"));
		commandExecutor = register(new CommandExecutor(System.in, System.out, new CommandProviderImpl()));

		entityHelper.spawn(entityHelper.getPlayer(), 400, 150);

		new GameLoop().start(60, (lastTps) -> {
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
						Logger.warn(object, String.format("Error occurred while trying to invoke method '%s' because it takes too many arguments. Ticked methods aren't allowed to have any arguments! The class of this method has been disabled!", method.getName()));
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
	
	public void stop() {
		//save logic
		running = false;
	}

	public EntityManager getEntityHelper() {
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

	public CommandExecutor getCommandExecutor() {
		return commandExecutor;
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

	public long getRuntime(ChronoUnit unit) {
		return unit.between(startTime, LocalDateTime.now());
	}
}
