package level;

import data.filemanager.Filemanager;
import graphics.ImageLoader;
import level.tile.Tile;
import level.tile.TileType;
import level.tile.Tiles;
import main.GamePanel;
import utils.Logger;

import java.awt.*;
import java.util.Objects;

public class LevelManager {
    private final GamePanel gp;
    private final Filemanager fileManager = new Filemanager();
    private final int[][] level = loadLevel(Objects.requireNonNull(getClass().getResource("/res/level/test_level.txt")).getPath());

    public LevelManager(GamePanel gp) {
        this.gp = gp;
        Logger.log(this.getClass(), "Initialized");
    }

    public int[][] loadLevel(String path) {
        fileManager.setPath(path);
        int[][] level = new int[fileManager.lines()][fileManager.get(0).length()];
        for (int line = 0; line < fileManager.lines(); line++) {
            String[] nums = fileManager.get(line).split(" ");
            for (int number = 0; number < nums.length; number++) {
                level[line][number] = Integer.parseInt(nums[number]);
            }
        }
        return level;
    }

    public void drawLevel(Graphics g) {
        for (int i = 0; i < level.length; i++) {
            for (int j = 0; j < level[i].length; j++) {
                var type = Tiles.getTypeByID(level[i][j]);
                var tile = type.parent();
                g.drawImage(ImageLoader.getCachedOrLoad(type.getPath(), type.parent().getName()), j * tile.getWidth(),
                        i * tile.getHeight(), tile.getWidth(), tile.getHeight(), null);
            }
        }
    }
}