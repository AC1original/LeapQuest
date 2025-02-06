package graphics.animation;
import java.awt.image.BufferedImage;

public class AnimationFrame {
    private BufferedImage image;
    private int imgWidth, imgHeight;
    private static final AnimationFrame[] empty;

    private AnimationFrame(BufferedImage image, int imgWidth, int imgHeight) {
        this.image = image;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
    }

    public static AnimationFrame[] createEmpty() {
        return empty;
    }

    public static AnimationFrame[] create(BufferedImage img, int split) {
        AnimationFrame[] animationFrames = new AnimationFrame[split];
        for (int i = 0; i<split; i++) {
            animationFrames[i] = create(img.getSubimage(i*(img.getWidth()/split), 0, img.getWidth()/split, img.getHeight()));
        }
        return animationFrames;
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

    public void modifySize(AnimationFrame[] frames, int newWidth, int newHeight) {
        for (AnimationFrame frame : frames) {
            frame.setImgWidth(newWidth);
            frame.setImgHeight(newHeight);
        }
    }

    static {
        empty = new AnimationFrame[10];
    }
}

