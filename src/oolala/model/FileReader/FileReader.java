package oolala.model.FileReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FileReader {

    private static final char COMMENT_STARTER = '#';
    private static final String EMPTY = "";

    private File file;

    protected List<String> tokens;


    public FileReader (String fileName) {
        file = new File(fileName);
        tokens = new ArrayList<>();
    }

    /**
     * Process the raw user input file and convert the texts
     * into a list of tokens that will be used by LOGO model
     * @throws FileNotFoundException
     */
    public void processInputs() throws FileNotFoundException {
        List<String> commandLine = readFileToList();

        extractTokens(commandLine);
    }

    protected void extractTokens(List<String> commands) {

        for (int i = 0; i < commands.size(); i++) {
            String[] splitCommands = commands.get(i).split(" ");
            splitCommands = trimArray(splitCommands);

            tokens.addAll(Arrays.asList(splitCommands));
        }

        // clean up commands again
        processCommands(tokens);
    }

    /**
     * Read the txt file and convert input texts into different commands
     * @return the converted commands for each line
     * @throws FileNotFoundException
     */
    private List<String> readFileToList() throws FileNotFoundException {
        Scanner sc = new Scanner(file); // https://www.geeksforgeeks.org/scanner-class-in-java/

        String buffer = scanFile(sc);

        // each row saves one line of code
        String[] rowCommands = buffer.split("\n");

        List<String> commands = new ArrayList<>();
        commands.addAll(Arrays.asList(rowCommands));

        // remove comment lines that are separate from command lines
        processCommands(commands);

        return commands;
    }

    /**
     * Scan the text files and write the values into a string
     * @param sc
     * @return
     */
    private String scanFile(Scanner sc) {
        StringBuilder builder = new StringBuilder();

        while (sc.hasNextLine()) {
            builder.append(sc.nextLine() + "\n");
        }
        sc.close();

        return builder.toString();
    }

    /**
     * Process each line of command so that they are viable for execution
     * @param commandLine
     */
    protected void processCommands(List<String> commandLine) {
        for (int i = 0; i < commandLine.size(); i++) {

            // Trim all lines before other processing procedures
            commandLine.set(i, commandLine.get(i).trim().toLowerCase());

            String current = commandLine.get(i);
            // delete all empty lines
            if (current.equals(EMPTY)) {
                commandLine.remove(i);
                i --;
            }

            // delete all separate comment lines
            else if (current.charAt(0) == COMMENT_STARTER){
                commandLine.remove(i);
                i --;
            }

            // delete inline comments
            else if (current.indexOf(COMMENT_STARTER) != -1) {
                String newCurrent = current.substring(0, current.indexOf(COMMENT_STARTER)).trim();
                commandLine.set(i, newCurrent);
            }
        }
    }

    /**
     * Trim each element within the command array
     * @param array
     * @return
     */
    protected String[] trimArray(String[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].trim();
        }
        return array;
    }

    // A bunch of getters
    public List<String> getTokens() {
        return tokens;
    }

}
