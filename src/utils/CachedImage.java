package utils;

import java.awt.image.BufferedImage;

public record CachedImage(String name, String path, BufferedImage image) {
}
