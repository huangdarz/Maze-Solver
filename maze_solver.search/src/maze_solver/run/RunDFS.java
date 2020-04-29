package maze_solver.run;

import javafx.geometry.Point2D;
import maze_solver.depthFirstSearch.DepthFirstSearch;

import java.awt.image.BufferedImage;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static maze_solver.tiled_image_util.TiledImageConvertor.convert2DArray;
import static maze_solver.tiled_image_util.TiledImageReducer.reduce;
import static maze_solver.tiled_image_util.TiledImageCreator.create;

public class RunDFS {
    public BufferedImage run(BufferedImage mazeImage, Point2D startPoint, Point2D endPoint,
                             Predicate<Integer> isPath, Supplier<Integer> solvedIndicator,
                             int tileWidth, int tileHeight) {
        return reduce.andThen(param -> param.apply(tileWidth, tileHeight))
                .andThen(convert2DArray)
                .andThen(matrix -> DepthFirstSearch.run(matrix, startPoint, endPoint, isPath, solvedIndicator))
                .andThen(create)
                .andThen(param -> param.apply(tileWidth, tileHeight))
                .apply(mazeImage);
    }
}
