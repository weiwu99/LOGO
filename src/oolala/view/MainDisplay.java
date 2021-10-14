package oolala.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ResourceBundle;

public class MainDisplay {

    private final static String logoTitle = "LogoTitle";
    private final static String lSystemTitle = "SystemTitle";
    private final static String darwinTitle = "SimulationTitle";

    private int myWidth, myHeight;
    private String displayLanguage;
    private Scene myScene;
    private ResourceBundle myResources;
    public static final String DEFAULT_RESOURCE_PACKAGE = "oolala.view.resources.";
    public static final String DEFAULT_STYLESHEET = "resources/Design.css";

    public MainDisplay(String language, int width, int height) {
        myWidth = width;
        myHeight = height;
        displayLanguage = language;
        myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + language);
    }

    public VBox setUpComponents() {
        VBox myMenuRoot = new VBox();
        HBox horizontal = new HBox();

        // Create one button for each possible simulation
        Button startLogoIDEButton = makeButton("Logo", value -> startLogoIDE());
        Button startLSystemButton = makeButton("L-System", value -> startLSystem());
        Button startDarwinButton = makeButton("Darwin", value -> startDarwin());

        horizontal.getChildren().addAll(startLogoIDEButton, startLSystemButton, startDarwinButton);
        horizontal.setAlignment(Pos.BOTTOM_CENTER);
        horizontal.setMargin(startLogoIDEButton, new Insets(100, 20, 0, 20));
        horizontal.setMargin(startLSystemButton, new Insets(100, 0, 0, 0));
        horizontal.setMargin(startDarwinButton, new Insets(100, 20, 0, 20));
        myMenuRoot.getChildren().add(horizontal);
        myMenuRoot.setAlignment(Pos.CENTER);
        return myMenuRoot;
    }

    public Scene setupDisplay(){
        myScene = new Scene(setUpComponents(), myWidth, myHeight);
        myScene.getStylesheets().add(getClass().getResource(DEFAULT_STYLESHEET).toExternalForm());
        return myScene;
    }

    private void startLogoIDE() {
        LogoIDEView myLogoIDEView = new LogoIDEView(displayLanguage, Color.BLACK, Color.CORNSILK, myWidth, myHeight, myScene, "data/examples/logo/");
        myScene.setRoot((Parent) myLogoIDEView.setupDisplay(myWidth, myHeight, logoTitle));
    }

    private void startLSystem(){
        LSystemView myLSystemVisualizer = new LSystemView(displayLanguage, Color.BLACK, Color.CORNSILK, myWidth, myHeight, myScene, "data/examples/lsystem/");
        myScene.setRoot((Parent) myLSystemVisualizer.setupDisplay(myWidth, myHeight, lSystemTitle));
    }

    private void startDarwin(){
        DarwinSimulatorView myDarwinSimulatorView = new DarwinSimulatorView(displayLanguage, Color.BLACK, Color.CORNSILK, myWidth, myHeight, myScene, "data/examples/darwin/");
        myScene.setRoot((Parent) myDarwinSimulatorView.setupDisplay(myWidth, myHeight, darwinTitle));
    }

    private Button makeButton(String property, EventHandler<ActionEvent> response) {
        Button result = new Button();
        result.setText(myResources.getString(property));
        result.setOnAction(response);
        return result;
    }

    public void changeScene(Scene scene) {
        myScene = scene;
    }
}
