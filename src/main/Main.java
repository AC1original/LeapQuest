package main;

import utils.Logger;

import java.util.Scanner;
import java.util.concurrent.Executors;

//TODO: Better project structure
public class Main {
	
	public static void main(String[] args) throws InterruptedException {
		Executors.newSingleThreadExecutor().submit(() -> {
			Scanner scanner = new Scanner(System.in);
			do {
				String[] command = scanner.nextLine().split(" ");
				if (command.length == 3 && command[0].equals("tp")) {
					GamePanel.getInstance().getEntityHelper().getPlayer().teleport(Integer.parseInt(command[1]), Integer.parseInt(command[2]));
				}
			} while (GamePanel.isRunning());
		});

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
