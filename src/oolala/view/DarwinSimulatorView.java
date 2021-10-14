package oolala.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import oolala.model.DarwinSimulatorModel;

import java.io.*;

public class DarwinSimulatorView extends ApplicationDisplay {

    //private static String SCRIPT_FILEPATH = "data/examples/darwin/";
    private static String SCRIPT_FILEPATH = "./oolala_team17/data/examples/darwin/";
    public final static int ANIMATION_SLIDER_START = 1;
    public final static int ANIMATION_SLIDER_MIN = 0;
    public final static int ANIMATION_SLIDER_MAX = 4;

    private boolean isPaused;
    private String selectedSpeciesName;
    private String newSpeciesName;
    private TextField setRangeField, setNameField;
    private DarwinEnvironmentView myEnvironmentView;
    private DarwinSimulatorModel myModel;
    private MenuBar speciesOptions;
    private GridPane spawnInputGrid, rangeInputGrid, nameInputGrid;
    private Slider animationSlider;
    private Button pause;
    private double creatureSensingRange;

    public DarwinSimulatorView(String language, Paint pen, Paint background, int width, int height, Scene scene, String filePath) {
        super(language, pen, background, width, height, scene, filePath);
        creatureSensingRange = 50;
    }

    @Override
    // Make user-entered URL/text field and back/next buttons
    public Node makeInputPanel () {
        VBox result = new VBox();
        setupSpeciesNameInput();
        myCommandLine = makeInputArea(350, 250);
        Button createSpeciesButton = makeButton("Create", event -> getInputAndUpdateModel());
        //this indicates the menubar for all the species present that have been created (to help identify which item to replace image for as well as setting positions on canvas)
        speciesOptions = makeMenuBar("Species");
        Button createExistingSpeciesButton = makeButton("CreateExisting", event -> updateModelWithSelection());
        result.getChildren().addAll(nameInputGrid, myCommandLine, createSpeciesButton, speciesOptions, createExistingSpeciesButton);
        result.setAlignment(Pos.CENTER);
        result.setSpacing(20);
        result.setMargin(nameInputGrid, new Insets(0, 20, 0, 20));
        result.setMargin(myCommandLine, new Insets(0, 20, 0, 20));
        return result;
    }

    @Override
    public Node makeModificationsPanel() {
        HBox result = new HBox();
        Button clearButton = makeButton("ClearSimulation", event -> clear());
        Button runButton = makeButton("RunSimulation", event -> startAnimation(animationSlider.getValue() * SECONDS_DELAY));
        pause = makeButton("PauseSimulation", e -> togglePause());
        backgroundColorPicker = background();
        //TODO -> do something with the value of the slider (edit the speed of our animation, AKA seconds delay)
        animationSlider = makeSlider(ANIMATION_SLIDER_MIN, ANIMATION_SLIDER_START, ANIMATION_SLIDER_MAX);
        result.getChildren().addAll(runButton, clearButton, pause, animationSlider, backgroundColorPicker);
        result.setSpacing(25);
        result.setAlignment(Pos.CENTER);
        result.setMargin(runButton, new Insets(0, 0, 20, 0));
        result.setMargin(clearButton, new Insets(0, 0, 20, 0));
        result.setMargin(backgroundColorPicker, new Insets(0, 0, 20, 0));
        result.setMargin(pause, new Insets(0, 0, 20, 0));
        return result;
    }

    @Override
    public Node makeFilePanel() {
        VBox result = new VBox();
        Button loadCommandFile = commandFile();
        Button saveButton = makeButton("SaveProgram", event -> {
            try {
                saveFile();
            } catch (IOException e) {
                showError(e.getMessage());
                return;
            }
        });
        setSpeciesLocation();
        setupRangeInput();
        //menuBar options for later
        Button speciesImageButton = selectImage();
        result.getChildren().addAll(loadCommandFile, saveButton, spawnInputGrid, rangeInputGrid, speciesImageButton);
        result.setAlignment(Pos.CENTER);
        result.setSpacing(20);
        return result;
    }

    private void setupSpeciesNameInput() {
        Text text1 = new Text("Name:");
        setNameField = new TextField();
        nameInputGrid = makeGridPane(200);
        nameInputGrid.add(text1, 0, 0);
        nameInputGrid.add(setNameField, 1, 0);
    }

    private void setupRangeInput() {
        Text text1 = new Text("Range:");
        setRangeField = new TextField();
        Button radiusSetup = makeButton("Range", event -> {
            try {
                creatureSensingRange = Double.parseDouble(setRangeField.getText());
                setRangeField.clear();
            } catch(NumberFormatException e) {
                showError("Not a Valid Input. Please Enter Integers.");
                setRangeField.clear();
                return;
            }

        });
        rangeInputGrid = makeGridPane(310);
        rangeInputGrid.add(text1, 0, 0);
        rangeInputGrid.add(setRangeField, 1, 0);
        rangeInputGrid.add(radiusSetup, 1, 1);
    }

    private void togglePause() {
        if(isPaused) {
            myAnimation.play();
            pause.setText(myResources.getString("PauseSimulation"));
        }
        else {
            myAnimation.pause();
            pause.setText(myResources.getString("ResumeSimulation"));
        }
        isPaused = !isPaused;
    }

    private void setSpeciesLocation() {
        Text text1 = new Text("X-Position:");
        Text text2 = new Text("Y-Position:");
        inputPositionX = new TextField();
        inputPositionY = new TextField();
        Button speciesSetup = makeButton("Location", event -> {
            try {
                creatureHomeX = Integer.parseInt(inputPositionX.getText());
                creatureHomeY = Integer.parseInt(inputPositionY.getText());
                inputPositionX.clear();
                inputPositionY.clear();
            } catch(NumberFormatException e) {
                showError("Not a Valid Input. Please Enter Integers.");
                inputPositionX.clear();
                inputPositionY.clear();
                return;
            }
        });
        spawnInputGrid = makeGridPane(200);
        addGridPaneComponents(spawnInputGrid, text1, inputPositionX, text2, inputPositionY, speciesSetup);
    }

    @Override
    public Node makeCreatureDisplay(){
        myEnvironmentView = new DarwinEnvironmentView();
        myEnvironmentView.setColor(backgroundColor);
        return myEnvironmentView.setupDisplay(myCanvasWidth, myCanvasHeight);
    }

    @Override
    protected void backgroundFunction() {
        backgroundColor = backgroundColorPicker.getValue();
        myEnvironmentView.setColor(backgroundColor);
    }

    @Override
    protected void penFunction() {}

    @Override
    public Button selectImage() {
        return makeButton("SelectSpecies", event -> {
            File selectedFile = makeFileChooser("JPEG files (*.jpeg)", "*.jpeg");
            if(selectedFile != null) {
                try {
                    InputStream selectedInputStream = new FileInputStream(selectedFile);
                    Image selectedImage = new Image(selectedInputStream);
                    myEnvironmentView.setNewSpeciesImage(selectedImage);
                } catch (IOException ex) {
                    showError(ex.getMessage());
                    return;
                }
            }
        });
    }

    @Override
    public void clear() {
        super.clear();
        myEnvironmentView.clear();
        setRangeField.clear();
        setNameField.clear();
        speciesOptions.getMenus().get(0).getItems().clear();
        selectedSpeciesName = null;
        isPaused = false;
    }

    @Override
    //moves typed input into a file and send to backend
    public void runInput(String stringFile) throws IOException {
        File file = new File(SCRIPT_FILEPATH + stringFile + ".txt");
        file.createNewFile();
        BufferedWriter bf = new BufferedWriter(new FileWriter(file));
        bf.write(myCommandLine.getText());
        bf.close();
    }

    private void addToExistingSpeciesMenu(String species){
        MenuItem item = new MenuItem(species);
        item.setOnAction(event -> {
            selectedSpeciesName = item.getText();
        });
        speciesOptions.getMenus().get(0).getItems().add(item);
    }

    private void getInputAndUpdateModel(){
        try {
            newSpeciesName = setNameField.getText();
            runInput("script");
            updateModelWithInput(SCRIPT_FILEPATH + "script.txt");
        } catch (IOException e) {
            showError(e.getMessage());
        }
    }

    @Override
    public void updateModelWithInput(String path) throws IOException {
        if(!continuing){
            // initialize the model and get ready to run the program
            myModel = new DarwinSimulatorModel(myEnvironmentView, myCanvasWidth, myCanvasHeight);
            myModel.setSensingRange(50);
        }
        continuing = true;
        myModel.setSpawn(creatureHomeX, creatureHomeY);
        myModel.setSensingRange(creatureSensingRange);
        boolean successfulCreation = myModel.createCreatureOfNewSpecies(path, newSpeciesName);
        if(!successfulCreation) {
            //TODO: make this part of English.properties
            showError("Failed Creature Creation - Invalid Spawn Location or \"" + newSpeciesName + "\" Species Already Exists");
        }
        else{
            addToExistingSpeciesMenu(newSpeciesName);
            myCommandLine.clear();
            setNameField.clear();
        }
    }

    public void updateModelWithSelection(){
        if(speciesOptions.getMenus().isEmpty() || selectedSpeciesName == null){
            //TODO: make this part of English.properties
            showError("No existing species selected");
        }
        else {
            myModel.setSpawn(creatureHomeX, creatureHomeY);
            myModel.setSensingRange(creatureSensingRange);
            boolean successfulCreation = myModel.createCreatureOfExistingSpecies(selectedSpeciesName);
            if(!successfulCreation) {
                //TODO: make this part of English.properties
                showError("Failed Creature Creation - Invalid Spawn Location");
            }
        }
    }

    @Override
    public void step() {
        if(myAnimation == null || myModel == null){
            return;
        }
        else{
            myModel.step();
        }
    }

    @Override
    public Button commandFile() {
        return makeButton("LoadProgram", e -> {
            File selectedFile = makeFileChooser("TXT files (*.txt)", "*.txt");
            try {
                int dotIndex = selectedFile.getName().indexOf(".");
                newSpeciesName = selectedFile.getName().substring(0, dotIndex);
                updateModelWithInput(selectedFile.toPath().toString());
            } catch (Exception ex) {
                return;
            }
        });
    }
}
