package oolala;


import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import oolala.view.MainDisplay;

public class Main extends Application {

    public static final String TITLE = "OOLALA Simulator";
    public static final int WIDTH_SIZE = 1100;
    public static final int HEIGHT_SIZE = 800;
    public static final String DEFAULT_LANGUAGE = "English";

    /**
     * Organize display of game in a scene and start the game.
     */
    @Override
    public void start (Stage stage) {
        MainDisplay program = new MainDisplay(DEFAULT_LANGUAGE, WIDTH_SIZE, HEIGHT_SIZE);

        stage.setScene(program.setupDisplay());
        stage.setTitle(TITLE);
        stage.show();
    }
}
