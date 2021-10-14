package oolala.model;

import oolala.model.FileReader.FileReader;
import oolala.model.LogoCommands.*;
import oolala.view.TurtleView;

import java.io.FileNotFoundException;
import java.util.*;

public class LogoProgrammingModel {

    //private final String USER_INPUT_FILE = "data/examples/logo/telltest.txt";

    // Initializes the display of Turtle Graphics
    private TurtleView myTurtleView;

    // List of turtles depending on their activity
    private Map<String, Turtle> activeTurtles;
    private Map<String, Turtle> inactiveTurtles;

    // List of each command string extracted from user inputs
    private List<String> myTokens;

    // Maps the token string to its corresponding command object
    private Map<String, LogoCommand> commandMap;

    /**
     * Constructor of LOGO backend model
     * @param view View of Turtle Graphics
     */
    public LogoProgrammingModel(TurtleView view) {
        myTokens = new ArrayList<>();
        activeTurtles = new HashMap<>();
        inactiveTurtles = new HashMap<>();
        commandMap = new HashMap<>();
        myTurtleView = view;

        initializeCommandMap();
    }

    /**
     * Set up a map that translates the string token commands into corresponding command objects
     */
    private void initializeCommandMap() {
        commandMap.put("fd", new ForwardLogoCommand(activeTurtles));
        commandMap.put("bk", new BackwardLogoCommand(activeTurtles));
        commandMap.put("lt", new LeftTurnLogoCommand(activeTurtles));
        commandMap.put("rt", new RightTurnLogoCommand(activeTurtles));
        commandMap.put("pd", new PenDownLogoCommand(activeTurtles));
        commandMap.put("pu", new PenUpLogoCommand(activeTurtles));
        commandMap.put("st", new ShowTurtleLogoCommand(activeTurtles));
        commandMap.put("ht", new HideTurtleLogoCommand(activeTurtles));
        commandMap.put("home", new HomeLogoCommand(activeTurtles));
        commandMap.put("stamp", new StampLogoCommand(activeTurtles));
        commandMap.put("tell", new TellLogoCommand(activeTurtles));
    }

    /**
     * Convert the token @ index to its corresponding action
     * @param index the current index in the token arraylist
     * @return the updated index
     * @throws NullPointerException
     * @throws NumberFormatException
     */
    public int tokenToAction(int index) throws NullPointerException, NumberFormatException {

        // TODO: probably need to throw exceptions in case of incorrect outputs -> in FileReader maybe?
        String command = myTokens.get(index).toLowerCase();

        // perform the corresponding actions for this token and output the updated index
        index = commandMap.get(command).performAction(myTokens, index, myTurtleView, inactiveTurtles);

        // increment and return the next index
        index++;
        return index;
    }

    public String tokenToString(int index) throws NullPointerException {
        return commandMap.get(myTokens.get(index).toLowerCase()).getCommand();
    }

    public void performActionHistory(String command) throws NullPointerException, NumberFormatException {
        List<String> commands = new ArrayList<>();
        String [] splitCommands = command.split(" ");
        commands.addAll(Arrays.asList(splitCommands));
        commandMap.get(commands.get(0).toLowerCase()).performAction(commands, 0, myTurtleView, inactiveTurtles);
    }

    /**
     * Read tokens from script.txt file that stores user inputs
     * @param s file directory
     * @throws FileNotFoundException
     */
    public void updateTokens(String s) throws FileNotFoundException {
        FileReader userTexts = new FileReader(s);
        userTexts.processInputs();
        myTokens = userTexts.getTokens();
    }

    public Map<String, LogoCommand> getCommandMap() {
        return commandMap;
    }

    public List<String> getMyTokens() {
        return myTokens;
    }

    public void setMyTokens(List<String> myTokens) {
        this.myTokens = myTokens;
    }

    public Map<String, Turtle> getActiveTurtles() {
        return activeTurtles;
    }

    public Map<String, Turtle> getInactiveTurtles() {
        return inactiveTurtles;
    }

    public void resetTokens() {
        myTokens = new ArrayList<>();
    }

    /**
     * Sets the home location for new Turtle objects to be spawned at by relaying the desired location to the
     * TellLogoCommand object in the model's map of commands
     * @param x the x-coordinate of the desired home location
     * @param y the y-coordinate of the desired home location
     */
    public void setHome(double x, double y){
        TellLogoCommand temp = (TellLogoCommand) commandMap.get("tell");
        temp.setHome(x, y);
        for(Map.Entry currentElement : activeTurtles.entrySet()){
            Turtle currentTurtle = (Turtle) currentElement.getValue();
            currentTurtle.setHome(x, y);
        }
        for(Map.Entry currentElement : inactiveTurtles.entrySet()){
            Turtle currentTurtle = (Turtle) currentElement.getValue();
            currentTurtle.setHome(x, y);
        }
    }
}
