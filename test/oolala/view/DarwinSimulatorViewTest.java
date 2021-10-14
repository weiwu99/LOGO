package oolala.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

class DarwinSimulatorViewTest extends DukeApplicationTest {

    private final int myWidth = 1000;
    private final int myHeight = 700;
    private final Paint myBackgroundColor = Color.THISTLE;
    private final Paint penColor = Color.RED;
    private Scene myScene;
    private String language = "English";
    private final String title = "LogoTitle";
    private DarwinSimulatorView myDarwinSimulatorView;

    @Override
    public void start (Stage stage) {
        HBox myMenuRoot = new HBox();
        myScene = new Scene(myMenuRoot, myWidth, myHeight, myBackgroundColor);
        myDarwinSimulatorView = new DarwinSimulatorView(language, penColor, myBackgroundColor, myWidth, myHeight, myScene, "data/examples/logo/");
        myScene.setRoot((Parent) myDarwinSimulatorView.setupDisplay(myWidth, myHeight, title));
        stage.setScene(myScene);
        stage.show();
    }

    @Test
    void testLoadSpeciesProgram() {
        Button button = lookup("Load Species Program").query();
        String expected = "CLICK test!";
        clickOn(button);
    }

    @Test
    void testSaveSpeciesProgram() {
        Button button = lookup("Save Species Program").query();
        String expected = "CLICK test!";
        clickOn(button);
    }

    @Test
    void testSetSpawn() {
        Button button = lookup("Set Spawn").query();
        String expected = "CLICK test!";
        clickOn(button);
    }

    @Test
    void testSetSensingRange() {
        Button button = lookup("Set Sensing Range").query();
        String expected = "CLICK test!";
        clickOn(button);
    }

    @Test
    void testSpeciesImage() {
        Button button = lookup("Set Species Image").query();
        String expected = "CLICK test!";
        clickOn(button);
    }

    @Test
    void testRunSimulation() {
        Button button = lookup("Run Simulation").query();
        String expected = "CLICK test!";
        clickOn(button);
    }

    @Test
    void testClearSimulation() {
        Button button = lookup("Clear Simulation").query();
        String expected = "CLICK test!";
        clickOn(button);
    }

    @Test
    void testCreateNewSpecies() {
        Button button = lookup("Create New Species").query();
        String expected = "CLICK test!";
        clickOn(button);
    }

    @Test
    void testExistingSpecies() {
        Button button = lookup("Create Existing Species").query();
        String expected = "CLICK test!";
        clickOn(button);
    }
}