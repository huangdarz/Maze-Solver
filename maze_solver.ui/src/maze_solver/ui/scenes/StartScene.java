package maze_solver.ui.scenes;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class StartScene extends BaseScene<BorderPane> {

    @SuppressWarnings("FieldCanBeLocal")
    private final double
            VBOX_SPACING = 45,

    FONT_SIZE_TEXT = 64,
            FONT_SIZE_FIELD = 36,
            FONT_SIZE_BUTTON = 36;

    private final VBox
            vBoxForElements;

    private final Text
            txt_MazeSolver;

    private final TextField
            field_Name;

    private final Button
            btn_ExistingSaveFile,
            btn_NewSave;

    public StartScene(Stage stage) {
        super(new BorderPane(), stage);

        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Maze Solver Save File", "*.mzsf"));

        this.vBoxForElements = new VBox(VBOX_SPACING);
        this.txt_MazeSolver = new Text("Maze Solver");
        this.field_Name = new TextField();
        this.btn_ExistingSaveFile = new Button("Existing Save File");
        this.btn_NewSave = new Button("New Save");

        configNodes();
        initNodes();
        root().requestFocus();

        nameProperty.addListener((observable, oldValue, newValue) -> {
            btn_ExistingSaveFile.setDisable(false);
            btn_NewSave.setDisable(false);
        });
    }

    @Override
    protected void configNodes() {
        vBoxForElements.setAlignment(Pos.CENTER);

        txt_MazeSolver.setFont(Font.font(FONT_SIZE_TEXT));

        field_Name.setPromptText("Enter Name:");
        field_Name.setFont(Font.font(FONT_SIZE_FIELD));
        field_Name.setMaxWidth(0.75 * SCENE_WIDTH);
        nameProperty.bind(field_Name.textProperty());

        btn_ExistingSaveFile.setDisable(true);
        btn_ExistingSaveFile.setFont(Font.font(FONT_SIZE_BUTTON));
        btn_ExistingSaveFile.setOnAction(event -> {
            saveFileProperty.set(fileChooser.showOpenDialog(instanceStage));
        });

        btn_NewSave.setDisable(true);
        btn_NewSave.setFont(Font.font(FONT_SIZE_BUTTON));
        btn_NewSave.setOnAction(event -> {
            var file = fileChooser.showSaveDialog(instanceStage);
            try {
                if (file != null) file.createNewFile();
                saveFileProperty.set(file);
            } catch (IOException ignored) {
                saveFileProperty.set(null);
            }
        });
    }

    @Override
    protected void initNodes() {
        vBoxForElements.getChildren()
                .addAll(txt_MazeSolver,
                        field_Name,
                        btn_ExistingSaveFile,
                        btn_NewSave);
        root().setCenter(vBoxForElements);
    }

}