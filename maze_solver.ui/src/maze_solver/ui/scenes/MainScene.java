package maze_solver.ui.scenes;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import maze_solver.ui.controllers.MainSceneController;

public class MainScene extends BaseScene<BorderPane> {

    private static final double
            CANVAS_WIDTH = 0.95 * SCENE_WIDTH,
            CANVAS_HEIGHT = 0.86 * SCENE_HEIGHT;

    private final HBox
            hBoxTop,
            hBoxBottom;

    private final Button
            btn_Save,
            btn_LoadMaze,
            btn_Attempts,
            btn_Reset,
            btn_Solve;

    private final Label
            lbl_Timer,
            lbl_TileSize;

    private final Separator
            separator;

    private final SeparatorMenuItem
            separatorMenuItem,
            separatorMenuItem1;

    private final ComboBox<Integer>
            combo_TileSize;

    private final Canvas
            canvas;

    private final ContextMenu
            contextMenu;

    private final MenuItem
            menu_StartPoint,
            menu_EndPoint,
            menu_remove,
            menu_PlayerStart,
            menu_PlayerEnd;

    private final MainSceneController
            mainSceneController;

    public MainScene(Stage stage) {
        super(new BorderPane(), stage);

        this.hBoxTop = new HBox();
        this.hBoxBottom = new HBox();
        this.btn_Save = new Button("Save");
        this.btn_LoadMaze = new Button("Load Maze");
        this.btn_Attempts = new Button("Attempts");
        this.btn_Reset = new Button("Reset");
        this.btn_Solve = new Button("Solve");
        this.lbl_Timer = new Label("Timer: 00:00 Minutes");
        this.lbl_TileSize = new Label("Tile Size:");
        this.separator = new Separator(Orientation.VERTICAL);
        this.separatorMenuItem = new SeparatorMenuItem();
        this.separatorMenuItem1 = new SeparatorMenuItem();
        this.combo_TileSize = new ComboBox<>();
        this.canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        this.contextMenu = new ContextMenu();
        this.menu_StartPoint = new MenuItem("Start Point");
        this.menu_EndPoint = new MenuItem("End Point");
        this.menu_remove = new MenuItem("Remove");
        this.menu_PlayerStart = new MenuItem("Player Start Point");
        this.menu_PlayerEnd = new MenuItem("Player End Point");

        this.mainSceneController = new MainSceneController(canvas, canvas.getGraphicsContext2D(),
                combo_TileSize.getValue() == null ? 1 : combo_TileSize.getValue(),
                CANVAS_WIDTH, CANVAS_HEIGHT, instanceStage, saveFileProperty.get());


        configNodes();
        initNodes();
        root().setFocusTraversable(false);
        mainSceneController.configListeners(btn_Solve);
        saveFileProperty.addListener((observable, oldValue, newValue) -> {
            mainSceneController.setSaveFile(newValue);
            mainSceneController.openSaveFileImage();
        });
    }

    @Override
    protected void configNodes() {
        hBoxTop.setSpacing(25);
        hBoxTop.setAlignment(Pos.CENTER);

        hBoxBottom.setAlignment(Pos.CENTER);

        configBtn(btn_Save, btn_LoadMaze, btn_Attempts, btn_Reset, btn_Solve);
        btn_LoadMaze.setOnAction(mainSceneController::btnLoadMazeAction);
        btn_Reset.setOnAction(mainSceneController::btnResetAction);
        btn_Solve.setDisable(true);
        btn_Solve.setOnAction(mainSceneController::btnSolveAction);

        lbl_Timer.setFont(Font.font(30));
        lbl_TileSize.setFont(Font.font(30));

        combo_TileSize.setPrefHeight(40);
        combo_TileSize.setPromptText("Pixels");
        combo_TileSize.getSelectionModel().select(Integer.valueOf(10));
        combo_TileSize.valueProperty().addListener(
                (observable, oldValue, newValue) -> mainSceneController.setTileSize(newValue));
        combo_TileSize.setFocusTraversable(false);
        mainSceneController.setTileSize(combo_TileSize.getValue());

        canvas.setOnMouseMoved(mainSceneController::calcTileXY);
        canvas.setOnContextMenuRequested(event -> contextMenu.show(canvas, event.getScreenX(), event.getScreenY()));
        canvas.setOnMouseClicked(event -> contextMenu.hide());
        canvas.setOnKeyPressed(mainSceneController::playerMovement);
        canvas.setFocusTraversable(true);

        menu_StartPoint.setOnAction(mainSceneController::startPointAction);
        menu_EndPoint.setOnAction(mainSceneController::endPointAction);
        menu_remove.setOnAction(mainSceneController::removeAction);
        menu_PlayerStart.setOnAction(mainSceneController::menuPlayerStart);
        menu_PlayerEnd.setOnAction(mainSceneController::menuPlayerEnd);
    }

    @Override
    protected void initNodes() {
        hBoxTop.getChildren()
                .addAll(btn_Save,
                        btn_LoadMaze,
                        btn_Attempts,
                        btn_Reset,
                        lbl_Timer,
                        separator,
                        lbl_TileSize,
                        combo_TileSize);
        hBoxBottom.getChildren()
                .add(btn_Solve);
        root().setCenter(canvas);
        root().setTop(hBoxTop);
        root().setBottom(hBoxBottom);
        for (int s = 1; s <= 25; s++) combo_TileSize.getItems().add(s);
        contextMenu.getItems()
                .addAll(menu_PlayerStart,
                        menu_PlayerEnd,
                        separatorMenuItem1,
                        menu_StartPoint,
                        menu_EndPoint,
                        separatorMenuItem,
                        menu_remove);
    }

    private void configBtn(Button... buttons) {
        for (Button btn : buttons) {
            btn.setFont(Font.font(20));
            btn.setTextAlignment(TextAlignment.CENTER);
            btn.setFocusTraversable(false);
        }
    }

}