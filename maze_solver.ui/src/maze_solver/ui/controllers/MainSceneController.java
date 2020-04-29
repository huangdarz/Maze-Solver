package maze_solver.ui.controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import maze_solver.run.RunDFS;
import maze_solver.path.Path;
import maze_solver.path.PathDrawer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class MainSceneController {

    private final double
            CANVAS_WIDTH,
            CANVAS_HEIGHT;

    private final Canvas
            canvas;

    private final GraphicsContext
            graphicsContext;

    private int
            tileSize;

    private final Stage
            instanceStage;

    private File
            saveFile;

    private double
            imageX,
            factor,
            tileX,
            tileY;

    private final SimpleObjectProperty<BufferedImage>
            image,
            resizedImage,
            solvedImage;

    private final SimpleBooleanProperty
            canSolve;

    private final boolean[]
            startAndEnd;

    private Point2D
            startPoint,
            endPoint;

    private final SimpleObjectProperty<Point2D>
            player;

    private final Path
            path;

    public MainSceneController(Canvas canvas, GraphicsContext graphicsContext, int tileSize,
                               double CANVAS_WIDTH, double CANVAS_HEIGHT, Stage stage, File saveFile) {
        this.canvas = canvas;
        this.graphicsContext = graphicsContext;
        this.tileSize = tileSize;
        this.CANVAS_WIDTH = CANVAS_WIDTH;
        this.CANVAS_HEIGHT = CANVAS_HEIGHT;
        this.instanceStage = stage;
        this.saveFile = saveFile;

        this.imageX = 0;
        this.factor = 0;
        this.tileX = 0;
        this.tileY = 0;
        this.image = new SimpleObjectProperty<>();
        this.resizedImage = new SimpleObjectProperty<>();
        this.solvedImage = new SimpleObjectProperty<>();
        this.canSolve = new SimpleBooleanProperty(false);
        this.startAndEnd = new boolean[] {false, false};
        this.startPoint = new Point2D(0, 0);
        this.endPoint = new Point2D(0, 0);
        this.player = new SimpleObjectProperty<>();
        this.path = new Path(startPoint, endPoint);
    }

    public void configListeners(Button btn_Solve) {
        image.addListener((observable, oldValue, newValue) -> displayImage(newValue));
        canSolve.addListener((observable, oldValue, newValue) -> {
            if (newValue) btn_Solve.setDisable(false);
            else btn_Solve.setDisable(true);
        });
        solvedImage.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue != oldValue) {
                displayImage(newValue);
            }
        });
        player.addListener((observable, oldValue, newValue) -> {
            path.newPoint(newValue);
            PathDrawer.draw(graphicsContext, path.getPath(), tileSize, tileSize, factor, imageX, "cyan");
//            System.out.println(newValue.toString());
        });
    }

    public void btnLoadMazeAction(ActionEvent event) {
        var fileChooser = new FileChooser();
        fileChooser.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("png", "*.png"),
                        new FileChooser.ExtensionFilter("jpeg", "*.jpeg"),
                        new FileChooser.ExtensionFilter("jpg", "*.jpg"));
        var imgFile = fileChooser.showOpenDialog(instanceStage);
        try {
            if (imgFile != null) image.set(ImageIO.read(imgFile));

            Scanner scanner = new Scanner(saveFile);
            ArrayList<String> existingLines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                existingLines.add(line);
            }
            var hasExistingImgPath = false;
            var existingPath = "";
            for (String line : existingLines) {
                if (line.startsWith("{") && line.endsWith("}")) {
                    hasExistingImgPath = true;
                    existingPath = line;
                }
            }
            if (hasExistingImgPath) {
                var indexPath = existingLines.indexOf(existingPath);
                existingLines.remove(existingPath);
                if (imgFile != null) {
                    existingLines.add(indexPath, "{" + imgFile.getCanonicalPath() + "}\n");
                }
            } else {
                if (imgFile != null) {
                    existingLines.add("{" + imgFile.getCanonicalPath() + "}\n");
                }
            }
            if (imgFile != null) {
                FileWriter writer = new FileWriter(saveFile);
                for (String line : existingLines) {
                    writer.write(line);
                }
                writer.close();
            }
        } catch (IOException ignored) {}
        if (imgFile != null) {
            try {
                image.set(ImageIO.read(imgFile));
            } catch (IOException ignored) {}
        }
    }

    public void openSaveFileImage() {
        if (saveFile != null) {
            var hasMazePath = false;
            var mazePath = "";
            try {
                Scanner scanner = new Scanner(saveFile);
                while (scanner.hasNextLine() && !hasMazePath) {
                    String line = scanner.nextLine();
                    hasMazePath = line.startsWith("{") && line.endsWith("}");
                    if (hasMazePath) {
                        mazePath = line.substring(1, line.length() - 1);
                        System.out.println(mazePath);
                    }
                }
            } catch (FileNotFoundException | NullPointerException ignored) {}
            if (hasMazePath) {
                var imgFile = new File(mazePath);
                try {
                    image.set(ImageIO.read(imgFile));
                } catch (IOException ignored) {}
            }
        }
    }

    public void btnResetAction(ActionEvent event) {
        graphicsContext.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        displayImage(image.get());
        startAndEnd[0] = false;
        startAndEnd[1] = false;
        canSolve.set(false);
        path.getPath().clear();
        player.set(new Point2D(-1, -1));
        endPoint = new Point2D(0, 0);
    }

    public void btnSolveAction(ActionEvent event) {
        var solver = new RunDFS();
        solvedImage.set(solver.run(image.get(),
                startPoint, endPoint,
                i -> i == Color.WHITE.getRGB(), Color.BLUE::getRGB, tileSize,
                tileSize));
    }

    public void startPointAction(ActionEvent event) {
        if (!startAndEnd[0]) {
            drawPoint("green", Color.WHITE::getRGB);
            startPoint = new Point2D(tileX, tileY);
            startAndEnd[0] = true;
            if (startAndEnd[1]) canSolve.set(true);
        }
    }

    public void endPointAction(ActionEvent event) {
        if (!startAndEnd[1]) {
            drawPoint("red", Color.WHITE::getRGB);
            endPoint = new Point2D(tileX, tileY);
            startAndEnd[1] = true;
            if (startAndEnd[0]) canSolve.set(true);
        }
    }

    public void removeAction(ActionEvent event) {
        graphicsContext.clearRect((tileX * tileSize * factor) + imageX + 1,
                tileY * tileSize * factor + 2, tileSize * factor - 3,
                tileSize * factor - 3);
        graphicsContext.setFill(Paint.valueOf("white"));
        graphicsContext.fillRect((tileX * tileSize * factor) + imageX + 1,
                tileY * tileSize * factor + 2, tileSize * factor - 3,
                tileSize * factor - 3);
        var temp = new Point2D(tileX, tileY);
        if (temp.equals(startPoint)) {
            startAndEnd[0] = false;
        } else if (temp.equals(endPoint)) {
            startAndEnd[1] = false;
        }
        canSolve.set(false);
    }

    public void displayImage(BufferedImage newValue) {
        if (newValue != null) {
            graphicsContext.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
            if (newValue.getHeight() >= newValue.getWidth()) {
                factor = ((CANVAS_HEIGHT) / newValue.getHeight());
            } else {
                factor = ((CANVAS_WIDTH) / newValue.getWidth());
            }
            int newWidth = (int) (newValue.getWidth() * factor);
            int newHeight = (int) (newValue.getHeight() * factor);
            BufferedImage resized = new BufferedImage(
                    newWidth, newHeight, newValue.getType());
            Graphics2D graph = (Graphics2D) resized.getGraphics();
            graph.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graph.drawImage(newValue, 0, 0,
                    newWidth, newHeight, 0, 0, newValue.getWidth(), newValue.getHeight(), null);
            graph.dispose();
            double centreX = (canvas.getWidth() / 2)  - (resized.getWidth() / 2.0);
            imageX = centreX;
            graphicsContext.drawImage(SwingFXUtils.toFXImage(resized, null), centreX, 0);
            graphicsContext.strokeRect(centreX - 1, 0, resized.getWidth() + 1, resized.getHeight() + 1);
            graphicsContext.strokeRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
            resizedImage.set(resized);
        }
    }

    public void mousePointPlacement(MouseEvent event) {
        try {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                var mouseX = event.getX();
                var mouseY = event.getY();
                tileY = Math.floor(mouseY / (tileSize * factor));
                tileX = Math.floor((mouseX - imageX) / (tileSize * factor));
                System.out.println("Tile Y: " + tileY + " | " + "Tile X: " + tileX);
                graphicsContext.setFill(Paint.valueOf("cyan"));
                graphicsContext.fillRect((tileX * tileSize * factor) + imageX + 1,
                        tileY * tileSize * factor + 2, tileSize * factor - 3,
                        tileSize * factor - 3);
            }
        } catch (NullPointerException ignored) {}
    }

    public void drawPoint(String colour, Supplier<Integer> pathColour) {
        try {
            graphicsContext.setFill(Paint.valueOf(colour));
            if (image.get().getRGB((int)(tileX * tileSize), (int) (tileY * tileSize)) == pathColour.get()) {
                graphicsContext.fillOval((tileX * tileSize * factor) + imageX + 1,
                        tileY * tileSize * factor + 2, tileSize * factor - 3,
                        tileSize * factor - 3);
            }
        } catch (NullPointerException | ArrayIndexOutOfBoundsException ignored) {}
    }

    public void calcTileXY(MouseEvent event) {
        tileX = Math.floor((event.getX() - imageX) / (tileSize * factor));
        tileY = Math.floor(event.getY() / (tileSize * factor));
    }

    public void playerMovement(KeyEvent keyEvent) {
        if (player.get() != null) {
            try {
                switch (keyEvent.getCode()) {
                    case UP:
                        if (image.get().getRGB((int) (player.get().getX() * tileSize),
                                (int) ((player.get().getY() - 1) * tileSize)) == Color.WHITE.getRGB())
                            player.set(player.get().subtract(0, 1));
                        break;
                    case DOWN:
                        if (image.get().getRGB((int) (player.get().getX() * tileSize),
                                (int) ((player.get().getY() + 1) * tileSize)) == Color.WHITE.getRGB())
                            player.set(player.get().add(0, 1));
                        break;
                    case LEFT:
                        if (image.get().getRGB((int) ((player.get().getX() - 1) * tileSize),
                                (int) (player.get().getY() * tileSize)) == Color.WHITE.getRGB())
                            player.set(player.get().subtract(1, 0));
                        break;
                    case RIGHT:
                        if (image.get().getRGB((int) ((player.get().getX() + 1) * tileSize),
                                (int) (player.get().getY() * tileSize)) == Color.WHITE.getRGB())
                            player.set(player.get().add(1, 0));
                        break;
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {};
        }
    }

    public void menuPlayerStart(ActionEvent event) {
        path.setStartPoint(new Point2D(tileX, tileY));
        player.set(new Point2D(tileX, tileY));
        drawPoint("cyan", Color.WHITE::getRGB);
    }

    public void menuPlayerEnd(ActionEvent event) {
        path.setEndPoint(new Point2D(tileX, tileY));
        drawPoint("orange", Color.WHITE::getRGB);
    }

    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }

    public void setSaveFile(File file) {
        this.saveFile = file;
    }

    public void setPlayer(Point2D point2D) {
        this.player.set(point2D);
    }

}