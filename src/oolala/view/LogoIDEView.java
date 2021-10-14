package oolala.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import oolala.model.LogoProgrammingModel;
import oolala.model.Turtle;

import java.io.*;
import java.util.Map;

public class LogoIDEView extends ApplicationDisplay {

    private TurtleView myTurtleView;
    private LogoProgrammingModel myModel;
    private Slider slider;
    private MenuBar turtleStatus;

    public LogoIDEView(String language, Paint pen, Paint background, int width, int height, Scene scene, String filePath) {
        super(language, pen, background, width, height, scene, filePath);
    }

    @Override
    public Node makeModificationsPanel() {
        HBox result = new HBox();
        slider = makeSlider(1, 10, 20);
        setHomeCoordinates();
        turtleStatus = makeMenuBar("Turtle Status");
        backgroundColorPicker = background();
        penColorPicker = pen();
        result.getChildren().addAll(slider, homeInputs, turtleStatus, backgroundColorPicker, penColorPicker);
        result.setSpacing(30);
        result.setAlignment(Pos.CENTER);
        result.setMargin(slider, new Insets(0, 0, 20, 0));
        result.setMargin(backgroundColorPicker, new Insets(0, 0, 20, 0));
        result.setMargin(penColorPicker, new Insets(0, 0, 20, 0));
        return result;
    }

    @Override
    public Node makeFilePanel() {
        VBox result = new VBox();
        commandHistory = makeMenuBar("Command History");
        Button loadCommandFile = commandFile();
        Button chooseImageButton = selectImage();
        Button saveButton = makeButton("Save", event -> {
            try {
                saveFile();
            } catch (IOException e) {
                showError("Unable to convert into a .txt file. Please enter a valid path.");
                return;
            }
        });
        result.getChildren().addAll(commandHistory, loadCommandFile, chooseImageButton, saveButton);
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
        return makeButton("SelectImage", event -> {
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

    @Override
    public void clear(){
        super.clear();
        myTurtleView.clear();
        myModel.setHome(0, 0);
        turtleStatus.getMenus().get(0).getItems().clear();
    }

    @Override
    //moves typed input into a file and send to backend
    public void runInput(String stringFile) throws IOException {
        File file = new File(scriptFilePath + stringFile + ".txt");
        file.createNewFile();
        BufferedWriter bf = new BufferedWriter(new FileWriter(file));
        bf.write(myCommandLine.getText());
        bf.close();
        myModelTokenIndex = 0;
    }

    @Override
    public void updateModelWithInput(String path) throws IOException {
        turtleStatus.getMenus().get(0).getItems().clear();
        if(!continuing) {
            // initialize the model and get ready to run the program
            myModel = new LogoProgrammingModel(myTurtleView);
        }
        else {
            myModel.resetTokens();
        }
        myModel.setHome(creatureHomeX, creatureHomeY);
        myTurtleView.setPenWidth(slider.getValue());
        // start to receive all tokens from the input file
        myModel.updateTokens(path);
        continuing = true;
        startAnimation(SECONDS_DELAY);
    }

    //TODO: refactor step once everything is done
    @Override
    //read each line individually and then @ end of the file stop the steps
    public void step() {
        int startIndex = myModelTokenIndex;
        try {
            myModelTokenIndex = myModel.tokenToAction(myModelTokenIndex);
        } catch(Exception e) {
            showError("Not a valid command input. Please re-enter a valid input or re-upload a valid file.");
            clear();
            return;
        }
        commandHistoryUpdate(myModel.tokenToString(startIndex));
        if (myAnimation != null && myModelTokenIndex >= myModel.getMyTokens().size()) {
            createSubMenu(myModel.getActiveTurtles(), turtleStatus);
            createSubMenu(myModel.getInactiveTurtles(), turtleStatus);
            myAnimation.stop();
        }
    }

    @Override
    public MenuItem createCommandHistoryItem(String command) {
        MenuItem item = new MenuItem(command);
        item.setOnAction(event -> {
            try {
                myTurtleView.setPenWidth(slider.getValue());
                myModel.performActionHistory(command);
            } catch (Exception e) {
                showError("Invalid inputs for the associated command. Please try again.");
                clear();
                return;
            }
        });
        return item;
    }

    public void createSubMenu(Map<String, Turtle> turtles, MenuBar menuBar) {
        for(String s : turtles.keySet()) {
            Menu subMenu = new Menu(s);
            double[] location = turtles.get(s).moveDistance(0);
            double angle = turtles.get(s).changeAngle(0);
            MenuItem coordinates = new MenuItem("x-coordinate: " + location[0] + ", y-coordinate: " + location[1] + " " + ", Orientation: " + angle);
            subMenu.getItems().add(coordinates);
            menuBar.getMenus().get(0).getItems().add(subMenu);
        }
    }
}
