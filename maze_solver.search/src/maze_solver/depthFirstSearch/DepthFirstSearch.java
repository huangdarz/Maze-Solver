package maze_solver.depthFirstSearch;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class DepthFirstSearch {

    public static <T> T[][] run(T[][] matrix, Point2D startPoint, Point2D endPoint, Predicate<T> checkIsValid,
                                Supplier<T> solvedIndicator) {
        var resultMatrix = Arrays.stream(matrix).map(m -> m.clone()).toArray(m -> matrix.clone());
        var hasFoundSolution = depthFirstSearch(resultMatrix, Optional.ofNullable(startPoint),
                Optional.ofNullable(endPoint), new ArrayList<Point2D>(), checkIsValid, solvedIndicator, -1);
        return hasFoundSolution ? resultMatrix : matrix;
    }

    private static <T> boolean depthFirstSearch(T[][] matrix, Optional<Point2D> startPoint, Optional<Point2D> endPoint,
                                                ArrayList<Point2D> pointsVisited, Predicate<T> checkIsValid,
                                                Supplier<T> solvedIndicator, int prevDirection) {
        var resultHasFoundSolution = false;
        var currentPoint = startPoint.filter(point2D -> withinMatrixIndices(matrix, point2D))
                .filter(DepthFirstSearch::isWholeNumberCoord)
                .orElseThrow(IllegalArgumentException::new);
        var endGoal = endPoint.filter(point2D -> withinMatrixIndices(matrix, point2D))
                .filter(DepthFirstSearch::isWholeNumberCoord)
                .orElseThrow(IllegalArgumentException::new);
        if (currentPoint.equals(endGoal)) resultHasFoundSolution = true;
        pointsVisited.add(currentPoint);
        for (byte direction = 0; direction < 4 && !resultHasFoundSolution; direction++) {
            var x = (int) currentPoint.getX();
            var y = (int) currentPoint.getY();
            if (direction != prevDirection) {
                switch (direction) {
                    case 0: // up
                        try {
                            if (checkIsValid.test(matrix[y - 1][x]) &&
                                    !pointsVisited.contains(currentPoint.subtract(0,1))) {
                                resultHasFoundSolution = depthFirstSearch(matrix,
                                        Optional.of(currentPoint.subtract(0,1)), endPoint,
                                        pointsVisited, checkIsValid, solvedIndicator, 1);
                            }
                        } catch (ArrayIndexOutOfBoundsException ignored) {}
                        break;
                    case 1: // down
                        try {
                            if (checkIsValid.test(matrix[y + 1][x]) &&
                                    !pointsVisited.contains(currentPoint.add(0,1))) {
                                resultHasFoundSolution = depthFirstSearch(matrix,
                                        Optional.of(currentPoint.add(0,1)), endPoint,
                                        pointsVisited, checkIsValid, solvedIndicator, 0);
                            }
                        } catch (ArrayIndexOutOfBoundsException ignored) {}
                        break;
                    case 2: // left
                        try {
                            if (checkIsValid.test(matrix[y][x - 1]) &&
                                    !pointsVisited.contains(currentPoint.subtract(1,0))) {
                                resultHasFoundSolution = depthFirstSearch(matrix,
                                        Optional.of(currentPoint.subtract(1,0)), endPoint,
                                        pointsVisited, checkIsValid, solvedIndicator, 3);
                            }
                        } catch (ArrayIndexOutOfBoundsException ignored) {}
                        break;
                    case 3: // right
                        try {
                            if (checkIsValid.test(matrix[y][x + 1]) &&
                                    !pointsVisited.contains(currentPoint.add(1,0))) {
                                resultHasFoundSolution = depthFirstSearch(matrix,
                                        Optional.of(currentPoint.add(1,0)), endPoint,
                                        pointsVisited, checkIsValid, solvedIndicator, 2);
                            }
                        } catch (ArrayIndexOutOfBoundsException ignored) {}
                        break;
                }
            }
        }
        if (resultHasFoundSolution) {
            matrix[(int) currentPoint.getY()][(int) currentPoint.getX()] = solvedIndicator.get();
        }
        pointsVisited.remove(currentPoint);
        return resultHasFoundSolution;
    }

    private static <T> boolean withinMatrixIndices(T[][] matrix, Point2D point) {
        return point.getX() >= 0 && point.getX() < matrix[0].length &&
                point.getY() >= 0 && point.getY() < matrix.length;
    }

    private static boolean isWholeNumberCoord(Point2D point2D) {
        return point2D.getX() % 1 == 0.0 && point2D.getY() % 1 == 0.0;
    }

}
