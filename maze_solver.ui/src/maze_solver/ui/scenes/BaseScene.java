package maze_solver.ui.scenes;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public abstract class BaseScene<T> extends Scene {

    protected static final double
            SCENE_WIDTH = 1280,
            SCENE_HEIGHT = 720;

    protected final Stage
            instanceStage;

    public static final SimpleObjectProperty<File>
            saveFileProperty = new SimpleObjectProperty<>();

    public static final SimpleStringProperty
            nameProperty = new SimpleStringProperty("");

    protected final FileChooser
            fileChooser;

    public BaseScene(Parent root, Stage stage) {
        super(root, SCENE_WIDTH, SCENE_HEIGHT);
        this.instanceStage = stage;
        this.fileChooser = new FileChooser();
    }

    protected abstract void configNodes();

    protected abstract void initNodes();

    protected T root() {
        return ((T) getRoot());
    }

}