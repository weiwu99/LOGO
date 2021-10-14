package oolala.view;

import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

/**
 * Feel free to completely change this code or delete it entirely. 
 */
class MainDisplayTest extends DukeApplicationTest {

    // keep only if needed to call application methods in tests
    private MainDisplay myDisplay;
    // keep GUI components used in multiple tests
    private String language = "English";
    private final int myWidth = 1000;
    private final int myHeight = 700;

    @Override
    public void start (Stage stage) {
        myDisplay = new MainDisplay(language, myWidth, myHeight);
        myDisplay.setUpComponents();
        stage.setScene(myDisplay.setupDisplay());
        stage.show();
    }

    @Test
    void testLogoIDE() {
        Button button = lookup("Start Logo IDE").query();
        String expected = "CLICK test!";
        clickOn(button);
    }

    @Test
    void testLSystemIDE() {
        Button button = lookup("Start L-System Visualizer").query();
        String expected = "CLICK test!";
        clickOn(button);
    }

    @Test
    void testDarwinIDE() {
        Button button = lookup("Start Darwin Simulator").query();
        String expected = "CLICK test!";
        clickOn(button);
    }
}
