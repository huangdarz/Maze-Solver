package maze_solver.path;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class PathDrawer {

    private static ArrayList<Point2D> previousPath = new ArrayList<>();

    public static void draw(GraphicsContext graphics, ArrayList<Point2D> path, int tileWidth, int tileHeight,
                            double factor, double imageX, String colour) {
        clear(graphics, tileWidth, tileHeight, factor, imageX);
        graphics.setFill(Paint.valueOf(colour));
        for (int i = 0; i < path.size(); i++) {
            graphics.fillRect(path.get(i).getX() * tileWidth * factor + imageX + 1,
                    path.get(i).getY() * tileHeight * factor + 2,
                    tileWidth * factor - 3, tileHeight * factor - 3);
        }
        previousPath = path;
    }

    private static void clear(GraphicsContext graphics, int tileWidth, int tileHeight,
                              double factor, double imageX) {
        if (previousPath != null) {
            for (int i = 0; i < previousPath.size(); i++) {
                graphics.clearRect(previousPath.get(i).getX() * tileWidth * factor + imageX + 1,
                        previousPath.get(i).getY() * tileHeight * factor + 2,
                        tileWidth * factor - 3, tileHeight * factor - 3);
                graphics.setFill(Paint.valueOf("white"));
                graphics.fillRect(previousPath.get(i).getX() * tileWidth * factor + imageX + 1,
                        previousPath.get(i).getY() * tileHeight * factor + 2,
                        tileWidth * factor - 3, tileHeight * factor - 3);
            }
        }
    }

}