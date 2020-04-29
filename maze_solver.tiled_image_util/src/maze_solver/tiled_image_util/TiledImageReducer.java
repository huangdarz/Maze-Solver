package maze_solver.tiled_image_util;

import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class TiledImageReducer {
    public static Function<BufferedImage, BiFunction<Integer, Integer, BufferedImage>> reduce =
            image -> (tileWidth, tileHeight) -> {
        Objects.requireNonNull(image, "Image must not be null");
        if (tileWidth <= 0 || tileHeight <= 0) return image;
        var resultImage = new BufferedImage(image.getWidth() / tileWidth,
                image.getHeight() / tileHeight, BufferedImage.TYPE_INT_ARGB);
        for (int row = 0; row < image.getHeight(); row++) {
            for (int col = 0; col < image.getWidth(); col++) {
                if (col % tileWidth == 0 && row % tileHeight == 0) {
                    resultImage.setRGB(col / tileWidth, row / tileHeight, image.getRGB(col, row));
                }
            }
        }
        return resultImage;
    };
}
