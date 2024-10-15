package graphics.animation;

import java.awt.image.BufferedImage;

public class AnimationFrame {
    private BufferedImage image;
    private int imgWidth, imgHeight;

    private AnimationFrame(BufferedImage image, int imgWidth, int imgHeight) {
        this.image = image;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
    }

    public static AnimationFrame create(BufferedImage image) {
        return create(image, image.getWidth(), image.getHeight());
    }

    public static AnimationFrame create(BufferedImage image, int imgWidth, int imgHeight) {
        return new AnimationFrame(image, imgWidth, imgHeight);
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
    }

    public void setImgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
    }
}

