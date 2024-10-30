package level;

import data.filemanager.FileManager;
import data.filemanager.IncorrectPathException;
import main.GamePanel;

import java.awt.*;
import java.util.Arrays;

public class LevelManager {
    private final GamePanel gp;
    private final FileManager fileManager = new FileManager();

    public LevelManager(GamePanel gp) {
        this.gp = gp;
    }

    public int[][] loadLevel(String path) {
        try {
            fileManager.setupPath(path);
        } catch (IncorrectPathException e) {
            throw new RuntimeException(e);
        }
        int[][] retu = new int[fileManager.length()][fileManager.get(0).length()];
        for (int i = 0; i < fileManager.length(); i++) {
            String s = Arrays.toString(fileManager.get(i).split(" "));
            //System.out.println(s);

            for (int c = 0; c < fileManager.get(i).length(); c++) {
                retu[i][c] = fileManager.get(i).toCharArray()[c];
            }
        }
        return retu;
    }

    public void drawLevel(Graphics g) {
        /*int[][] level = loadLevel("C:\\Users\\bitte\\Documents\\Programmierung\\PA-2BKI2\\src\\res\\level\\test_level.txt");
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[y].length; x++) {
                if (level[y][x] != 1) {
                    return;
                }
                g.drawImage(ImageLoader.getAutomated("/res/level/tiles/test.png", "test"), x*32, y*32, 32, 32, null);
            }
        }

         */
    }

    public GamePanel getGamePanel() {
        return gp;
    }
}
