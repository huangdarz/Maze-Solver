package maze_solver.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import maze_solver.ui.scenes.BaseScene;
import maze_solver.ui.scenes.MainScene;
import maze_solver.ui.scenes.StartScene;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Maze Solver");

        var start = new StartScene(primaryStage);
        var mainScene = new MainScene(primaryStage);

        BaseScene.saveFileProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !BaseScene.nameProperty.isEmpty().get()) {
                primaryStage.setScene(mainScene);
            }
        });

        BaseScene.nameProperty.addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                primaryStage.setTitle("Maze Solver" + " | " + newValue);
            }
        });

        primaryStage.setScene(start);
        primaryStage.show();
    }
}
