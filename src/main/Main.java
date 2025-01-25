package main;

import java.util.concurrent.TimeUnit;

//TODO: GameLoop helper class
public class Main {
	
	public static void main(String[] args) throws InterruptedException {
		Cache<String> cache = new Cache.CacheBuilder<String>()
				.timeoutDelay(5, TimeUnit.SECONDS)
				.deleteAfterTimeout(true)
				.build();

		cache.add("string1", "String1");
		cache.add("string2", "String2");
		Thread.sleep(5000);
		cache.add("string3", "String3");
		cache.add("string4", "String4");

		while (cache.stream().findAny().isPresent()) {
			cache.forEach(c -> System.out.println(c.toString()));
		}

		/*Logger.info(Main.class, "Starting Game Loop.");
		GamePanel.getInstance().run();
		Logger.error(Main.class, "Game Loop failed!");
				 */
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
