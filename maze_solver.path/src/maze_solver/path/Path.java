package maze_solver.path;

import javafx.geometry.Point2D;

import java.util.ArrayList;

public class Path {

    private Point2D startPoint;
    private Point2D endPoint;
    private ArrayList<Point2D> listOfPoints;

    public Path(Point2D startPoint, Point2D endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.listOfPoints = new ArrayList<>();
    }

    public void addPoint(Point2D point2D) {
        listOfPoints.add(point2D);
    }

    public void removePoint(Point2D point2D) {
        listOfPoints.remove(point2D);
    }

    public void newPoint(Point2D point2D) {
        ArrayList<Point2D> temp = new ArrayList<>();
        if (listOfPoints.contains(point2D)) {
            System.out.println(listOfPoints.indexOf(point2D));
            for (int i = 0; i <= listOfPoints.indexOf(point2D); i++) {
                temp.add(listOfPoints.get(i));
            }
            listOfPoints = temp;
        } else {
            listOfPoints.add(point2D);
        }
    }

    public ArrayList<Point2D> getPath() {
        return listOfPoints;
    }

    public boolean isComplete() {
        var ignoredPoints = new ArrayList<Point2D>();
        var resultIsComplete = false;
        var currentPoint = startPoint;
        var hasFoundNextPoint = true;
        var previousPoint = startPoint;
        while (hasFoundNextPoint) {
            hasFoundNextPoint = false;
            for (int i = 0; i < listOfPoints.size(); i++) {
                if (currentPoint.add(1, 0).equals(listOfPoints.get(i)) ||
                        currentPoint.add(0, 1).equals(listOfPoints.get(i)) ||
                        currentPoint.subtract(1, 0).equals(listOfPoints.get(i)) ||
                        currentPoint.subtract(0, 1).equals(listOfPoints.get(i))) {
                    previousPoint = currentPoint;
                    currentPoint = listOfPoints.get(i);
                    hasFoundNextPoint = true;
                }
            }
            if (currentPoint.equals(endPoint)) {
                hasFoundNextPoint = false;
                resultIsComplete = true;
            }
        }
        return resultIsComplete;
    }

    public void setStartPoint(Point2D point2D) {
        this.startPoint = point2D;
    }

    public void setEndPoint(Point2D point2D) {
        this.endPoint = point2D;
    }

}