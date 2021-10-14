package oolala.model;

import oolala.model.FileReader.FileReader;
import oolala.model.FileReader.LSystemFileReader;

import java.io.FileNotFoundException;
import java.util.*;

public class LSystemModel {

    // List of each command string
    private List<String> LogoModelTokens;
    private List<String> LSystemCommandList;

    // parameters for settings
    private double moveLength;
    private double turnAngle;
    private int numOfLevels;

    // Maps the token string to its corresponding command object
    private Map<String, List<String>> LSystemCommandMap;
    private Map<String, String> myRules;
    private Map<Integer, String> expandedSymbols;

//    // Maps the token string to its corresponding command object
//    private Map<String, LSystemCommand> LSystemCommandMap;
    /**
     * Constructor of L-System backend model
     */
    public LSystemModel(int levelCount, double length, double angle) {
        numOfLevels = levelCount;
        moveLength = length;
        turnAngle = angle;

        LogoModelTokens = new ArrayList<>();

        LSystemCommandList = new ArrayList<>();
        tellTurtleCommand(); //command tokens that initialize a turtle in LOGO

        LSystemCommandMap = new HashMap<>();
        expandedSymbols = new HashMap<>();
        myRules = new HashMap<>();

        initializeTranslationMap();
    }

    private void tellTurtleCommand() {
        List<String> tellTurtle = Arrays.asList("tell", "1", "LSystemTurtle", "ht");
        LogoModelTokens.addAll(tellTurtle);
    }

    /**
     * Set up a map that translates the key tokens into corresponding command lists
     */
    private void initializeTranslationMap() {
        LSystemCommandMap.put("f", Arrays.asList("pd", "fd", Double.toString(moveLength)));
        LSystemCommandMap.put("g", Arrays.asList("pu", "fd", Double.toString(moveLength)));
        LSystemCommandMap.put("a", Arrays.asList("pu", "bk", Double.toString(moveLength)));
        LSystemCommandMap.put("b", Arrays.asList("pd", "bk", Double.toString(moveLength)));
        LSystemCommandMap.put("+", Arrays.asList("rt", Double.toString(turnAngle)));
        LSystemCommandMap.put("-", Arrays.asList("lt", Double.toString(turnAngle)));
        LSystemCommandMap.put("x", Arrays.asList("stamp"));
    }

    /**
     * Read the LSystem key strings from the input file
     * Use the LSystemCommandMap map to translate the keys into a list of LSystem commands
     */
    public void keyTranslation() {
        for (int index = 0; index < LSystemCommandList.size(); index++) {
            String currentToken = LSystemCommandList.get(index).toLowerCase();

            if (currentToken.equals("start")) {
                String symbol = LSystemCommandList.get(++index);
                startSymbol(symbol);
            }
            else if (currentToken.equals("rule")) {
                String variable = LSystemCommandList.get(++index);
                String rule = LSystemCommandList.get(++index);
                setUpRules(variable, rule);
            }
            else if (currentToken.equals("set")) {
                String symbol = LSystemCommandList.get(++index);
                String logoCommand = LSystemCommandList.get(++index);
                setLogoCommands(symbol, logoCommand);
            }
        }
    }

    private void startSymbol(String symbol) {
        expandedSymbols.put(0, symbol.toLowerCase());
    }

    private void setUpRules(String variable, String rule) {
        myRules.put(variable.toLowerCase(), rule.toLowerCase());
    }

    private void setLogoCommands(String symbol, String commands) {
        String[] splitCommands = commands.split(" ");
        splitCommands = trimArray(splitCommands);

        LSystemCommandMap.put(symbol, Arrays.asList(splitCommands));
    }

    /**
     * Trim each element within the command array
     * @param array
     * @return
     */
    private String[] trimArray(String[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].trim();
        }
        return array;
    }

    /**
     * Use dynamic programming to store the expanded strings at each layer
     * The expansion strings at layer i is built from the strings from layer i-1 according to the rule
     * @param rules set up the relationship between the predecessor variable and output successor
     */
    public void buildExpansion(Map<String, String> rules) {
        for (int i = 1; i <= numOfLevels; i++) {
            String previousExpansion = expandedSymbols.get(i-1);
            StringBuilder expansionBuilder = new StringBuilder();
            for (int j = 0; j < previousExpansion.length(); j++){
                String currentLetter = previousExpansion.substring(j, j+1);
                if(rules.containsKey(currentLetter)) {
                    expansionBuilder.append(rules.get(currentLetter));
                }
                else {
                    expansionBuilder.append(currentLetter);
                }
            }
            String newExpansion = expansionBuilder.toString();
            expandedSymbols.put(i, newExpansion);
        }
    }

    /**
     * Read through the string from last layer of expandedSymbols,
     * Loop through each character key, translate the key to LOGO executable commands,
     * and write all commands into the output token lists that will be used by a LOGO model.
     * LogoModelTokens will then store all the LOGO command tokens translated from the last layer of expandedSymbols
     * @throws NullPointerException
     * @throws NumberFormatException
     */
    public void writeTokenList() throws NullPointerException, NumberFormatException {
        // TODO: probably need to throw exceptions in case of incorrect outputs -> in FileReader maybe?

        String finalExpansion = expandedSymbols.get(numOfLevels);
        for (int i = 0; i < finalExpansion.length(); i++){
            String currentKey = finalExpansion.substring(i, i+1).toLowerCase();

            // translate the corresponding lists for each key and LSystemCommandList add the elements into the list
            LogoModelTokens.addAll(LSystemCommandMap.get(currentKey));
        }
    }

    /**
     * Read tokens from a .txt file that stores user input commands
     * @param s file directory
     * @throws FileNotFoundException
     */
    public void updateKeys(String s) throws FileNotFoundException {
        FileReader userTexts = new LSystemFileReader(s);
        userTexts.processInputs();
        LSystemCommandList = userTexts.getTokens();
    }

    public List<String> getLogoModelTokens() {
        return LogoModelTokens;
    }

    public List<String> getLSystemCommandList() {
        return LSystemCommandList;
    }

    public Map<Integer, String> getExpandedSymbols() {
        return expandedSymbols;
    }

    public Map<String, String> getMyRules() {
        return myRules;
    }

    public Map<String, List<String>> getLSystemCommandMap() {
        return LSystemCommandMap;
    }
}
