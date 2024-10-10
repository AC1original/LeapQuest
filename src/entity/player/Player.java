package entity.player;

import entity.Entity;
import graphics.Images;

import java.awt.image.BufferedImage;

public class Player extends Entity {

    @Override
    public BufferedImage getImage() {
        return Images.PLAYER.image;
    }
}
