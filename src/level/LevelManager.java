package level;

import de.ac.Filemanager;
import graphics.ImageLoader;
import level.tile.Tile;
import level.tile.TileType;
import level.tile.Tiles;
import main.GamePanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.HitBox;
import utils.Logger;

import java.awt.*;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/*
TODO: Create nice level
TODO: Level movement
 */
public class LevelManager {
    private final GamePanel gp;
    private final Filemanager fileManager = new Filemanager();
    private boolean showHitBox = false;
    private final int[][] level;
    private final Map<HitBox, TileType> levelDat = new HashMap<>();

    public LevelManager(GamePanel gp, String resourceLocation) {
        this.gp = gp;
        level = loadLevel(Objects.requireNonNull(getClass().getResource(resourceLocation)).getPath());
        reloadLevelDat();
        showHitBox(true);
        Logger.log(this.getClass(), "Initialized");
    }

    public int[][] loadLevel(String path) {
        Logger.log(this.getClass(), "Try to load level at: " + path);
        try {
            fileManager.setPath(path);
        } catch (NoSuchFileException e) {
            throw new RuntimeException(e);
        }
        int[][] level = new int[fileManager.lines()][fileManager.get(0).split(" ").length];
        for (int line = 0; line < fileManager.lines(); line++) {
            String[] nums = fileManager.get(line).split(" ");
            for (int number = 0; number < nums.length; number++) {
                try {
                    level[line][number] = Integer.parseInt(nums[number]);
                } catch (NumberFormatException e) {
                    Logger.log(this.getClass(), "Failed to read level from file. Returned empty 2D-int-array instead", true);
                    return new int[0][0];
                }
            }
        }
        Logger.log(this.getClass(), "Successfully loaded level at: " + path);
        return level;
    }

    public void reloadLevelDat() {
        Logger.log(this.getClass(), "Try to reload level data");
        levelDat.clear();
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[y].length; x++) {
                var type = Tiles.getTypeByID(level[y][x]);
                levelDat.put(new HitBox(x * type.width(), y * type.height(), type.width(), type.height()), type);
            }
        }
        Logger.log(this.getClass(), "Successfully reloaded level data");
    }

    public void drawLevel(@NotNull Graphics g) {
        g.drawImage(ImageLoader.getCachedOrLoad("/res/level/background/sky.png", "background_sky"),
                0, 0, gp.getGameWidth(), gp.getGameHeight(), null);
        g.drawImage(ImageLoader.getCachedOrLoad("/res/level/background/mountains.png", "background_mountains"),
                0, 0, gp.getGameWidth(), gp.getGameHeight(), null);
        g.drawImage(ImageLoader.getCachedOrLoad("/res/level/background/ruins.png", "background_ruins"),
                0, 0, gp.getGameWidth(), gp.getGameHeight(), null);
        g.drawImage(ImageLoader.getCachedOrLoad("/res/level/background/sun.png", "background_sun"),
                gp.getGameWidth() - 120, 10, 100, 100, null);


        levelDat.forEach((loc, type) -> {
            g.drawImage(type.image(), loc.getX(), loc.getY(), loc.getWidth(), loc.getHeight(), null);

            if (isHitBoxShown()) {
                if (type.parent().isSolid()) {
                    g.setColor(Color.RED);
                    g.drawRect(loc.getX(), loc.getY(), loc.getWidth(), loc.getHeight());
                }
            }
        });
    }

    public void tick() {
        Tiles.getRegistered().forEach(Tile::tick);
    }

    public void showHitBox(boolean hitBox) {
        this.showHitBox = hitBox;
    }
    public boolean isHitBoxShown() {
        return showHitBox;
    }

    @Nullable
    public TileType wouldCollideTile(HitBox hitBox) {
        return levelDat.get(wouldCollideHitBox(hitBox));
    }

    public boolean wouldCollide(HitBox hitBox) {
        return wouldCollideHitBox(hitBox) != null;
    }

    @Nullable
    public HitBox wouldCollideHitBox(@NotNull HitBox hitBox) {
        return levelDat.keySet()
                .stream()
                .filter(rect -> levelDat.get(rect).parent().isSolid())
                .filter(hitBox::intersects)
                .findFirst()
                .orElse(null);
    }
}