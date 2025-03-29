package level;

import de.ac.Filemanager;
import graphics.Drawable;
import graphics.GameRenderer;
import graphics.ImageLoader;
import level.tile.Tile;
import level.tile.TileType;
import level.tile.Tiles;
import org.jetbrains.annotations.NotNull;
import utils.HitBox;
import utils.Logger;
import utils.Ticked;

import java.awt.*;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/*
TODO: Create nice level
TODO: Level movement
TODO: Extra thread (completable future)
TODO: Object oriented use
 */
public class LevelManager implements Drawable {
    private final GameRenderer renderer;
    private final Filemanager fileManager;
    private boolean showHitBox = false;
    private final int[][] level;
    private final Map<HitBox, TileType> levelDat = new HashMap<>();

    public LevelManager(GameRenderer renderer, String resourceLocation) {
        this.renderer = renderer;

        fileManager = new Filemanager();
        level = loadLevel(Objects.requireNonNull(getClass().getResource(resourceLocation)).getPath());
        reloadLevelDat();
        renderer.addDrawable(this);
        Logger.info(this, "Initialized.");
    }

    public int[][] loadLevel(String path) {
        Logger.info(this, "Try to load level at: " + path + ".");
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
                    Logger.error(this, "Failed to read level from file. Returned empty 2D-int-array instead.");
                    return new int[0][0];
                }
            }
        }
        Logger.info(this, "Successfully loaded level at: " + path + ".");
        return level;
    }

    public void reloadLevelDat() {
        Logger.info(this, "Try to reload level data.");
        levelDat.clear();
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[y].length; x++) {
                var type = Tiles.getTypeByID(level[y][x]);
                levelDat.put(new HitBox(x * type.width(), y * type.height(), type.width(), type.height()), type);
            }
        }
        Logger.info(this, "Successfully reloaded level data.");
    }

    @Override
    public void fDraw(Graphics g) {
        g.drawImage(ImageLoader.getCachedOrLoad("/res/level/background/sky.png", "background_sky"),
                0, 0, renderer.getWidth(), renderer.getHeight(), null);
        g.drawImage(ImageLoader.getCachedOrLoad("/res/level/background/mountains.png", "background_mountains"),
                0, 0, renderer.getWidth(), renderer.getHeight(), null);
        g.drawImage(ImageLoader.getCachedOrLoad("/res/level/background/ruins.png", "background_ruins"),
                0, 0, renderer.getWidth(), renderer.getHeight(), null);
        g.drawImage(ImageLoader.getCachedOrLoad("/res/level/background/sun.png", "background_sun"),
                renderer.getWidth() - 200, 10, 150, 150, null);


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

    public void moveLevel(int deltaX, int deltaY) {
        levelDat.forEach((loc, type) -> loc.move(deltaX, deltaY));
    }

    @Ticked
    public void tick() {
        Tiles.getRegistered().forEach(Tile::tick);
    }

    public void showHitBox(boolean hitBox) {
        this.showHitBox = hitBox;
    }

    public boolean isHitBoxShown() {
        return showHitBox;
    }

    public List<TileType> getCollisionTiles(@NotNull HitBox hitBox) {
        return getCollisions(hitBox).stream()
                .map(levelDat::get)
                .collect(Collectors.toList());
    }

    public boolean checkCollision(int x, int y, int width, int height) {
        return !getCollisions(x, y, width, height).isEmpty();
    }

    public boolean checkCollision(HitBox hitBox) {
        return !getCollisions(hitBox).isEmpty();
    }

    public List<HitBox> getCollisions(int x, int y, int width, int height) {
        return levelDat.keySet()
                .stream()
                .filter(rect -> levelDat.get(rect).parent().isSolid())
                .filter(hb -> hb.intersects(x, y, width, height))
                .collect(Collectors.toList());
    }

    public List<HitBox> getCollisions(@NotNull HitBox hitBox) {
        return getCollisions(hitBox.getX(), hitBox.getY(), hitBox.getWidth(), hitBox.getHeight());
    }

    @Override
    public Priority priority() {
        return Priority.LOW;
    }

    @Override
    public Drawable parent() {
        return this;
    }
}