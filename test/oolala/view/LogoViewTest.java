package oolala.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class LogoViewTest extends DukeApplicationTest  {

    private final String filepath = "data/examples/logo/telltest.txt";
    private final int myWidth = 1000;
    private final int myHeight = 700;
    private final Paint myBackgroundColor = Color.THISTLE;
    private final Paint penColor = Color.RED;
    private Scene myScene;
    private String language = "English";
    private final String title = "LogoTitle";
    private LogoIDEView myLogoIDEView;

    @Override
    public void start (Stage stage) {
        HBox myMenuRoot = new HBox();
        myScene = new Scene(myMenuRoot, myWidth, myHeight, myBackgroundColor);
        myLogoIDEView = new LogoIDEView(language, penColor, myBackgroundColor, myWidth, myHeight, myScene, "data/examples/logo/");
        myScene.setRoot((Parent) myLogoIDEView.setupDisplay(myWidth, myHeight, title));
        stage.setScene(myScene);
        stage.show();
    }

    @Test
    public void testInputPanel() {
        myLogoIDEView.setCommandLineText("tell 1 turtle1");
        assertTrue(myLogoIDEView.getCommandLineText().equals("tell 1 turtle1"));
    }


    @Test
    void testLoadCommand() {
        Button button = lookup("Load Command File").query();
        String expected = "CLICK test!";
        clickOn(button);
    }

    @Test
    void testTurtleImage() {
        Button button = lookup("Load Turtle Image").query();
        String expected = "CLICK test!";
        clickOn(button);
    }

    @Test
    void testSaveFile() {
        Button button = lookup("Save Into File").query();
        String expected = "CLICK test!";
        clickOn(button);
    }

    @Test
    void testSetHome() {
        Button button = lookup("Set Home").query();
        String expected = "CLICK test!";
        clickOn(button);
    }

    @Test
    void testMainScreen() {
        Button button = lookup("Return to Main Screen").query();
        String expected = "CLICK test!";
        clickOn(button);
    }

    @Test
    void testRun() {
        Button button = lookup("Run").query();
        String expected = "CLICK test!";
        clickOn(button);
    }

}
