package main;

import utils.Logger;

public class Main {
	private static final GamePanel gp = new GamePanel();
	
	public static void main(String[] args) throws InterruptedException {
		Logger.log("Main: Starting Game Loop");
		gp.run();
		Logger.log("Main: Game Loop failed", true);
	}
}
