package graphics;

import java.awt.image.BufferedImage;

public enum Images {
    SPRITES(ImageLoader.getImageByPath("res/spriteSheets/sprite.png")),
    PLAYER(ImageLoader.getSubImage(SPRITES.image, 1338, 2, 88, 94));

    public BufferedImage image;
    Images(BufferedImage image) {
        this.image = image;
    }
}
