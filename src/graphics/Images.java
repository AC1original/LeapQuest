package graphics;

import java.awt.image.BufferedImage;

public enum Images {
    EMPTY(ImageLoader.getImageByPath("res/util/empty.png"));

    public final BufferedImage image;
    Images(BufferedImage image) {
        this.image = image;
    }

}
