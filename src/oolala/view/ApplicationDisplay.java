package oolala.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.*;
import java.util.ResourceBundle;

public abstract class ApplicationDisplay {

    public final static double SECONDS_DELAY = 0.1;
    public final static int INPUT_FIELD_WIDTH = 40;
    public final static int BUTTON_WIDTH = 150;
    public final static int BUTTON_HEIGHT = 30;
    public final static String ALERT_MESSAGE = "Error";

    protected int sceneHeight, sceneWidth;
    protected int myCanvasHeight, myCanvasWidth;
    protected int creatureHomeX, creatureHomeY;
    protected int myModelTokenIndex;

    protected boolean continuing;
    protected String sceneLanguage, scriptFilePath;
    protected ColorPicker backgroundColorPicker, penColorPicker;
    protected Paint penColor, backgroundColor;
    protected TextArea myCommandLine;
    protected Scene mainScene;
    protected ResourceBundle myResources;
    protected Timeline myAnimation;
    protected MenuBar commandHistory;
    protected TextField inputPositionX, inputPositionY;
    protected GridPane homeInputs;

    public ApplicationDisplay(String language, Paint pen, Paint background, int width, int height, Scene scene, String filePath) {
        myResources = ResourceBundle.getBundle(MainDisplay.DEFAULT_RESOURCE_PACKAGE + language);
        commandHistory = makeMenuBar("Command History");
        sceneWidth = width;
        sceneHeight = height;
        sceneLanguage = language;
        mainScene = scene;
        penColor = pen;
        backgroundColor = background;
        scriptFilePath = filePath;
    }

    public Node setupDisplay(int sceneWidth, int sceneHeight, String title){
        myCanvasWidth = (int)((0.6) * sceneHeight);
        myCanvasHeight = (int)((0.5) * sceneWidth);
        BorderPane myRoot = new BorderPane();
        myRoot.setTop(makeTitle(title));
        myRoot.setBottom(makeModificationsPanel());
        myRoot.setCenter(makeCreatureDisplay());
        myRoot.setRight(makeInputPanel());
        myRoot.setLeft(makeFilePanel());
        return myRoot;
    }

    // Set up title for the IDE
    public Node makeTitle(String text) {
        VBox panel = new VBox();
        Label label = makeLabel(myResources.getString(text));
        panel.setAlignment(Pos.CENTER);
        Button returnHome = makeButton("Main", e -> {
            MainDisplay display = new MainDisplay(sceneLanguage, sceneWidth, sceneHeight);
            mainScene.setRoot(display.setUpComponents());
            display.changeScene(mainScene);
        });
        panel.getChildren().addAll(label, returnHome);
        panel.setMargin(label, new Insets(0, 15, 0, 0));
        return panel;
    }

    // Make user-entered URL/text field and back/next buttons
    public Node makeInputPanel() {
        VBox result = new VBox();
        myCommandLine = makeInputArea(350, 250);
        Button clearButton = makeButton("Clear", event -> clear());
        Button runButton = makeButton("Run", event -> {
            try {
                runInput("script");
                updateModelWithInput( scriptFilePath + "script.txt");
            } catch (IOException e) {
                showError(e.getMessage());
                return;
            }
        });
        result.getChildren().addAll(myCommandLine, runButton, clearButton);
        result.setAlignment(Pos.CENTER);
        result.setSpacing(20);
        result.setMargin(myCommandLine, new Insets(0, 20, 0, 10));
        return result;
    }

    public String getCommandLineText() {
        return myCommandLine.getText();
    }

    public void setCommandLineText(String text) {
        myCommandLine.setText(text);
    }

    public abstract Node makeModificationsPanel();

    public abstract Node makeFilePanel();

    public abstract Node makeCreatureDisplay();

    public ColorPicker background() {
        return makeColorPicker((Color) backgroundColor, e -> backgroundFunction());
    }

    protected abstract void backgroundFunction();

    public ColorPicker pen() {
        return makeColorPicker((Color) penColor, e -> penFunction());
    }

    protected abstract void penFunction();

    public Button commandFile() {
        return makeButton("Load", e -> {
            File selectedFile = makeFileChooser("TXT files (*.txt)", "*.txt");
            try {
                myModelTokenIndex = 0;
                updateModelWithInput(selectedFile.toPath().toString());
            } catch (IOException ex) {
                showError(ex.getMessage());
                return;
            }
        });
    }

    public abstract Button selectImage();

    public File makeFileChooser(String description, String extensions) {
        FileChooser myFileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, extensions);
        myFileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = myFileChooser.showOpenDialog(null);
        return selectedFile;
    }

    public MenuBar makeMenuBar(String text) {
        MenuBar menuBar = new MenuBar();
        Menu m = new Menu(text);
        menuBar.getMenus().add(m);
        return menuBar;
    }

    public Label makeLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        return label;
    }

    public GridPane makeGridPane(int width) {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setAlignment(Pos.CENTER);
        pane.setVgap(5);
        pane.setHgap(5);
        pane.setPrefWidth(width);
        return pane;
    }

    public Slider makeSlider(int min, int start, int max) {
        Slider slider = new Slider();
        slider.setMin(min);
        slider.setMax(max);
        slider.setValue(start);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        return slider;
    }

    // Typical code to create text field for input
    // https://stackoverflow.com/questions/25252558/javafx-how-to-make-enter-key-submit-textarea
    public TextArea makeInputArea (double height, double width) {
        TextArea cmdBox = new TextArea();
        cmdBox.setPrefColumnCount(INPUT_FIELD_WIDTH);
        cmdBox.setPrefHeight(height);
        cmdBox.setPrefWidth(width);
        return cmdBox;
    }

    public Button makeButton (String label, EventHandler<ActionEvent> handler) {
        Button result = new Button();
        result.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        result.setText(myResources.getString(label));
        result.setOnAction(handler);
        return result;
    }

    public ColorPicker makeColorPicker(Color defaultChoice, EventHandler<ActionEvent> e) {
        ColorPicker picker = new ColorPicker(defaultChoice);
        picker.setOnAction(e);
        return picker;
    }

    public void setHomeCoordinates() {
        Text text1 = new Text("X-Position:");
        Text text2 = new Text("Y-Position:");
        inputPositionX = new TextField();
        inputPositionY = new TextField();
        Button homeSetUpButton = makeButton("Home", event -> {
            try {
                creatureHomeX = Integer.parseInt(inputPositionX.getText());
                creatureHomeY = Integer.parseInt(inputPositionX.getText());
            } catch(NumberFormatException e) {
                showError("Not a Valid Input. Please Enter Integers.");
                return;
            }
        });
        homeInputs = makeGridPane(310);
        addGridPaneComponents(homeInputs, text1, inputPositionX, text2, inputPositionY, homeSetUpButton);
    }

    public void addGridPaneComponents(GridPane pane, Text text1, TextField textField1, Text text2, TextField textField2, Button button) {
        pane.add(text1, 0, 0);
        pane.add(textField1, 1, 0);
        pane.add(text2, 0, 1);
        pane.add(textField2, 1, 1);
        pane.add(button, 1, 2);
    }

    //clear the textBox and the canvas
    public void clear() {
        myCommandLine.clear();
        inputPositionX.clear();
        inputPositionY.clear();
        creatureHomeX = creatureHomeY = 0;
        myModelTokenIndex = 0;
        commandHistory.getMenus().get(0).getItems().clear();
        continuing = false;
        if (myAnimation != null) {
            myAnimation.stop();
        }
    }

    public void saveFile() throws IOException {
        TextInputDialog fileDialog = new TextInputDialog();
        fileDialog.setTitle("Text File Name Input");
        fileDialog.setHeaderText("Please Input the Name You Would Like to Save Into For Your Text File.");
        fileDialog.getDialogPane().setContentText("Name");
        fileDialog.showAndWait();
        String input = fileDialog.getEditor().getCharacters().toString();
        runInput(input);
    }

    //moves typed input into a file and send to backend
    public abstract void runInput(String stringFile) throws IOException;

    public abstract void updateModelWithInput(String path) throws IOException;

    //read each line individually and then @ end of the file stop the steps
    public abstract void step();

    public void commandHistoryUpdate(String command) {
        MenuItem item = createCommandHistoryItem(command);
        if(!checkIfItemExists(command)) {
            commandHistory.getMenus().get(0).getItems().add(item);
        }
    }

    public boolean checkIfItemExists(String command) {
        for(MenuItem item : commandHistory.getMenus().get(0).getItems()) {
            if(item.getText().equals(command)) {
                return true;
            }
        }
        return false;
    }

    public MenuItem createCommandHistoryItem(String command) {
        return new MenuItem(command);
    }

    public void startAnimation(double delay) {
        if (myAnimation != null) {
            myAnimation.stop();
        }
        myAnimation = new Timeline();
        myAnimation.setCycleCount(Timeline.INDEFINITE);
        myAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(delay), e -> step()));
        myAnimation.play();
    }

    // Display given message as an error in the GUI
    public void showError (String message) {
        if(myAnimation != null){
            myAnimation.stop();
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(ALERT_MESSAGE);
        alert.setContentText(message);
        alert.show();
    }
}
