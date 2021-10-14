package oolala.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import util.DukeApplicationTest;

public class TurtleViewTest extends DukeApplicationTest {
    private final int myWidth = 1000;
    private final int myHeight = 700;
    private final Paint myBackgroundColor = Color.THISTLE;
    private final Paint penColor = Color.RED;
    private Scene myScene;
    private String language = "English";
    private final String title = "LogoTitle";

    @Override
    public void start (Stage stage) {
        HBox myMenuRoot = new HBox();
        myScene = new Scene(myMenuRoot, myHeight, myWidth, myBackgroundColor);
        LogoIDEView myLogoIDEView = new LogoIDEView(language, penColor, myBackgroundColor, myWidth, myHeight, myScene, "data/examples/logo/");
        myScene.setRoot((Parent) myLogoIDEView.setupDisplay(myWidth, myHeight, title));
        stage.setScene(myScene);
        stage.show();
    }
}
