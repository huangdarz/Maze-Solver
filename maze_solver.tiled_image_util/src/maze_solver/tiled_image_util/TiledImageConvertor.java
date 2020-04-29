package maze_solver.tiled_image_util;

import java.awt.image.BufferedImage;
import java.util.function.Function;

public class TiledImageConvertor {
    public static Function<BufferedImage, Integer[][]> convert2DArray = image -> {
        Integer[][] resultArray = new Integer[image.getHeight()][image.getWidth()];
        for (int row = 0; row < image.getHeight(); row++) {
            for (int col = 0; col < image.getWidth(); col++) {
                resultArray[row][col] = image.getRGB(col, row);
            }
        }
        return resultArray;
    };
}
