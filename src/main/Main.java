package main;
import utils.Logger;

//TODO: GameLoop helper class
public class Main {
	
	public static void main(String[] args) {
		Logger.info(Main.class, "Starting Game Loop.");
		GamePanel.getInstance().run();
		Logger.error(Main.class, "Game Loop failed!");
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
