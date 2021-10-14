package oolala.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import oolala.model.LSystemModel;
import oolala.model.LogoProgrammingModel;

import java.io.*;

public class LSystemView extends ApplicationDisplay {

    private static String SCRIPT_FILEPATH = "data/examples/lsystem/";
    //private static String SCRIPT_FILEPATH = "data/examples/lsystem/";

    private TurtleView myTurtleView;
    private boolean [] inputs = {false, false, false};
    private int movementLength, movementAngle, numberOfLevels;
    private TextField length, angle;
    private LogoProgrammingModel myModel;
    private LSystemModel systemModel;
    private GridPane patternInputs;

    public LSystemView(String language, Paint pen, Paint background, int width, int height, Scene scene, String filePath) {
        super(language, pen, background, width, height, scene, filePath);
    }

    @Override
    public Node makeModificationsPanel() {
        HBox result = new HBox();
        backgroundColorPicker = background();
        penColorPicker = pen();
        setHomeCoordinates();
        setPatternInputs();
        Button setLevels = makeButton("Level", e -> {
            try {
                assignLevels();
                inputs[2] = true;
            } catch (IOException ex) {
                showError(ex.getMessage());
                return;
            }
        });
        result.getChildren().addAll(patternInputs, homeInputs, setLevels, backgroundColorPicker, penColorPicker);
        result.setSpacing(20);
        result.setAlignment(Pos.CENTER);
        result.setMargin(backgroundColorPicker, new Insets(0, 0, 20, 0));
        result.setMargin(setLevels, new Insets(0, 0, 20, 0));
        result.setMargin(penColorPicker, new Insets(0, 20, 20, 0));
        return result;
    }

    @Override
    public Node makeFilePanel() {
        VBox result = new VBox();
        commandHistory = makeMenuBar("Command History");
        Button loadCommandFile = commandFile();
        Button saveButton = makeButton("Save", event -> {
            try {
                saveFile();
            } catch (IOException e) {
                showError(e.getMessage());
                return;
            }
        });
        Button leafImageButton = selectImage();
        result.getChildren().addAll(commandHistory, loadCommandFile, saveButton, leafImageButton);
        result.setAlignment(Pos.CENTER);
        result.setSpacing(50);
        result.setMargin(commandHistory, new Insets(0, 20, 0, 20));
        return result;
    }

    @Override
    public Node makeCreatureDisplay() {
        myTurtleView = new TurtleView();
        myTurtleView.setColors(penColor, backgroundColor);
        return myTurtleView.setupDisplay(myCanvasWidth, myCanvasHeight);
    }

    @Override
    protected void backgroundFunction(){
        if(backgroundColorPicker.getValue().equals(penColor)) {
            showError("You have made the background color the same has the Turtle pen color. Please make these colors different.");
            backgroundColorPicker.setValue((Color) backgroundColor);
            return;
        }
        backgroundColor = backgroundColorPicker.getValue();
        myTurtleView.setColors(penColor, backgroundColor);
    }

    @Override
    protected void penFunction(){
        if(penColorPicker.getValue().equals(backgroundColor)) {
            showError("You have made the Turtle pen color the same has the background color. Please make these colors different.");
            penColorPicker.setValue((Color) penColor);
            return;
        }
        penColor = penColorPicker.getValue();
        myTurtleView.setColors(penColor, backgroundColor);
    }

    @Override
    public Button selectImage() {
        return makeButton("SelectLeaf", event -> {
            File selectedFile = makeFileChooser("JPEG files (*.jpeg)", "*.jpeg");
            try {
                InputStream selectedInputStream = new FileInputStream(selectedFile);
                Image selectedTurtleImage = new Image(selectedInputStream);
                myTurtleView.setCurrentCreatureImage(selectedTurtleImage);
            } catch (Exception ex) {
                return;
            }
        });
    }

    private void assignLevels() throws IOException {
        TextInputDialog fileDialog = new TextInputDialog();
        fileDialog.setTitle("Number of Levels Input");
        fileDialog.setHeaderText("Please input the number of levels you would like to simulate for your inputted program.");
        fileDialog.getDialogPane().setContentText("Levels");
        fileDialog.showAndWait();
        numberOfLevels = Integer.parseInt(fileDialog.getEditor().getCharacters().toString());
    }

    private void setPatternInputs() {
        Text text1 = new Text("Length:");
        Text text2 = new Text("Angle:");
        length = new TextField();
        angle = new TextField();
        Button patternSetUp = makeButton("Pattern", event -> {
            movementLength = Integer.parseInt(length.getText());
            inputs[0] = true;
            movementAngle = Integer.parseInt(angle.getText());
            inputs[1] = true;
        });
        patternInputs = makeGridPane(310);
        addGridPaneComponents(patternInputs, text1, length, text2, angle, patternSetUp);
    }

    @Override
    public void clear() {
        super.clear();
        length.clear();
        angle.clear();
        myTurtleView.clear();
        for(int i = 0; i < inputs.length; i++) {
            inputs[i] = false;
        }
    }

    @Override
    //moves typed input into a file and send to backend
    public void runInput(String stringFile) throws IOException {
        File file = new File(SCRIPT_FILEPATH + stringFile + ".txt");
        file.createNewFile();
        BufferedWriter bf = new BufferedWriter(new FileWriter(file));
        bf.write(myCommandLine.getText());
        bf.close();
        myModelTokenIndex = 0;
    }

    @Override
    public void updateModelWithInput(String path) throws IOException {
        for(int i = 0; i < inputs.length; i++) {
            if(!inputs[i]) {
                throw new IOException("Please enter a length, angle, and number of levels before running a program.");
            }
        }
        clear();
        // initialize the model and get ready to run the program
        myModel = new LogoProgrammingModel(myTurtleView);
        systemModel = new LSystemModel(numberOfLevels, movementLength, movementAngle);
        myModel.setHome(creatureHomeX, creatureHomeY);
        // start to receive all tokens from the input file
        systemModel.updateKeys(path);
        systemModel.keyTranslation();
        systemModel.buildExpansion(systemModel.getMyRules());
        systemModel.writeTokenList();
        myModel.setMyTokens(systemModel.getLogoModelTokens());
        continuing = true;
        startAnimation(SECONDS_DELAY);
    }

    //TODO: refactor step once everything is done
    @Override
    //read each line individually and then @ end of the file stop the steps
    public void step() {
        int startIndex = myModelTokenIndex;
        // myIndex indicates which token we are at right now
        try {
            myModelTokenIndex = myModel.tokenToAction(myModelTokenIndex);
        } catch(Exception e) {
            showError("Not a valid command input. Please re-enter a valid input or re-upload a valid file.");
            clear();
            return;
        }
        String command = myModel.tokenToString(startIndex);
        MenuItem item = createCommandHistoryItem(command);
        if(!checkIfItemExists(command)) {
            commandHistory.getMenus().get(0).getItems().add(item);
        }
        if (myAnimation != null && myModelTokenIndex >= myModel.getMyTokens().size()) {
            myAnimation.stop();
        }
    }

    @Override
    public MenuItem createCommandHistoryItem(String command) {
        MenuItem item = new MenuItem(command);
        item.setOnAction(event -> {
            try {
                myModel.performActionHistory(command);
            } catch (Exception e) {
                showError("Invalid inputs for the associated command. Please try again.");
                clear();
                return;
            }
        });
        return item;
    }
}
