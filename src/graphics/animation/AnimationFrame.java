package graphics.animation;

import graphics.Images;

import java.awt.image.BufferedImage;

public class AnimationFrame {
    private BufferedImage image;
    private int delay;
    private int imgWidth, imgHeight;

    private AnimationFrame(BufferedImage image, int imgWidth, int imgHeight, int delay) {
        this.image = image;
        this.delay = delay;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
    }

    public static AnimationFrame create(BufferedImage image, int imgWidth, int imgHeight, int delay) {
        return new AnimationFrame(image, imgWidth, imgHeight, delay);
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getDelay() {
        return delay;
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

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
    }

    public void setImgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
    }
}

