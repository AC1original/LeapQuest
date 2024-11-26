package main;
import utils.Logger;

public class Main {
	
	public static void main(String[] args) throws InterruptedException {
		Logger.log("Main: Starting Game Loop");
		GamePanel.getInstance().run();
		Logger.log("Main: Game Loop failed", true);
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
