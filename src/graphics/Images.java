package graphics;

import java.awt.image.BufferedImage;

public enum Images {
    EMPTY(ImageLoader.getImageByPath("res/util/empty.png")),
    PLAYER_DEFAULT(ImageLoader.getImageByPath("res/entity/player/player_idle_left.png").getSubimage(0, 0, 38, 28)),
    PLAYER_IDLING_LEFT(ImageLoader.getImageByPath("res/entity/player/player_idle_left.png")),
    PLAYER_IDLING_RIGHT(ImageLoader.getImageByPath("res/entity/player/player_idle_right.png")),
    PLAYER_WALK_LEFT(ImageLoader.getImageByPath("res/entity/player/player_walk_left.png")),
    PLAYER_WALK_RIGHT(ImageLoader.getImageByPath("res/entity/player/player_walk_right.png")),
    TEST(ImageLoader.getImageByPath("res/level/tiles/test.png"));

    public final BufferedImage image;
    Images(BufferedImage image) {
        this.image = image;
    }

}
