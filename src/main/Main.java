package main;

import utils.Logger;

import java.util.logging.Level;

public class Main {
	private static final GamePanel gp = new GamePanel();
	
	public static void main(String[] args) throws InterruptedException {
		Logger.log("Main: Starting Game Loop");
		gp.run();
		Logger.log("Main: Game Loop failed", true);
	}

	public static GamePanel getGamePanel() {
		return gp;
	}
}
