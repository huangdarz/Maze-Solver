package maze_solver.tiled_image_util;

import java.awt.image.BufferedImage;
import java.util.function.BiFunction;
import java.util.function.Function;

public class TiledImageCreator {
    public static Function<Integer[][], BiFunction<Integer, Integer, BufferedImage>>
            create = matrix -> (tileWidth, tileHeight) -> {
        var image = new BufferedImage(matrix[0].length, matrix.length, BufferedImage.TYPE_INT_ARGB);
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                image.setRGB(col, row, matrix[row][col]);
            }
        }
        return expandImage(image, tileWidth, tileHeight);
    };

    private static BufferedImage expandImage(BufferedImage image, int tileWidth, int tileHeight) {
        if (image == null  || tileWidth < 0 || tileHeight < 0) return image;
        var resultImage = new BufferedImage(image.getWidth() * tileWidth,
                image.getHeight() * tileHeight, BufferedImage.TYPE_INT_ARGB);
        var reducedX = -1;
        for (int x = 0; x < resultImage.getWidth(); x++) {
            if (x % tileWidth == 0) reducedX++;
            var reducedY = -1;
            for (int y = 0; y < resultImage.getHeight(); y++) {
                if (y % tileHeight == 0) reducedY++;
                resultImage.setRGB(x, y, image.getRGB(reducedX, reducedY));
            }
        }
        return resultImage;
    }
}
