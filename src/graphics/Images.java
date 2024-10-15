package graphics;

import java.awt.image.BufferedImage;

public enum Images {
    EMPTY(ImageLoader.getImageByPath("res/util/empty.png")),
    PLAYER_IDLING_RIGHT(ImageLoader.getImageByPath("res/entity/player/player_idle_right.png"));

    public final BufferedImage image;
    Images(BufferedImage image) {
        this.image = image;
    }

}
