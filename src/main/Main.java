package main;
import utils.Logger;

public class Main {
	private static GamePanel gp;
	
	public static void main(String[] args) throws InterruptedException {
		gp = new GamePanel();

		Logger.log("Main: Starting Game Loop");
		gp.run();
		Logger.log("Main: Game Loop failed", true);
	}

	public static GamePanel getGamePanel() {
		return gp;
	}
}
