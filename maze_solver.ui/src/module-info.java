module maze.solver.ui {
    requires javafx.graphics;
    requires javafx.controls;
    requires java.desktop;
    requires javafx.swing;
    requires maze.solver.search;
    requires maze.solver.path;

    exports maze_solver.ui;
}